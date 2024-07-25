package site.sorghum.anno.plugin;


import org.pf4j.Extension;
import site.sorghum.anno.plugin.ao.OnlineMeta;

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
        list.add(createRootMenu("online_meta", "在线元数据", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(OnlineMeta.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        return list;
    }
}
