package tech.powerjob.server.solon.core.lock;

import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import tech.powerjob.server.solon.moniter.MonitorService;
import tech.powerjob.server.solon.moniter.events.lock.SlowLockEvent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * aspect for @UseSegmentLock
 *
 * @author tjq
 * @since 1/16/21
 */
@Slf4j
public class UseCacheLockAspect implements Interceptor {

    private MonitorService monitorService;

    private AviatorEvaluatorInstance instance;

    public UseCacheLockAspect(AppContext context) {
        context.getBeanAsync(MonitorService.class, bean -> {
            this.monitorService = bean;
        });

        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        instance.useLRUExpressionCache(1000);
        this.instance = instance;

    }

    private final Map<String, Cache<String, ReentrantLock>> lockContainer = Maps.newConcurrentMap();

    private static final long SLOW_THRESHOLD = 100;

    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        UseCacheLock useCacheLock = inv.method().getAnnotation(UseCacheLock.class);
        Cache<String, ReentrantLock> lockCache = lockContainer.computeIfAbsent(useCacheLock.type(), ignore -> {
            int concurrencyLevel = useCacheLock.concurrencyLevel();
            log.info("[UseSegmentLockAspect] create Lock Cache for [{}] with concurrencyLevel: {}", useCacheLock.type(), concurrencyLevel);
            return CacheBuilder.newBuilder()
                .initialCapacity(300000)
                .maximumSize(500000)
                .concurrencyLevel(concurrencyLevel)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();
        });
        final Method method = inv.method().getMethod();
        Map<String, Object> param = inv.argsAsMap();
        Object key = instance.execute(useCacheLock.key(), param, true);
        final ReentrantLock reentrantLock = lockCache.get(key.toString(), ReentrantLock::new);
        long start = System.currentTimeMillis();
        reentrantLock.lockInterruptibly();
        try {
            long timeCost = System.currentTimeMillis() - start;
            if (timeCost > SLOW_THRESHOLD) {

                final SlowLockEvent slowLockEvent = new SlowLockEvent()
                    .setType(SlowLockEvent.Type.LOCAL)
                    .setLockType(useCacheLock.type())
                    .setLockKey(String.valueOf(key))
                    .setCallerService(method.getDeclaringClass().getSimpleName())
                    .setCallerMethod(method.getName())
                    .setCost(timeCost);

                monitorService.monitor(slowLockEvent);

                log.warn("[UseSegmentLockAspect] wait lock for method({}#{}) cost {} ms! key = '{}', args = {}, ", method.getDeclaringClass().getSimpleName(), method.getName(), timeCost,
                    key,
                    JSON.toJSONString(param));
            }
            return inv.invoke();
        } finally {
            reentrantLock.unlock();
        }
    }

}
