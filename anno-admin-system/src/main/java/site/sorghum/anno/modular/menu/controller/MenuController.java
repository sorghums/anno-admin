package site.sorghum.anno.modular.menu.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.menu.entity.model.AnnoMenu;
import site.sorghum.anno.modular.menu.entity.response.AnnoMenuResponse;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

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
@SaIgnore
public class MenuController {

    @Inject
    AnnoService annoService;

    @Mapping(value = "/dataMenu")
    public List<AnnoMenuResponse> dataMenu() {
        QueryRequest<AnnoMenu> annoMenuQueryRequest = new QueryRequest<AnnoMenu>(){{
            setClazz(AnnoMenu.class);
            setOrderBy("sort");
        }};
        List<AnnoMenu> list = annoService.list(annoMenuQueryRequest);
        return listToTree(list2AnnoMenuResponse(list));
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
