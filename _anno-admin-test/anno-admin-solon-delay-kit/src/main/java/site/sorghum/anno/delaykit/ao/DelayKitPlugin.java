package site.sorghum.anno.delaykit.ao;

import org.pf4j.Extension;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.util.ArrayList;
import java.util.List;

@Extension
public class DelayKitPlugin extends AnnoPlugin {

    public DelayKitPlugin() {
        super("延迟分布式调度框架", "延迟一下!");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("delay_kit", "延迟调度", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(RemoteJobAo.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        list.add(createEntityMenu(JobHistoryAo.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        return list;
    }
}
