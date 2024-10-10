package site.sorghum.anno.delaykit.server;

import cn.hutool.core.lang.Singleton;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.PlainOptions;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class JobRegister {

    private static final String KEY_PREFIX = "delay-kit:job-discovery:";

    private static final String JOB_KEY_PREFIX = "delay-kit:reverse-job-discovery:";

    @Inject
    RedissonClient redissonClient;

    public JobRegister() {

    }


    public void register(String service, Collection<String> jobNames) {
        String key = buildKey(service);
        redissonClient.getBucket(key).expire(Duration.ofSeconds(3 * 60));
        RSet<String> actualSet = getSet(key);
        actualSet.clear();
        actualSet.addAll(jobNames);
        // 反向注册 TODO 一些过期问题仍未解决
        for (String jobName : jobNames) {
            String jobKey = buildJobKey(jobName);
            RSet<String> jobSet = getSetCache(jobKey);
            redissonClient.getBucket(jobKey).expire(Duration.ofSeconds(3 * 60));
            jobSet.add(service);
        }
    }

    public List<String> allServices() {
        Iterable<String> keysByPattern = redissonClient.getKeys().getKeysByPattern(buildKey("*"));
        List<String> result = new ArrayList<>();
        keysByPattern.forEach(it -> {
            it = it.replace(KEY_PREFIX, "");
            result.add(it);
        });
        return result;
    }

    public List<String> allJobNames() {
        Iterable<String> keysByPattern = redissonClient.getKeys().getKeysByPattern(buildJobKey("*"));
        List<String> result = new ArrayList<>();
        keysByPattern.forEach(it -> {
            it = it.replace(JOB_KEY_PREFIX, "");
            result.add(it);
        });
        return result;
    }

    public Set<String> jobNames(String service) {
        return getSet(buildKey(service)).readAll();
    }

    public Set<String> job2Service(String jobName) {
        return getSetCache(buildJobKey(jobName)).readAll();
    }


    private RSet<String> getSet(String key) {
        return Singleton.get(
            key,
            () -> redissonClient.getSet(PlainOptions.name(key).timeout(Duration.ofSeconds(3 * 60)).codec(new StringCodec()))
        );
    }

    private RSetCache<String> getSetCache(String key) {
        return Singleton.get(
            key,
            () -> redissonClient.getSetCache(PlainOptions.name(key).timeout(Duration.ofSeconds(3 * 60)).codec(new StringCodec()))
        );
    }

    private String buildKey(String service) {
        return KEY_PREFIX + service;
    }

    private String buildJobKey(String jobName) {
        return JOB_KEY_PREFIX + jobName;
    }
}
