package site.sorghum.anno.spring.db.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import org.noear.wood.annotation.Db;

import java.util.Arrays;
import java.util.Objects;

/**
 * 异步代理注入工具类
 *
 * @author Sorghum
 * @since 2022/09/29
 */
public class InjectUtil {

    public static boolean isInjectClazz(Object bean) {
        if (Objects.isNull(bean)){
            return false;
        }
        Class<?> targetClass = targetClass(bean);
        return Arrays.stream(ReflectUtil.getFields(targetClass)).anyMatch(field -> AnnotationUtil.getAnnotation(field, Db.class) != null);
    }

    public static boolean isInjectClazz(Class<?> clazz) {
        return ReflectUtil.getFields(clazz, field -> AnnotationUtil.getAnnotation(field, Db.class) != null).length != 0;
    }



    public static Class<?> targetClass(Object bean){
        Class<?> clazz = bean.getClass();
        Class<?> targetClass;
        if (isCglibProxyClass(clazz)) {
            targetClass = getUserClass(clazz);
        } else {
            targetClass = clazz;
        }
        return targetClass;
    }


    private static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }


    private static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    private static Class<?> getUserClass(Class<?> clazz) {
        if (clazz.getName().contains("$$")) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                return superclass;
            }
        }
        return clazz;
    }
}
