package site.sorghum.anno.test.modular.ebusiness;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.modular.plugin.AnPluginMenu;
import site.sorghum.anno.modular.plugin.AnnoPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/23 15:39
 */
@Component
public class BusinessPlugin extends AnnoPlugin {

    public BusinessPlugin() {
        super("商品插件", "一个测试插件");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("business", "商品管理", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(BusinessProduct.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        list.add(createEntityMenu(BusinessProductCat.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));

        return list;
    }
}
