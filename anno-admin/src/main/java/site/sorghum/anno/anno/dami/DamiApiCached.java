package site.sorghum.anno.anno.dami;

import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;

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

}
