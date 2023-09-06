package site.sorghum.anno.pre.plugin.entity.response;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.pre.plugin.ao.AnAnnoMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * vue菜单
 *
 * @author sorghum
 * @date 2023/07/23
 */
@NoArgsConstructor
@Data
public class ReactMenu {

    transient private String id;

    transient private String parentId;

    private String icon;

    private String title;

    private String path;

    List<ReactMenu> children;

    private Map<String, Object> props;
    public static ReactMenu toVueMenu(AnAnnoMenu anAnnoMenu) {
        ReactMenu reactMenu = new ReactMenu();
        reactMenu.setId(anAnnoMenu.getId());
        reactMenu.setParentId(anAnnoMenu.getParentId());
        if (anAnnoMenu.getParseData() == null){
            anAnnoMenu.setParseData(RandomUtil.randomString(5));
        }
        reactMenu.setPath("/amisDemo/index/" + anAnnoMenu.getParseData());
        reactMenu.setProps(new HashMap<>());
        reactMenu.setIcon("HomeOutlined");
        reactMenu.setTitle(anAnnoMenu.getTitle());
        return reactMenu;
    }
}
