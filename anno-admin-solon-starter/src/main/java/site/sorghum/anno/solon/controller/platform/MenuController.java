package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.controller.MenuBaseController;
import site.sorghum.anno.plugin.entity.response.ReactMenu;
import site.sorghum.anno.plugin.entity.response.AnAnnoMenuResponse;
import site.sorghum.anno.plugin.entity.response.VbenMenu;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = AnnoConstants.BASE_URL + "/system/config")
@Controller
@Condition(onClass = AnnoPlatform.class)
@Api(tags = "菜单控制器")
public class MenuController {
    @Inject
    MenuBaseController menuBaseController;

    @Mapping(value = "/dataMenu")
    public List<AnAnnoMenuResponse> dataMenu() {
        return menuBaseController.dataMenu();
    }

    @Mapping(value = "/anMenu")
    @SaIgnore
    @Get
    @ApiOperation("获取anMenu菜单")
    public AnnoResult<List<ReactMenu>> anMenu() {
        return menuBaseController.anMenu();
    }

    @Mapping(value = "/anButton")
    public AnnoResult<Map> anButton() {
        return menuBaseController.anButton();
    }

    @Mapping(value = "/vbenMenu")
    @ApiOperation("获取vben菜单")
    @SaIgnore
    @Get
    public AnnoResult<List<VbenMenu>> vbenMenu() {
        return menuBaseController.vbenMenu();
    }

}
