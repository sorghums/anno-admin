package site.sorghum.anno.method;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.method.processor.MTBasicProcessor;
import site.sorghum.anno.method.processor.MethodBasicProcessor;
import site.sorghum.anno.method.resource.JarResource;
import site.sorghum.anno.method.resource.ResourceFinder;
import site.sorghum.anno.method.route.MethodRoute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 方法模版工厂类
 *
 * @author songyinyin
 * @since 2024/1/15 15:21
 */
@Slf4j
public class MethodTemplateManager {

    public static final String METHOD_PATH = "method";
    public static final double MT_DEFAULT_INDEX = 1000;

    private static final Map<String, List<MethodBasicProcessor>> methodTemplateMap = new HashMap<>();
    private static final Map<String, Set<MTProcessorInfo>> processorInfoMap = new HashMap<>();
    private static final Map<Class<?>, MethodRoute> methodRouteBeanMap = new HashMap<>();
    private static final Map<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();

    public static <T> T create(Class<T> clazz) {
        Object proxy = proxyCache.computeIfAbsent(clazz, k -> {
            Object object = Proxy.newProxyInstance(k.getClassLoader(), new Class[]{k}, new MethodTemplateInvocationHandler());
            if (log.isDebugEnabled()) {
                log.debug("This MethodTemplate created successfully: {}", k.getName());
            }
            return object;
        });

        return (T) proxy;
    }

    public static void parse(String packageName) throws IOException {
        if (methodRouteBeanMap.isEmpty()) {
            Set<Class<?>> methodRouteClass = ClassUtil.scanPackageBySuper(MethodRoute.class.getPackageName(), MethodRoute.class);
            for (Class<?> routeClass : methodRouteClass) {
                MethodRoute methodRoute = (MethodRoute) ReflectUtil.newInstance(routeClass);
                methodRouteBeanMap.put(routeClass, methodRoute);
            }
        }
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName, MethodTemplate.class);

        // 解析决策表
        parse(classes, METHOD_PATH + "/*.csv", METHOD_PATH + "/**/*.csv");

        // 兼容 AnnoBaseProxy#supportEntities
        supportOld();

        // 兼容无决策表的情况
        supportNoRuleCsv(classes);

