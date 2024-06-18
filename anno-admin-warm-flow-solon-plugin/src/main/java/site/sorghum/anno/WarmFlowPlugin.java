package site.sorghum.anno;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.ao.FlowDefinitionAo;
import site.sorghum.anno.ao.FlowHisTaskAo;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/5 12:39
 */
@Component
public class WarmFlowPlugin extends AnnoPlugin {

    public WarmFlowPlugin() {
        super("warm-flow", "warm-flow 的 anno-admin 版本");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("warm-flow", "WarmFlow", "layui-icon layui-icon-diamond", 20));
        list.add(createEntityMenu(FlowDefinitionAo.class, list.get(0).getId(), "layui-icon layui-icon-cart", 200));
        list.add(createEntityMenu(FlowHisTaskAo.class, list.get(0).getId(), "layui-icon layui-icon-note", 210));
        return list;
    }
}
