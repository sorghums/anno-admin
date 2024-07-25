package tech.powerjob.server.solon;

import org.pf4j.Extension;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;
import tech.powerjob.server.solon.persistence.remote.model.AppInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/5 12:39
 */
@Extension
public class PowerjobPlugin extends AnnoPlugin {

    public PowerjobPlugin() {
        super("powerjob-server-solon", "powerjob-server 的 solon 版本，分布式调度中心");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("powerjob-server-solon", "定时任务", "layui-icon layui-icon-diamond", 20));
        list.add(createEntityMenu(AppInfoDO.class, list.get(0).getId(), "layui-icon layui-icon-cart", 200));
        list.add(createEntityMenu(JobInfoDO.class, list.get(0).getId(), "layui-icon layui-icon-note", 210));
        list.add(createEntityMenu(InstanceInfoDO.class, list.get(0).getId(), "layui-icon layui-icon-note", 220));
        return list;
    }
}
