package site.sorghum.anno.common.util;

import org.noear.redisx.RedisClient;
import org.noear.redisx.plus.RedisBucket;
import org.noear.redisx.plus.RedisHash;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import java.util.function.Supplier;

/**
 * 缓存工具类
 *
 * @author Sorghum
 * @since 2023/06/28
 */
@Component
public class CacheUtil {

    private static RedisClient redisClient;

    private static RedisBucket bucket;
    private static final String CACHE_PREFIX = "anno:cache";

    @Init
    public void init() {
        Solon.context().getBeanAsync(
            RedisClient.class,
                (bean) -> {
                    redisClient = bean;
                    bucket =  redisClient.getBucket();
                }
        );
    }

    public static <T> T getOrCreate(String sourceName, Supplier<T> tSupplier, Class<T> clazz, Long cacheSeconds, String... args) {
        String mapKey = buildKey(true, sourceName);
        String memberKey = buildKey(false, args);
        String buildKey = buildKey(false, mapKey, memberKey);

        String storeStr;
        if ((storeStr = bucket.get(buildKey)) != null){
            return JSONUtil.toBean(storeStr, clazz);
        }
        T t = tSupplier.get();
        bucket.store(buildKey, JSONUtil.toJsonString(t), cacheSeconds.intValue());
        return t;
    }

    public static void remove(String sourceName, String... args) {
        String mapKey = buildKey(true, sourceName);
        String memberKey = buildKey(false, args);
        String buildKey = buildKey(false, mapKey, memberKey);
        bucket.remove(buildKey);
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
