package site.sorghum.anno.modular.menu.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.menu.entity.model.AnnoMenu;
import site.sorghum.anno.modular.menu.entity.response.AnnoMenuResponse;

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
    AnnoService annoService;

    @Inject
    AuthService authService;

    @Mapping(value = "/dataMenu")
    public List<AnnoMenuResponse> dataMenu() {
        String uid = StpUtil.getLoginId().toString();
        QueryRequest<AnnoMenu> annoMenuQueryRequest = new QueryRequest<AnnoMenu>(){{
            setClazz(AnnoMenu.class);
            setOrderBy("sort");
        }};
        List<AnnoMenu> list = annoService.list(annoMenuQueryRequest);
        // 过滤需要权限的菜单
        List<AnnoMenu> nList = list.stream().filter(
                annoMenu -> {
                    if (StrUtil.isNotBlank(annoMenu.getPermissionId())) {
                        return authService.permissionList(uid).contains(annoMenu.getPermissionId());
                    }
                    return true;
                }
        ).collect(Collectors.toList());
        return listToTree(list2AnnoMenuResponse(nList));
    }

    public static List<AnnoMenuResponse> listToTree(List<AnnoMenuResponse> list) {
        Map<String ,AnnoMenuResponse> map = new HashMap<>();
        List<AnnoMenuResponse> roots = new ArrayList<>();
        for (AnnoMenuResponse node : list) {
            map.put(node.getId(), node);
        }
        for (AnnoMenuResponse node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                AnnoMenuResponse parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }
    private static List<AnnoMenuResponse> list2AnnoMenuResponse(List<AnnoMenu> annoMenus) {
        return annoMenus.stream().map(
                annoMenu -> {
                    AnnoMenuResponse annoMenuResponse = new AnnoMenuResponse();
                    BeanUtil.copyProperties(annoMenu,annoMenuResponse);
                    annoMenuResponse.setChildren(new ArrayList<>());
                    return annoMenuResponse;
                }
        ).collect(Collectors.toList());
    }



    private static boolean isRootNode(Object value){
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }
}
