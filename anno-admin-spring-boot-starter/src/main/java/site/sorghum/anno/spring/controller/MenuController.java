package site.sorghum.anno.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.controller.MenuBaseController;
import site.sorghum.anno.pre.plugin.entity.response.ReactMenu;
import site.sorghum.anno.pre.plugin.entity.response.AnAnnoMenuResponse;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RequestMapping(value = "/system/config")
@RestController
public class MenuController extends MenuBaseController {

    @GetMapping(value = "/dataMenu")
    public List<AnAnnoMenuResponse> dataMenu() {
        return super.dataMenu();
    }

    @GetMapping(value = "/anMenu")
    public AnnoResult<List<ReactMenu>> anMenu() {
        return super.anMenu();
    }

    @GetMapping(value = "/anButton")
    public AnnoResult<Map> anButton() {
        return super.anButton();
    }

}
