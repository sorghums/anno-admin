package site.sorghum.anno.modular.anno.util;

import site.sorghum.anno.db.param.TableParam;
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
public class AnnoTableParamCache {
    /**
     * Anno Clazz 缓存
     */
    private static final Map<String, TableParam<?>> STRING_TABLE_PARAM_CACHE = new HashMap<>();


    /**
     * 存入缓存
     *
     * @param key   关键
     * @param tableParam tableParam
     */
    public static synchronized void put(String key,TableParam<?> tableParam){
        STRING_TABLE_PARAM_CACHE.put(key,tableParam);
    }

    /**
     * 获取缓存
     *
     * @param key 关键
     * @return {@link Class}<{@link ?}>
     */
    public static synchronized TableParam<?> get(String key){
        return STRING_TABLE_PARAM_CACHE.get(key);
    }

}
