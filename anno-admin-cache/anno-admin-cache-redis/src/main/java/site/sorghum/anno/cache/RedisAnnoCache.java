package site.sorghum.anno.cache;

import jakarta.inject.Named;
import org.noear.redisx.RedisClient;
import org.noear.redisx.plus.RedisBucket;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.cache.AnnoCache;
import site.sorghum.anno._common.util.JSONUtil;

import java.util.List;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/08/02
 */
@Named
public class RedisAnnoCache extends AnnoCache {

    private RedisBucket bucket;

    public void putCache(String key, Object value) {
        putCache(key, value, 0);
    }

    public void putCache(String key, Object value, int seconds) {
        init();
        bucket.store(key, JSONUtil.toJsonString(value), seconds);
    }


    public <T> T getCacheItem(String key, Class<T> clazz) {
        init();
        String json = bucket.get(key);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBean(json, clazz);
    }

    public <T> List<T> getCacheList(String key, Class<T> clazz) {
        init();
        String json = bucket.get(key);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBeanList(json, clazz);
    }

    public boolean containsCache(String key) {
        init();
        return bucket.exists(key);
    }

    public void delKey(String key) {
        init();
        bucket.remove(key);
    }

    public void delKeyPattern(String key) {
        init();
        bucket.removeByPattern(key);
    }

    public void removeKey(String key) {
        init();
        delKey(key);
    }

    private void init() {
        if (bucket == null) {
            bucket = AnnoBeanUtils.getBean(RedisClient.class).getBucket();
        }
    }
}
