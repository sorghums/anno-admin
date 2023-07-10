package site.sorghum.anno.common.util;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/06/28
 */
@Component
public class CacheUtil {

    private static RedissonClient redissonClient;

    private static final String CACHE_PREFIX = "anno:cache";

    @Init
    public void init() {
        Solon.context().getBeanAsync(
                RedissonClient.class,
                (bean) -> {
                    redissonClient = bean;
                }
        );
    }

    public static <T> T getOrCreate(String sourceName, Supplier<T> tSupplier, Class<T> clazz, Long cacheSeconds, String... args) {
        String mapKey = buildKey(true, sourceName);
        String memberKey = buildKey(false, args);
        RMapCache<String, String> cache = redissonClient.getMapCache(mapKey, new StringCodec());
        if (cache.containsKey(memberKey)) {
            String value = cache.get(memberKey);
            return JSONUtil.parseObject(value, clazz);
        }
        T t = tSupplier.get();
        cache.put(memberKey, JSONUtil.toJSONString(t), cacheSeconds, TimeUnit.SECONDS);
        return t;
    }

    public static void remove(String sourceName, String... args) {
        String mapKey = buildKey(true, sourceName);
        String memberKey = buildKey(false, args);
        RMapCache<String, String> cache = redissonClient.getMapCache(mapKey, new StringCodec());
        cache.remove(memberKey);
    }

    private static String buildKey(boolean prefix, String... args) {
        StringBuilder sb;
        if (prefix) {
            sb = new StringBuilder(CACHE_PREFIX);
        }
        sb = new StringBuilder();
        for (String arg : args) {
            sb.append(":").append(arg);
        }
        return sb.toString();
    }
}
