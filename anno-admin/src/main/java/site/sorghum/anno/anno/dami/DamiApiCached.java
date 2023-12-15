package site.sorghum.anno.anno.dami;

import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * createSender 时，增加缓存
 *
 * @author songyinyin
 * @since 2023/10/4 17:56
 */
public class DamiApiCached extends DamiApiImpl {

    private final Map<String, Object> proxyCache = new ConcurrentHashMap<>();

    public DamiApiCached(Supplier<DamiBus> busSupplier) {
        super(busSupplier);
    }

    @Override
    public <T> T createSender(String topicMapping, Class<T> senderClz) {
        return (T) proxyCache.getOrDefault(topicMapping + "##" + senderClz.getName(),
            super.createSender(topicMapping, senderClz));
    }

    @Override
    protected Method[] findMethods(Class<?> listenerClz) {
        Method[] methods = listenerClz.getDeclaredMethods();
        // 过滤掉桥接方法
        List<Method> resultMethods = Arrays.stream(methods).filter(e -> !e.isBridge()).toList();
        return resultMethods.toArray(new Method[0]);
    }
}
