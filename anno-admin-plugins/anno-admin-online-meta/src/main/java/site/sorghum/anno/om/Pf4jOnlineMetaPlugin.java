package site.sorghum.anno.om;


import site.sorghum.anno.om.ao.OnlineClassMeta;
import site.sorghum.anno.om.proxy.OnlineTableProxy;
import site.sorghum.anno.om.supplier.DsNameSupplier;
import site.sorghum.anno.pf4j.Pf4jLoadEntityPlugin;
import site.sorghum.anno.pf4j.Pf4jPluginContext;

import java.util.List;
import java.util.Map;

/**
 * 在线元插件
 *
 * @author Sorghum
 * @since 2024/02/26
 */
public class Pf4jOnlineMetaPlugin extends Pf4jLoadEntityPlugin {

    public Pf4jOnlineMetaPlugin(Pf4jPluginContext context) {
        super(context);
    }


    @Override
    public List<Class<?>> javaClasses() {
        return List.of(
            OnlineClassMeta.class
        );
    }

    @Override
    public Map<String, Object> registerBeans() {
        return Map.of(
            "dsNameSupplier", new DsNameSupplier(),
            "onlineTableProxy", new OnlineTableProxy()
        );
    }
}
