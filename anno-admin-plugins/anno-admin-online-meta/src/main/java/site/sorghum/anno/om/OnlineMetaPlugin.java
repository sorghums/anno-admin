package site.sorghum.anno.om;


import org.pf4j.Extension;
import site.sorghum.anno.om.ao.OnlineClassMeta;
import site.sorghum.anno.om.ao.OnlineMeta;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线元插件
 *
 * @author Sorghum
 * @since 2024/02/26
 */
@Extension
public class OnlineMetaPlugin extends AnnoPlugin {

    public OnlineMetaPlugin() {
        super("Online元数据插件", "在线管理元数据");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("online_meta", "在线元数据", "wpf:online", 10));
        list.add(createEntityMenu(OnlineClassMeta.class, list.get(0).getId(), "ri:java-line", 100));
        list.add(createEntityMenu(OnlineMeta.class, list.get(0).getId(), "fluent:document-yml-16-filled", 120));
        return list;
    }
}
