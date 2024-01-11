package site.sorghum.anno._common.util;

import org.noear.solon.annotation.Component;
import org.noear.redisx.RedisClient;
import org.noear.redisx.plus.RedisBucket;
import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.List;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/08/02
 */
@Component
@org.springframework.stereotype.Component
public class CacheUtil {
    private static final String BASE_PREFIX = "anno-cache";
    static RedisBucket bucket;

    public static void putCache(String key, Object value) {
        putCache(key, value, 0);
    }

    public static void putCache(String key, Object value, int seconds) {
        key = getCacheKey(key);
        bucket.store(key, JSONUtil.toJsonString(value), seconds);
    }


    public static <T> T getCacheItem(String key, Class<T> clazz) {
        String json = bucket.get(getCacheKey(key));
        if (json == null) {
            return null;
        }
        return JSONUtil.toBean(json, clazz);
    }

    public static <T> List<T> getCacheList(String key, Class<T> clazz) {
        key = getCacheKey(key);
        String json = bucket.get(key);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBeanList(json, clazz);
    }

    public static boolean containsCache(String key) {
        key = getCacheKey(key);
        return bucket.exists(key);
    }

    public static void delKey(String key) {
        key = getCacheKey(key);
        bucket.remove(key);
    }

    public static void removeKey(String key) {
        delKey(key);
    }

    public static String getCacheKey(String key) {
        init();
        return BASE_PREFIX + ":" + key;
    }

    public static String getCacheKey(String key, String tag) {
        return BASE_PREFIX + ":" + key + ":" + tag;
    }


    private static void init() {
        if (bucket == null) {
            bucket = AnnoBeanUtils.getBean(RedisClient.class).getBucket();
        }
    }
}
