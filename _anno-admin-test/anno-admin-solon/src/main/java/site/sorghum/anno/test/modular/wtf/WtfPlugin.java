package site.sorghum.anno.test.modular.wtf;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.test.modular.ebusiness.BusinessProduct;
import site.sorghum.anno.test.modular.ebusiness.BusinessProductCat;
import site.sorghum.anno.test.modular.ebusiness.BusinessVirtualTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/23 15:39
 */
@Component
public class WtfPlugin extends AnnoPlugin {

    public WtfPlugin() {
        super("WTF插件", "一个负责测试插件");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("wtf", "WTF管理", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(WtfA.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        list.add(createEntityMenu(WtfB.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));
        list.add(createEntityMenu(WtfC.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));
        list.add(createEntityMenu(WtfABCVirtual.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));
        return list;
    }
}
