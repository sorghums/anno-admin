package site.sorghum.anno._common.util;

import cn.hutool.core.map.SafeConcurrentHashMap;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.db.exception.AnnoDbException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 泛型util
 *
 * @author Sorghum
 * @since 2024/01/30
 */
public class GenericsUtil {
    /**
     * 实体类 -> 泛型实际类型 映射
     */
    private static final Map<String, Class<?>> ENTITY_CLASS_MAP = new SafeConcurrentHashMap<>();

    /**
     * 获取泛型类型
     *
     * @param nowClass       目标类
     * @param interfaceClass 接口类
     * @param index          下标
     * @return {@link Class}<{@link ?}>
     */
    public static Class<?> getInterfaceGenericsType(Class<?> nowClass, Class<?> interfaceClass, int index) {
        String key = getKey(nowClass, interfaceClass, index);
        if (ENTITY_CLASS_MAP.containsKey(key)) {
            return ENTITY_CLASS_MAP.get(key);
        }
        Type[] interfaces = nowClass.getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() instanceof Class<?> clazz) {
                    if (clazz.isAssignableFrom(interfaceClass)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if (index >= actualTypeArguments.length || index < 0) {
                            throw new BizException("UnKnow %s's entity class. actualTypeArguments.length: %s, index: %s".formatted(nowClass, actualTypeArguments.length, index));
                        }
                        if (!(actualTypeArguments[index] instanceof Class)) {
                            throw new BizException("UnKnow %s's entity class. actualTypeArguments[%s] is %s".formatted(nowClass, index, actualTypeArguments[index]));
                        }
                        ENTITY_CLASS_MAP.put(key, (Class<?>) actualTypeArguments[index]);
                    }
                }
            }
        }
        if (!ENTITY_CLASS_MAP.containsKey(key)) {
            throw new AnnoDbException("UnKnow %s's entity class.".formatted(nowClass));
        }
        return ENTITY_CLASS_MAP.get(key);
    }

    private static String getKey(Class<?> targetClass, Class<?> interfaceClass, int index) {
        return targetClass.getName() + "_" + interfaceClass.getName() + "_" + index;
    }
}