        infoToMtMap();
    }

    /**
     * 没有定义决策表，或者决策表中的部件不完整时，把所有方法模版接口的实现都追加进来
     */
    private static void supportNoRuleCsv(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            MethodTemplate methodTemplate = AnnotationUtil.getAnnotation(clazz, MethodTemplate.class);
            if (methodTemplate == null) {
                continue;
            }
            Set<String> mtMethods = Arrays.stream(findMethods(clazz)).map(Method::getName).collect(Collectors.toSet());

            Collection<?> beans = AnnoBeanUtils.getBeansOfType(clazz);
            for (Object bean : beans) {
                Method[] methods = findMethods(bean.getClass());
                for (Method method : methods) {
                    if (!mtMethods.contains(method.getName())) {
                        continue;
                    }

                    String subPath;
                    if (StrUtil.isNotBlank(methodTemplate.ruleDir())) {
                        subPath = methodTemplate.ruleDir();
                    } else {
                        continue;
                    }
                    String fullPath = "%s/%s/%s.csv".formatted(METHOD_PATH, subPath, method.getName());

                    Set<MTProcessorInfo> processorInfos = processorInfoMap.computeIfAbsent(fullPath, k -> new LinkedHashSet<>());
                    Optional<MTProcessorInfo> max = processorInfos.stream().max(Comparator.comparing(MTProcessorInfo::getIndex));
                    double index = max.map(mtProcessorInfo -> mtProcessorInfo.getIndex() + 1).orElse(10000D);

                    MethodUnit methodUnit = AnnotationUtil.getAnnotation(method, MethodUnit.class);

                    MTProcessorInfo info = new MTProcessorInfo();
                    info.setBeanName(StrUtil.lowerFirst(bean.getClass().getSimpleName()));
                    info.setBean(bean);
                    info.setMethodName(method.getName());
                    info.setExclude(false);
                    info.setFullPath(fullPath);
                    if (methodUnit != null) {
                        info.setPhase(methodUnit.phase());
                        info.setIndex(methodUnit.index());
                    } else {
                        info.setPhase(ExecutePhase.EXECUTE);
                        info.setIndex(index);
                    }
                    processorInfos.add(info);
                }
            }
        }
    }

    /**
     * 兼容 AnnoBaseProxy#supportEntities
     */
    private static void supportOld() {

        List<AnnoBaseProxy> proxies = AnnoBeanUtils.getBeansOfType(AnnoBaseProxy.class);
        for (AnnoBaseProxy<?> proxy : proxies) {
            if (ArrayUtil.isEmpty(proxy.supportEntities())) {
                continue;
            }
            String[] supportEntities = proxy.supportEntities();
            Method[] methods = findMethods(AnnoBaseProxy.class);
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equals("supportEntities")) {
                    continue;
                }
                MethodUnit methodUnit = AnnotationUtil.getAnnotation(method, MethodUnit.class);
                for (String supportEntity : supportEntities) {

                    MTProcessorInfo info = new MTProcessorInfo();
                    info.setBeanName(AnnoBeanUtils.getBeanName(proxy.getClass()));
                    info.setBean(proxy);
                    info.setMethodName(methodName);
                    info.setExclude(false);

                    String key = "method/all/" + methodName + ".csv";
                    if (StrUtil.equals(supportEntity, "PrimaryKeyModel")) {
                        info.setCondition("mt.instanceofPrimaryKeyModel(p0)");
                    } else if (StrUtil.equals(supportEntity, "PrimaryKeyModel.BaseMetaModel")) {
                        info.setCondition("mt.instanceofBaseMetaModel(p0)");
                    } else if (StrUtil.equals(supportEntity, "PrimaryKeyModel.BaseMetaModel.BaseOrgMetaModel")) {
                        info.setCondition("mt.instanceofBaseOrgMetaModel(p0)");
                    } else {
                        String[] split = supportEntity.split("\\.");
                        String entityName = split[split.length - 1];
                        key = "method/" + entityName + "/" + methodName + ".csv";
                    }
                    info.setFullPath(key);
                    if (methodUnit != null) {
                        info.setPhase(methodUnit.phase());
                        info.setIndex(methodUnit.index());
                    } else {
                        info.setPhase(ExecutePhase.EXECUTE);
                        info.setIndex(proxy.index());
                    }

                    Set<MTProcessorInfo> mtProcessorInfos = processorInfoMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
                    mtProcessorInfos.add(info);
                }
            }
        }
    }

    private static Method[] findMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        // 过滤掉桥接方法 静态方法
        List<Method> resultMethods = Arrays.stream(methods).filter(e -> !e.isBridge() && !Modifier.isStatic(e.getModifiers())).toList();
        return resultMethods.toArray(new Method[0]);
    }


    public static void parse(Set<Class<?>> classes, String... locationPatterns) throws IOException {

        MultiResource multiResource = new MultiResource();
        for (String locationPattern : locationPatterns) {
            MultiResource resources = ResourceFinder.of().find(locationPattern);
            for (Resource resource : resources) {
                multiResource.add(resource);
            }
        }
        Map<String, List<MTProcessorInfo>> infoMap = new HashMap<>();
        for (Class<?> methodTemplateClass : classes) {
            Method[] publicMethods = findMethods(methodTemplateClass);
            for (Method publicMethod : publicMethods) {

                Map<String, List<MTProcessorInfo>> map = parseMethodTemplates(publicMethod, multiResource);
                if (CollUtil.isNotEmpty(map)) {
                    infoMap.putAll(map);
                }
            }
        }
        if (CollUtil.isEmpty(infoMap)) {
            return;
        }

        for (String key : infoMap.keySet()) {
            Set<MTProcessorInfo> infos = processorInfoMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
            infos.addAll(infoMap.get(key));

            if (CollUtil.isEmpty(infos)) {
                continue;
            }
            List<String> excludeBeanNames = infos.stream()
                .filter(MTProcessorInfo::isExclude)
                .map(MTProcessorInfo::getBeanMethodName)
                .toList();

            Set<MTProcessorInfo> list = infos.stream()
                .filter(e -> StrUtil.isNotBlank(e.getBeanName()) && !excludeBeanNames.contains(e.getBeanMethodName()))
                .sorted(Comparator.comparing(MTProcessorInfo::getIndex))
                .collect(Collectors.toCollection(LinkedHashSet::new));
            if (CollUtil.isNotEmpty(list)) {
                processorInfoMap.put(key, list);
            }
        }
    }

    public static void infoToMtMap() {
        for (Map.Entry<String, Set<MTProcessorInfo>> entry : processorInfoMap.entrySet()) {
            String key = entry.getKey();
            Set<MTProcessorInfo> value = entry.getValue();
            List<MethodBasicProcessor> process = value.stream()
                .map(MethodTemplateManager::createProcessor)
                .collect(Collectors.toList());
            sortProcessors(process);
            methodTemplateMap.put(key, process);
        }
    }

    public static void clear() {
        methodTemplateMap.clear();
        processorInfoMap.clear();
        methodRouteBeanMap.clear();
        proxyCache.clear();
    }

    public static boolean isSupportMethod(Method method) {
        return !method.getDeclaringClass().equals(Object.class);
    }

    private static Map<String, List<MTProcessorInfo>> parseMethodTemplates(Method publicMethod, MultiResource resources) throws IOException {
        Pair<String, String> pair = getMethodFileName(publicMethod);
        String methodName = publicMethod.getName();
        Map<String, List<MTProcessorInfo>> infoMap = new HashMap<>();
        for (Resource resource : resources) {
            String file = resource.getName();
            if (!StrUtil.equals(file, pair.getValue())) {
                continue;
            }
            FileResource fileResource = null;
            if (resource instanceof FileResource variableFileResource) {
                fileResource = variableFileResource;
            }
            if (resource instanceof JarResource variableJarResource) {
                // 伪装为文件资源
                fileResource = new FileResource(variableJarResource.getFile());
            }
            if (fileResource == null) {
                continue;
            }
            List<String> parentPathList = new ArrayList<>();
            getParentPath(parentPathList, fileResource.getFile());
            // method/test_sayHello.csv
            String fullPath = parentPathList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.joining("/")) + "/" + pair.getValue();
            fullPath = FileUtil.normalize(fullPath);
            try (BufferedReader bufferedReader = resource.getReader(StandardCharsets.UTF_8)) {
                CsvReader reader = CsvUtil.getReader();
                List<MethodTemplateCsv> list = reader.read(bufferedReader, MethodTemplateCsv.class);
                if (CollUtil.isEmpty(list)) {
                    continue;
                }
                List<MTProcessorInfo> processorInfos = infoMap.computeIfAbsent(fullPath, k -> new ArrayList<>());
                for (MethodTemplateCsv templateCsv : list) {
                    MTProcessorInfo info = new MTProcessorInfo();
                    info.setPhase(templateCsv.getPhase());
                    info.setIndex(templateCsv.getIndex());
                    // 小写首字母
                    info.setBeanName(StrUtil.lowerFirst(templateCsv.getBeanName()));
                    info.setMethodName(templateCsv.getMethodName());
                    info.setExclude(templateCsv.isExclude());
                    info.setCondition(templateCsv.getCondition());
                    info.setFullPath(fullPath);
                    info.setMtMethodName(methodName);
                    info.deal();
                    processorInfos.add(info);
                }
            }
        }
        return infoMap;
    }

    private static MethodBasicProcessor createProcessor(MTProcessorInfo info) {
        Object bean = info.getBean();
        if (bean == null) {
            bean = AnnoBeanUtils.getBean(info.getBeanName());
        }
        if (bean == null) {
            bean = AnnoBeanUtils.getBean(ClassUtil.loadClass(StrUtil.upperFirst(info.getBeanName())));
        }
        if (bean == null) {
            throw new MTException("Bean: " + info.getBeanName() + " not found");
        }
        if (StrUtil.isBlank(info.getMethodName())) {
            if (bean instanceof MTBasicProcessor) {
                return new MethodBasicProcessor(bean, "process", info);
            } else {
                return new MethodBasicProcessor(bean, info.getMtMethodName(), info);
            }
        } else {
            return new MethodBasicProcessor(bean, info.getMethodName(), info);
        }
    }

    public static void getParentPath(List<String> parentPathList, File file) {
        String parentFileName = file.getParentFile().getName();
        if (StrUtil.equals(parentFileName, METHOD_PATH)) {
            parentPathList.add(parentFileName);
        } else {
            parentPathList.add(parentFileName);
            getParentPath(parentPathList, file.getParentFile());
        }
    }

    /**
     * 获取方法文件名
     *
     * @return key: 文件夹, value: 文件名
     */
    public static Pair<String, String> getMethodFileName(Method method) {
        MethodTemplate annotation = getMethodTemplateAnnotation(method);
        return Pair.of(annotation.ruleDir(), method.getName() + ".csv");
    }

    private static MethodTemplate getMethodTemplateAnnotation(Method method) {
        MethodTemplate annotation = AnnotationUtil.getAnnotation(method.getDeclaringClass(), MethodTemplate.class);
        if (annotation == null) {
            annotation = AnnotationUtil.getAnnotation(method, MethodTemplate.class);
        }
        if (annotation == null) {
            throw new MTException("MethodTemplate annotation not found in class: %s or method: %s".formatted(method.getDeclaringClass().getName(), method.getName()));
        }
        return annotation;
    }

    public static List<MethodBasicProcessor> getMethodProcessors(MTContext context) {
        MethodTemplate methodTemplate = getMethodTemplateAnnotation(context.getMethod());
        Class<? extends MethodRoute> routeClass = methodTemplate.route();
        MethodRoute methodRoute = methodRouteBeanMap.get(routeClass);
        String[] route = methodRoute.route(context);
        List<String> routeList = route == null ? null : Arrays.stream(route).filter(StrUtil::isNotBlank).toList();
        Pair<String, String> pair = getMethodFileName(context.getMethod());
        String methodFileName = METHOD_PATH + "/" + pair.getKey() + "/" + pair.getValue();
        if (CollUtil.isEmpty(routeList)) {
            return getMethodProcessors(methodFileName);
        }
        List<MethodBasicProcessor> processors = new ArrayList<>();
        for (String subPath : routeList) {
            List<MethodBasicProcessor> subProcessors = getMethodProcessors("%s/%s/%s".formatted(METHOD_PATH, subPath, pair.getValue()));
            if (CollUtil.isNotEmpty(subProcessors)) {
                processors.addAll(subProcessors);
            }
        }

        sortProcessors(processors);
        return processors;
    }

    public static List<MethodBasicProcessor> getMethodProcessors(String methodFileName) {
        return methodTemplateMap.get(FileUtil.normalize(methodFileName));
    }

    /**
     * 方法部件排序
     */
    private static void sortProcessors(List<MethodBasicProcessor> list) {
        list.sort(Comparator.comparing(MethodBasicProcessor::getPhaseOrdinal)
            .thenComparing(MethodBasicProcessor::getIndex));
    }


}
