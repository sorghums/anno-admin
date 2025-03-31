package site.sorghum.anno.om;


import site.sorghum.anno.om.ao.OnlineMeta;
import site.sorghum.anno.om.javacmd.ExportJarCmd;
import site.sorghum.anno.om.javacmd.PreviewMetaCmd;
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
    public List<String> ymlContents() {
        return List.of();
    }

    @Override
    public List<Class<?>> javaClasses() {
        return List.of(
            OnlineMeta.class
        );
    }

    @Override
    public Map<String, Object> registerBeans() {
        return Map.of(
            "exportJarCmd",new ExportJarCmd(),
            "previewMetaCmd",new PreviewMetaCmd(),
            "dsNameSupplier", new DsNameSupplier(),
            "onlineTableProxy", new OnlineTableProxy()
        );
    }
}
