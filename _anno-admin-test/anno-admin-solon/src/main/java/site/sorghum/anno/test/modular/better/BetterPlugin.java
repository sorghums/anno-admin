package site.sorghum.anno.test.modular.better;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/23 15:39
 */
@Component
public class BetterPlugin extends AnnoPlugin {

    public BetterPlugin() {
        super("Better插件", "一个负责文章插件");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("better", "Better插件", "layui-icon layui-icon-diamond", 10));
        list.add(createEntityMenu(Article.class, list.get(0).getId(), "layui-icon layui-icon-cart", 100));
        list.add(createEntityMenu(Favorites.class, list.get(0).getId(), "layui-icon layui-icon-note", 110));
        return list;
    }
}
