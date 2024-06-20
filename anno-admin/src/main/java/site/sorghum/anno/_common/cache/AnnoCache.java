package site.sorghum.anno._common.cache;

import java.util.List;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/08/02
 */
public abstract class AnnoCache {
    public void putCache(String key, Object value) {
        putCache(key, value, 0);
    }

    public abstract void putCache(String key, Object value, int seconds);

    public abstract <T> T getCacheItem(String key, Class<T> clazz);

    public abstract <T> List<T> getCacheList(String key, Class<T> clazz);

    public abstract boolean containsCache(String key);

    public abstract void delKey(String key);

    public abstract void delKeyPattern(String key);

    public void removeKey(String key){
        delKey(key);
    };
}
