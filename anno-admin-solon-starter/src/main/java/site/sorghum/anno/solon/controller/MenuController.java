package site.sorghum.anno.solon.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno.common.response.AnnoResult;
import site.sorghum.anno.modular.menu.controller.MenuBaseController;
import site.sorghum.anno.modular.menu.entity.response.ReactMenu;
import site.sorghum.anno.modular.menu.entity.response.SysAnnoMenuResponse;

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
public class MenuController extends MenuBaseController {

    @Mapping(value = "/dataMenu")
    public List<SysAnnoMenuResponse> dataMenu() {
        return super.dataMenu();
    }

    @Mapping(value = "/anMenu")
    public AnnoResult<List<ReactMenu>> anMenu() {
        return super.anMenu();
    }

    @Mapping(value = "/anButton")
    public AnnoResult<Map> anButton() {
        return super.anButton();
    }

}