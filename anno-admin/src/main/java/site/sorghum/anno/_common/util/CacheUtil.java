package site.sorghum.anno._common.util;

import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.cache.AnnoCache;

import java.util.List;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/08/02
 */
public class CacheUtil {

    private static final String BASE_PREFIX = "anno-cache";

    private static AnnoCache annoCache;

    public static void putCache(String key, Object value) {
        putCache(key, value, 0);
    }

    public static void putCache(String key, Object value, int seconds) {
        init();
        annoCache.putCache(getCacheKey(key), value, seconds);
    }


    public static <T> T getCacheItem(String key, Class<T> clazz) {
        init();
        return annoCache.getCacheItem(getCacheKey(key), clazz);
    }

    public static <T> List<T> getCacheList(String key, Class<T> clazz) {
        init();
        return annoCache.getCacheList(getCacheKey(key), clazz);
    }

    public static boolean containsCache(String key) {
        init();
        return annoCache.containsCache(getCacheKey(key));
    }

    public static void delKey(String key) {
        init();
        annoCache.delKey(getCacheKey(key));
    }

    public static void delKeyPattern(String key) {
        init();
        annoCache.delKeyPattern(getCacheKey(key));
    }

    public static void removeKey(String key) {
        init();
        delKey(getCacheKey(key));
    }

    private static String getCacheKey(String key) {
        return BASE_PREFIX + ":" + key;
    }

    private static String getCacheKey(String key, String tag) {
        return BASE_PREFIX + ":" + key + ":" + tag;
    }


    private static void init() {
        if (annoCache == null) {
            annoCache = AnnoBeanUtils.getBean(AnnoCache.class);
        }
    }
}
