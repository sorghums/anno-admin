package site.sorghum.anno.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.controller.MenuBaseController;
import site.sorghum.anno.plugin.entity.response.AnAnnoMenuResponse;
import site.sorghum.anno.plugin.entity.response.NaiveMenu;
import site.sorghum.anno.plugin.entity.response.VbenMenu;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/config")
@RestController
public class MenuController extends MenuBaseController {

    @Override
    @GetMapping(value = "/dataMenu")
    public List<AnAnnoMenuResponse> dataMenu() {
        return super.dataMenu();
    }

    @Override
    @GetMapping(value = "/anButton")
    public AnnoResult<Map> anButton() {
        return super.anButton();
    }

    @Override
    @GetMapping(value = "/vbenMenu")
    public AnnoResult<List<VbenMenu>> vbenMenu() {
        return super.vbenMenu();
    }

    @Override
    @GetMapping(value = "/naiveMenu")
    public AnnoResult<List<NaiveMenu>> naiveMenu() {
        return super.naiveMenu();
    }
}
