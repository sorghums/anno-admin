package site.sorghum.anno.pre.plugin.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.pre.plugin.ao.SysAnnoMenu;
import site.sorghum.anno.pre.plugin.entity.response.ReactMenu;
import site.sorghum.anno.pre.plugin.entity.response.SysAnnoMenuResponse;
import site.sorghum.anno.pre.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.pre.plugin.service.AuthService;
import site.sorghum.anno.pre.plugin.service.SysAnnoMenuService;

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
public class MenuBaseController {

    @Inject
    SysAnnoMenuService sysAnnoMenuService;

    @Inject
    AuthService authService;

    public List<SysAnnoMenuResponse> dataMenu() {
        String uid = StpUtil.getLoginId().toString();
        List<SysAnnoMenu> sysAnnoMenus = sysAnnoMenuService.list();
        // 过滤需要权限的菜单
        List<SysAnnoMenu> nList = sysAnnoMenus.stream().filter(
            sysAnnoMenu -> {
                if (StrUtil.isNotBlank(sysAnnoMenu.getPermissionId())) {
                    return AuthFunctions.permissionList.apply(uid).contains(sysAnnoMenu.getPermissionId());
                }
                return true;
            }
        ).collect(Collectors.toList());
        return listToTree(list2AnnoMenuResponse(nList));
    }

    public AnnoResult<List<ReactMenu>> anMenu() {
        String uid = StpUtil.getLoginId().toString();
        List<SysAnnoMenu> sysAnnoMenus = sysAnnoMenuService.list();
        // 过滤需要权限的菜单
        List<SysAnnoMenu> nList = sysAnnoMenus.stream().filter(
            sysAnnoMenu -> {
                if (StrUtil.isNotBlank(sysAnnoMenu.getPermissionId())) {
                    return AuthFunctions.permissionList.apply(uid).contains(sysAnnoMenu.getPermissionId());
                }
                return true;
            }
        ).collect(Collectors.toList());
        return AnnoResult.succeed(listToVueTree(list2VueMenuResponse(nList)));
    }


    public AnnoResult<Map> anButton() {
        String temp = """
            {
                "useHooks": {
                    "add": true,
                    "delete": true
                }
            }
            """;
        return AnnoResult.succeed(JSONUtil.toBean(temp, Map.class));
    }


    public static List<SysAnnoMenuResponse> listToTree(List<SysAnnoMenuResponse> list) {
        Map<String, SysAnnoMenuResponse> map = new HashMap<>();
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
                BeanUtil.copyProperties(sysAnnoMenu, annoMenuResponse);
                annoMenuResponse.setChildren(new ArrayList<>());
                return annoMenuResponse;
            }
        ).collect(Collectors.toList());
    }


    public static List<ReactMenu> listToVueTree(List<ReactMenu> list) {
        Map<String, ReactMenu> map = new HashMap<>();
        List<ReactMenu> roots = new ArrayList<>();
        for (ReactMenu node : list) {
            map.put(node.getId(), node);
        }
        for (ReactMenu node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                ReactMenu parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    private static List<ReactMenu> list2VueMenuResponse(List<SysAnnoMenu> sysAnnoMenus) {
        return sysAnnoMenus.stream().map(
            sysAnnoMenu -> {
                ReactMenu reactMenu = ReactMenu.toVueMenu(sysAnnoMenu);
                ;
                reactMenu.setChildren(new ArrayList<>());
                return reactMenu;
            }
        ).collect(Collectors.toList());
    }

    private static boolean isRootNode(Object value) {
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }
}
