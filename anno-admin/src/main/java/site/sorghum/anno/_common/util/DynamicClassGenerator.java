package site.sorghum.anno._common.util;

import lombok.SneakyThrows;
import org.noear.liquor.DynamicCompiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 动态类加载
 */
public class DynamicClassGenerator {

    private static final DynamicCompiler dynamicCompiler = new DynamicCompiler(ClassLoader.getSystemClassLoader());

    private static final Map<String, String> classNameContentMap = new HashMap<>();

    private static final Map<String, Class<?>> classNameClassMap = new HashMap<>();

    /**
     * 添加类定义
     *
     * @param className 类名
     * @return 类对象
     */
    @SneakyThrows
    public static Class<?> loadClass(String className) {
        if (classNameClassMap.containsKey(className)) {
            return classNameClassMap.get(className);
        }
        dynamicCompiler.addSource(className, classNameContentMap.get(className));
        dynamicCompiler.build();
        Class<?> clazz = dynamicCompiler.getClassLoader().loadClass(className);
        classNameClassMap.put(className, clazz);
        return clazz;
    }

    public static void reloadAll() {
        classNameClassMap.clear();
        dynamicCompiler.reset();
        Set<String> classNames = classNameContentMap.keySet();
        for (String className : classNames) {
            dynamicCompiler.addSource(className, classNameContentMap.get(className));
        }
        dynamicCompiler.compile();
        dynamicCompiler.build();
    }


    public static Class<?> addClass(String className, String classContent) {
        classNameContentMap.put(className, classContent);
        return loadClass(className);
    }
}
