package site.sorghum.anno.modular.menu.entity.response;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static ReactMenu toVueMenu(SysAnnoMenu sysAnnoMenu) {
        ReactMenu reactMenu = new ReactMenu();
        reactMenu.setId(sysAnnoMenu.getId());
        reactMenu.setParentId(sysAnnoMenu.getParentId());
        if (sysAnnoMenu.getParseData() == null){
            sysAnnoMenu.setParseData(RandomUtil.randomString(5));
        }
        reactMenu.setPath("/amisDemo/index/"+ sysAnnoMenu.getParseData());
        reactMenu.setProps(new HashMap<>());
        reactMenu.setIcon("HomeOutlined");
        reactMenu.setTitle(sysAnnoMenu.getTitle());
        return reactMenu;
    }
}
