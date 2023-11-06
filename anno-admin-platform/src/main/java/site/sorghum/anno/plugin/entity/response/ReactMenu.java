package site.sorghum.anno.plugin.entity.response;

import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

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

    private String name;

    private String path;

    Map<String,Object> meta;

    List<ReactMenu> children;

    private Map<String, Object> props;
    public static ReactMenu toVueMenu(AnAnnoMenu anAnnoMenu) {
        ReactMenu reactMenu = new ReactMenu();
        reactMenu.setId(anAnnoMenu.getId());
        reactMenu.setParentId(anAnnoMenu.getParentId());
        if (StrUtil.isNotBlank(anAnnoMenu.getParseData())){
            reactMenu.setName("AnViewList");
            reactMenu.setPath("/anView/anViewList/" + anAnnoMenu.getParseData());
        }

        if (StrUtil.isBlank(reactMenu.getPath())){
            reactMenu.setName("AnViewLayout");
            reactMenu.setPath("/anViewLayout/" + MD5Util.digestHex(anAnnoMenu.getTitle()));
        }
        reactMenu.setProps(new HashMap<>());
        reactMenu.setIcon("HomeOutlined");
        reactMenu.setTitle(anAnnoMenu.getTitle());
        reactMenu.setMeta(Map.of("locale",anAnnoMenu.getTitle(),"rootPath",true,"hideInMenu",false));
        return reactMenu;
    }
}
