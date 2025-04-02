package site.sorghum.anno.om;


import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import site.sorghum.anno.om.ao.OnlineClassMeta;
import site.sorghum.anno.om.ao.OnlineMeta;
import site.sorghum.anno.om.ao.OnlineTable;
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
@Slf4j
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
        list.add(createEntityMenu(OnlineTable.class, list.get(0).getId(), "mdi:jar-16", 140));
        list.add(createEntityMenu("OOMeta", list.get(0).getId(), "mdi:jar-16", 160));
        return list;
    }

    @Override
    public List<String> xmlPath(){
        return List.of("xml/*.xml");
    }
}
