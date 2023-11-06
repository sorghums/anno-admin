package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.controller.MenuBaseController;
import site.sorghum.anno.plugin.entity.response.ReactMenu;
import site.sorghum.anno.plugin.entity.response.AnAnnoMenuResponse;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = "/system/config")
@Controller
@Condition(onClass = AnnoPlatform.class)
public class MenuController {
    @Inject
    MenuBaseController menuBaseController;

    @Mapping(value = "/dataMenu")
    public List<AnAnnoMenuResponse> dataMenu() {
        return menuBaseController.dataMenu();
    }

    @Mapping(value = "/anMenu")
    @SaIgnore
    public AnnoResult<List<ReactMenu>> anMenu() {
        return menuBaseController.anMenu();
    }

    @Mapping(value = "/anButton")
    public AnnoResult<Map> anButton() {
        return menuBaseController.anButton();
    }

}
