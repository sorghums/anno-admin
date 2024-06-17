package site.sorghum.anno;

import cn.hutool.core.util.ReUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import jakarta.inject.Named;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import site.sorghum.anno._common.cache.AnnoCache;
import site.sorghum.anno._common.util.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Named
public class CaffeineAnnoCache extends AnnoCache {
    Map<String, Integer> defaultExpireTime = new ConcurrentHashMap<>();
    Cache<String, String> caffeine = Caffeine.newBuilder().expireAfter(
        new Expiry<String, String>() {
            @Override
            public long expireAfterCreate(@NonNull String key, @NonNull String v, long currentTime) {
                return TimeUnit.SECONDS.toNanos(defaultExpireTime.getOrDefault(key, 30));
            }

            @Override
            public long expireAfterUpdate(@NonNull String key, @NonNull String v, long currentTime, @NonNegative long currentDuration) {
                return TimeUnit.SECONDS.toNanos(defaultExpireTime.getOrDefault(key, 30));
            }

            @Override
            public long expireAfterRead(@NonNull String object, @NonNull String v, long currentTime, @NonNegative long currentDuration) {
                return currentDuration;
            }
        }
    ).build();

    @Override
    public void putCache(String key, Object value, int seconds) {
        defaultExpireTime.computeIfAbsent(
            key, s -> seconds
        );
        caffeine.put(key, JSONUtil.toJsonString(value));
    }

    @Override
    public <T> T getCacheItem(String key, Class<T> clazz) {
        String json = caffeine.getIfPresent(key);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBean(json, clazz);
    }

    @Override
    public <T> List<T> getCacheList(String key, Class<T> clazz) {
        String json = caffeine.getIfPresent(key);
        if (json == null) {
            return null;
        }
        return JSONUtil.toBeanList(json, clazz);
    }

    @Override
    public boolean containsCache(String key) {
        return caffeine.getIfPresent(key) != null;
    }

    @Override
    public void delKey(String key) {
        caffeine.invalidate(key);
    }

    @Override
    public void delKeyPattern(String key) {
        List<String> keys = new ArrayList<>();
        for (String tempKey : caffeine.asMap().keySet()) {
            if (ReUtil.isMatch(key,tempKey)){
                keys.add(tempKey);
            }
        }
        caffeine.invalidate(keys);
    }

}
