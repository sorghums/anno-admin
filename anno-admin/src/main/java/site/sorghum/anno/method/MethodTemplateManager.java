package site.sorghum.anno.method;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
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
import site.sorghum.anno.method.resource.ResourceFinder;
import site.sorghum.anno.method.route.MethodRoute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        parse(packageName, METHOD_PATH + "/*.csv", METHOD_PATH + "/**/*.csv");

        supportOld();

        infoToMtMap();
    }

    private static void supportOld() {

        List<AnnoBaseProxy> proxies = AnnoBeanUtils.getBeansOfType(AnnoBaseProxy.class);
        for (AnnoBaseProxy<?> proxy : proxies) {
            if (ArrayUtil.isEmpty(proxy.supportEntities())) {
                continue;
            }
            String[] supportEntities = proxy.supportEntities();
            Method[] methods = findMethods(proxy.getClass());
            for (Method method : methods) {
                if (method.getName().equals("supportEntities")) {
                    continue;
                }
                for (String supportEntity : supportEntities) {

                    String key = "method/base/" + method.getName() + ".csv";

                    MTProcessorInfo info = new MTProcessorInfo();
                    info.setBeanName(StrUtil.lowerFirst(proxy.getClass().getSimpleName()));
                    info.setBean(proxy);
                    info.setMethodName(method.getName());
                    info.setExclude(false);
                    if (StrUtil.equals(supportEntity, "PrimaryKeyModel")) {
                        info.setCondition("mt.instanceofPrimaryKeyModel(p0)");
                    } else if (StrUtil.equals(supportEntity, "PrimaryKeyModel.BaseMetaModel")) {
                        info.setCondition("mt.instanceofBaseMetaModel(p0)");
                    } else if (StrUtil.equals(supportEntity, "PrimaryKeyModel.BaseMetaModel.BaseOrgMetaModel")) {
                        info.setCondition("mt.instanceofBaseOrgMetaModel(p0)");
                    } else {
                        String[] split = supportEntity.split("\\.");
                        String entityName = split[split.length - 1];
                        key = "method/" + entityName + "/" + method.getName() + ".csv";
                    }
                    info.setFullPath(key);
                    info.setIndex(proxy.index());

                    Set<MTProcessorInfo> mtProcessorInfos = processorInfoMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
                    mtProcessorInfos.add(info);
                }
            }
        }
    }

    private static Method[] findMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        // 过滤掉桥接方法
        List<Method> resultMethods = Arrays.stream(methods).filter(e -> !e.isBridge()).toList();
        return resultMethods.toArray(new Method[0]);
    }


    public static void parse(String packageName, String... locationPatterns) throws IOException {
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName, MethodTemplate.class);
        MultiResource multiResource = new MultiResource();
        for (String locationPattern : locationPatterns) {
            MultiResource resources = ResourceFinder.of().find(locationPattern);
            for (Resource resource : resources) {
                multiResource.add(resource);
            }
        }
        Map<String, List<MTProcessorInfo>> infoMap = new HashMap<>();
        for (Class<?> methodTemplateClass : classes) {
            Method[] publicMethods = ClassUtil.getDeclaredMethods(methodTemplateClass);
            for (Method publicMethod : publicMethods) {
                if (isSupportMethod(publicMethod)) {

                    Map<String, List<MTProcessorInfo>> map = parseMethodTemplates(publicMethod, multiResource);
                    if (CollUtil.isNotEmpty(map)) {
                        infoMap.putAll(map);
                    }
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
                .toList();
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
        String methodFileName = getMethodFileName(publicMethod);
        String methodName = publicMethod.getName();
        Map<String, List<MTProcessorInfo>> infoMap = new HashMap<>();
        for (Resource resource : resources) {
            String file = resource.getName();
            if (!StrUtil.equals(file, methodFileName)) {
                continue;
            }
            if (!(resource instanceof FileResource fileResource)) {
                continue;
            }
            List<String> parentPathList = new ArrayList<>();
            getParentPath(parentPathList, fileResource.getFile());
            // method/test_sayHello.csv
            String fullPath = parentPathList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.joining("/")) + "/" + methodFileName;
            if (methodTemplateMap.containsKey(fullPath)) {
                if (log.isDebugEnabled()) {
                    log.debug("MethodTemplate is parsed, ignore: {}#{}()", publicMethod.getDeclaringClass().getName(), publicMethod.getName());
                }
                continue;
            }
            try (BufferedReader bufferedReader = resource.getReader(StandardCharsets.UTF_8)) {
                CsvReader reader = CsvUtil.getReader();
                List<MethodTemplateCsv> list = reader.read(bufferedReader, MethodTemplateCsv.class);
                if (CollUtil.isEmpty(list)) {
                    continue;
                }
                List<MTProcessorInfo> processorInfos = infoMap.computeIfAbsent(fullPath, k -> new ArrayList<>());
                for (MethodTemplateCsv templateCsv : list) {
                    MTProcessorInfo info = new MTProcessorInfo();
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

    public static String getMethodFileName(Method method) {
        MethodTemplate annotation = getMethodTemplateAnnotation(method);
        String fileNamePrefix = annotation.fileNamePrefix();
        if (StrUtil.isBlank(fileNamePrefix)) {
            return method.getName() + ".csv";
        }
        return fileNamePrefix + "_" + method.getName() + ".csv";
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
        List<String> routeList = Arrays.stream(route).filter(StrUtil::isNotBlank).toList();
        String methodFileName = getMethodFileName(context.getMethod());
        if (routeList.isEmpty()) {
            return getMethodProcessors(METHOD_PATH + "/" + methodFileName);
        }
        List<MethodBasicProcessor> processors = new ArrayList<>();
        for (String subPath : routeList) {
            List<MethodBasicProcessor> subProcessors = getMethodProcessors("%s/%s/%s".formatted(METHOD_PATH, subPath, methodFileName));
            if (CollUtil.isNotEmpty(subProcessors)) {
                processors.addAll(subProcessors);
            }
        }
        processors.sort(Comparator.comparing(e -> e.getProcessorInfo().getIndex()));
        return processors;
    }

    public static List<MethodBasicProcessor> getMethodProcessors(String methodFileName) {
        return methodTemplateMap.get(methodFileName);
    }


}
