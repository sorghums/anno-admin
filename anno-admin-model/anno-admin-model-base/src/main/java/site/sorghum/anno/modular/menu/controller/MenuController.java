package site.sorghum.anno.modular.menu.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;
import site.sorghum.anno.modular.menu.entity.response.SysAnnoMenuResponse;
import site.sorghum.anno.modular.system.service.SysAnnoMenuService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = "/system/config")
@Controller
public class MenuController {

    @Inject
    SysAnnoMenuService sysAnnoMenuService;

    @Inject
    AuthService authService;

    @Mapping(value = "/dataMenu")
    public List<SysAnnoMenuResponse> dataMenu() {
        String uid = StpUtil.getLoginId().toString();
        List<SysAnnoMenu> sysAnnoMenus = sysAnnoMenuService.list();
        // 过滤需要权限的菜单
        List<SysAnnoMenu> nList = sysAnnoMenus.stream().filter(
                sysAnnoMenu -> {
                    if (StrUtil.isNotBlank(sysAnnoMenu.getPermissionId())) {
                        return authService.permissionList(uid).contains(sysAnnoMenu.getPermissionId());
                    }
                    return true;
                }
        ).collect(Collectors.toList());
        return listToTree(list2AnnoMenuResponse(nList));
    }

    public static List<SysAnnoMenuResponse> listToTree(List<SysAnnoMenuResponse> list) {
        Map<String , SysAnnoMenuResponse> map = new HashMap<>();
        List<SysAnnoMenuResponse> roots = new ArrayList<>();
        for (SysAnnoMenuResponse node : list) {
            map.put(node.getId(), node);
        }
        for (SysAnnoMenuResponse node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                SysAnnoMenuResponse parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }
    private static List<SysAnnoMenuResponse> list2AnnoMenuResponse(List<SysAnnoMenu> sysAnnoMenus) {
        return sysAnnoMenus.stream().map(
                sysAnnoMenu -> {
                    SysAnnoMenuResponse annoMenuResponse = new SysAnnoMenuResponse();
                    BeanUtil.copyProperties(sysAnnoMenu,annoMenuResponse);
                    annoMenuResponse.setChildren(new ArrayList<>());
                    return annoMenuResponse;
                }
        ).collect(Collectors.toList());
    }



    private static boolean isRootNode(Object value){
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }
}
