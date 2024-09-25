package site.sorghum.anno.plugin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.entity.response.AnAnnoMenuResponse;
import site.sorghum.anno.plugin.entity.response.ReactMenu;
import site.sorghum.anno.plugin.entity.response.VbenMenu;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.plugin.service.AnnoMenuService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Named
public class MenuBaseController {

    @Inject
    AnnoMenuService annoMenuService;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    AnnoProperty annoProperty;

    public List<AnAnnoMenuResponse> dataMenu() {
        List<AnAnnoMenu> nList = getAnAnnoMenus();
        return listToTree(list2AnnoMenuResponse(nList));
    }

    public AnnoResult<List<ReactMenu>> anMenu() {
        List<AnAnnoMenu> nList = getAnAnnoMenus();
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


    public static List<AnAnnoMenuResponse> listToTree(List<AnAnnoMenuResponse> list) {
        Map<String, AnAnnoMenuResponse> map = new HashMap<>();
        List<AnAnnoMenuResponse> roots = new ArrayList<>();
        for (AnAnnoMenuResponse node : list) {
            map.put(node.getId(), node);
        }
        for (AnAnnoMenuResponse node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                AnAnnoMenuResponse parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    private static List<AnAnnoMenuResponse> list2AnnoMenuResponse(List<AnAnnoMenu> anAnnoMenus) {
        return anAnnoMenus.stream().map(
            sysAnnoMenu -> {
                AnAnnoMenuResponse annoMenuResponse = new AnAnnoMenuResponse();
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
        // 加入默认工作台节点
        roots.add(0, new ReactMenu() {{
            setPath("/dashboard/workplace");
            setName("Workplace");
            setMeta(
                Map.of(
                    "locale", "工作台"
                )
            );
        }});
        return roots;
    }

    private static List<ReactMenu> list2VueMenuResponse(List<AnAnnoMenu> anAnnoMenus) {
        return anAnnoMenus.stream().map(
            sysAnnoMenu -> {
                ReactMenu reactMenu = ReactMenu.toVueMenu(sysAnnoMenu);
                ;
                reactMenu.setChildren(new ArrayList<>());
                return reactMenu;
            }
        ).collect(Collectors.toList());
    }

    public static List<VbenMenu> listToVbenVueTree(List<VbenMenu> list) {
        Map<String, VbenMenu> map = new HashMap<>();
        List<VbenMenu> roots = new ArrayList<>();
        for (VbenMenu node : list) {
            map.put(node.getId(), node);
        }
        for (VbenMenu node : list) {
            if (node.getSort() == null){
                node.setSort(0);
            }
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                VbenMenu parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        roots.sort(Comparator.comparing(VbenMenu::getSort).reversed());
        // 加入默认工作台节点
        VbenMenu vbenMenu = new VbenMenu();
        vbenMenu.setPath("/dashboard");
        vbenMenu.setName("Analysis");
        vbenMenu.setComponent("/dashboard/analysis/index");
        vbenMenu.setMeta(new VbenMenu.VbenMeta());
        vbenMenu.getMeta().setTitle("工作台");
        vbenMenu.getMeta().setIcon("bx:bx-home");
        roots.add(0, vbenMenu);

        // 如果component="LAYOUT" 且children为空,则不显示
        roots.removeIf(node -> "LAYOUT".equals(node.getComponent()) && node.getChildren().isEmpty());
        return roots;
    }

    private static List<VbenMenu> list2VbenVueMenuResponse(List<AnAnnoMenu> anAnnoMenus) {
        return anAnnoMenus.stream().map(
            sysAnnoMenu -> {
                VbenMenu vbenMenu = VbenMenu.toVbenMenu(sysAnnoMenu);
                vbenMenu.setChildren(new ArrayList<>());
                return vbenMenu;
            }
        ).collect(Collectors.toList());
    }

    private static boolean isRootNode(Object value) {
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }

    public AnnoResult<List<VbenMenu>> vbenMenu() {
        List<AnAnnoMenu> nList = getAnAnnoMenus();
        return AnnoResult.succeed(listToVbenVueTree(list2VbenVueMenuResponse(nList)));
    }

    private List<AnAnnoMenu> getAnAnnoMenus() {
        // 登录校验
        permissionProxy.checkLogin();
        List<AnAnnoMenu> anAnnoMenus = annoMenuService.list();
        String uid;
        // 登录校验通过 但是anno系统未登录
        if (AnnoStpUtil.isLogin()){
            uid = AnnoStpUtil.getLoginIdAsString();
        } else {
            uid = null;
        }
        String tokenValue = AnnoStpUtil.getTokenValue();
        String apiServerUrl = annoProperty.getApiServerUrl();
        // 过滤需要权限的菜单
        List<AnAnnoMenu> nList = anAnnoMenus.stream().filter(
            sysAnnoMenu -> {
                if (StrUtil.isNotBlank(sysAnnoMenu.getPermissionId())) {
                    // uid为空返回全部
                    if (uid == null){
                        return true;
                    }
                    return AuthFunctions.permissionList.apply(uid).contains(sysAnnoMenu.getPermissionId());
                }
                return true;
            }
        ).collect(Collectors.toList());
        // 默认值设置
        for (AnAnnoMenu anAnnoMenu : nList) {
            if (StrUtil.isNotBlank(anAnnoMenu.getParseData())) {
                String temp = anAnnoMenu.getParseData().replace("[[[token]]]", tokenValue).replace("[[[apiServerUrl]]]", apiServerUrl);
                anAnnoMenu.setParseData(temp);
            }
        }
        // TPL模板配置
        for (AnAnnoMenu anAnnoMenu : nList) {
            if (StrUtil.isNotBlank(anAnnoMenu.getParseType()) && AnAnnoMenu.ParseTypeConstant.TPL.equals(anAnnoMenu.getParseType())) {
                anAnnoMenu.setParseData(
                    jointPath(apiServerUrl, AnnoConstants.BASE_URL, "annoTpl?_tplId=%s&_tokenValue=%s".formatted(anAnnoMenu.getParseData(), tokenValue))
                );
            }
        }
        return nList;
    }

    private String jointPath(Object... tempPath) {
        //1.去除空格 2.去除重复的/
        for (int i = 0; i < tempPath.length; i++) {
            String var = tempPath[i].toString();
            if (var.startsWith("/")){
                var = var.substring(1);
            }
            if (var.endsWith("/")){
                var = var.substring(0, var.length() - 1);
            }
            tempPath[i] = var.trim();
        }
        return StrUtil.join("/", tempPath);
    }
}
