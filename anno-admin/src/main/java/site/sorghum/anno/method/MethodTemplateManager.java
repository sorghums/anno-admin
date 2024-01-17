package site.sorghum.anno.method;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
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

    private static final Map<String, List<MTBasicProcessor>> methodTemplateMap = new HashMap<>();
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
        Set<Class<?>> methodRouteClass = ClassUtil.scanPackageBySuper(packageName, MethodRoute.class);
        for (Class<?> routeClass : methodRouteClass) {
            MethodRoute methodRoute = (MethodRoute) AnnoBeanUtils.getBean(routeClass);
            if (methodRoute == null) {
                methodRoute = (MethodRoute) ReflectUtil.newInstance(routeClass);
            }
            methodRouteBeanMap.put(routeClass, methodRoute);
        }
        parse(packageName, METHOD_PATH + "/*.csv");
        parse(packageName, METHOD_PATH + "/**/*.csv");
    }


    public static void parse(String packageName, String locationPattern) throws IOException {
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName, MethodTemplate.class);
        MultiResource resources = ResourceFinder.of().find(locationPattern);
        for (Class<?> methodTemplateClass : classes) {
            Method[] publicMethods = ClassUtil.getDeclaredMethods(methodTemplateClass);
            for (Method publicMethod : publicMethods) {
                if (isSupportMethod(publicMethod)) {
                    Map<String, List<MTBasicProcessor>> listMap = parseMethodTemplates(publicMethod, resources);
                    if (CollUtil.isNotEmpty(listMap)) {
                        methodTemplateMap.putAll(listMap);
                    }
                }
            }
        }
    }

    public static void clear() {
        methodTemplateMap.clear();
        methodRouteBeanMap.clear();
        proxyCache.clear();
    }

    public static boolean isSupportMethod(Method method) {
        return !method.getDeclaringClass().equals(Object.class);
    }

    /**
     * @return pair key = method/test_sayHello.csv, value = List<MTBasicProcessor>
     */
    private static Map<String, List<MTBasicProcessor>> parseMethodTemplates(Method publicMethod, MultiResource resources) throws IOException {
        String methodFileName = getMethodFileName(publicMethod);
        String methodName = publicMethod.getName();
        Map<String, List<MTBasicProcessor>> resultMap = new HashMap<>();
        for (Resource resource : resources) {
            List<MethodTemplateCsv> resultList = new ArrayList<>();
            String file = resource.getName();
            if (!StrUtil.equals(file, methodFileName)) {
                continue;
            }
            if (!(resource instanceof FileResource fileResource)) {
                continue;
            }
            List<String> parentPathList = new ArrayList<>();
            getParentPath(parentPathList, fileResource.getFile());
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
                    return null;
                }
                for (MethodTemplateCsv methodTemplateCsv : list) {
                    String beanName = methodTemplateCsv.getBeanName();
                    // 小写首字母
                    methodTemplateCsv.setBeanName(StrUtil.lowerFirst(beanName));
                }
                List<String> excludeBeanNames = list.stream()
                    .filter(MethodTemplateCsv::isExclude)
                    .map(MethodTemplateCsv::getBeanMethodName)
                    .toList();
                List<MethodTemplateCsv> templateCsvList = list.stream()
                    .filter(e -> StrUtil.isNotBlank(e.getBeanName()) && !excludeBeanNames.contains(e.getBeanMethodName()))
                    .toList();
                if (CollUtil.isNotEmpty(templateCsvList)) {
                    resultList.addAll(templateCsvList);
                }
            }
            List<MethodBasicProcessor> list = resultList.stream()
                .sorted(Comparator.comparing(MethodTemplateCsv::getIndex))
                .map(templateCsv -> {
                    Object bean = AnnoBeanUtils.getBean(templateCsv.getBeanName());
                    if (bean == null) {
                        throw new MTException("Bean: " + templateCsv.getBeanName() + " not found");
                    }
                    if (StrUtil.isBlank(templateCsv.getMethodName())) {
                        if (bean instanceof MTBasicProcessor) {
                            return new MethodBasicProcessor(bean, "process", templateCsv);
                        } else {
                            return new MethodBasicProcessor(bean, methodName, templateCsv);
                        }
                    } else {
                        return new MethodBasicProcessor(bean, templateCsv.getMethodName(), templateCsv);
                    }
                }).toList();
            resultMap.computeIfAbsent(fullPath, k -> new ArrayList<>()).addAll(list);
        }

        return resultMap;
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
            fileNamePrefix = method.getDeclaringClass().getSimpleName();
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

    public static List<MTBasicProcessor> getMethodProcessors(MTContext context) {
        MethodTemplate methodTemplate = getMethodTemplateAnnotation(context.getMethod());
        Class<? extends MethodRoute> routeClass = methodTemplate.route();
        MethodRoute methodRoute = methodRouteBeanMap.get(routeClass);
        String[] route = methodRoute.route(context);
        List<String> routeList = Arrays.stream(route).filter(StrUtil::isNotBlank).toList();
        String methodFileName = getMethodFileName(context.getMethod());
        if (routeList.isEmpty()) {
            return getMethodProcessors(METHOD_PATH + "/" + methodFileName);
        }
        List<MTBasicProcessor> processors = new ArrayList<>();
        for (String subPath : routeList) {
            List<MTBasicProcessor> subProcessors = getMethodProcessors("%s/%s/%s".formatted(METHOD_PATH, subPath, methodFileName));
            if (CollUtil.isNotEmpty(subProcessors)) {
                processors.addAll(subProcessors);
            }
        }
        processors.sort(Comparator.comparing(e -> e.getMethodTemplateCsv().getIndex()));
        return processors;
    }

    public static List<MTBasicProcessor> getMethodProcessors(String methodFileName) {
        return methodTemplateMap.get(methodFileName);
    }


}
