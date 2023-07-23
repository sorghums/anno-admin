package site.sorghum.anno.test.modular.ebusiness;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.modular.model.AnMenu;
import site.sorghum.anno.modular.model.AnnoModule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/23 15:39
 */
@Component
public class BusinessModule extends AnnoModule {

    public BusinessModule() {
        super("商品模块", "一个测试模块");
    }

    @Override
    public List<AnMenu> initEntityMenus() {
        List<AnMenu> list  = new ArrayList<>();
        list.add(createRootMenu("business", "商品管理", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(BusinessProduct.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        list.add(createEntityMenu(BusinessProductCat.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));

        return list;
    }
}
