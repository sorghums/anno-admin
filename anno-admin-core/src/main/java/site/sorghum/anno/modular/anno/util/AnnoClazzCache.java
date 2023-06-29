package site.sorghum.anno.modular.anno.util;

import site.sorghum.anno.exception.BizException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Anno Clazz 缓存
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class AnnoClazzCache {
    /**
     * Anno Clazz 缓存
     */
    private static final Map<String,Class<?>> ANNO_CLAZZ_CACHE = new HashMap<>();

    /**
     * 存入缓存
     *
     * @param key   关键
     * @param clazz clazz
     */
    public static synchronized void put(String key,Class<?> clazz){
        ANNO_CLAZZ_CACHE.put(key,clazz);
    }

    /**
     * 获取缓存
     *
     * @param key 关键
     * @return {@link Class}<{@link ?}>
     */
    public static synchronized Class<?> get(String key){
        return ANNO_CLAZZ_CACHE.get(key);
    }


    public static synchronized Collection<Class<?>> fetchAllClazz(){
        if (ANNO_CLAZZ_CACHE.isEmpty()) {
            throw new BizException("Anno Clazz Cache is Empty");
        }
        return ANNO_CLAZZ_CACHE.values();
    }

}
