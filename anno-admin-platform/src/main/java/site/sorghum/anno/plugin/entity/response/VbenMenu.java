package site.sorghum.anno.plugin.entity.response;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 * vben菜单
 *
 * @author Sorghum
 * @since 2023/11/30
 */
@Data
@NoArgsConstructor
public class VbenMenu {

    @ApiModelProperty("菜单id")
    public String id;

    @ApiModelProperty("菜单父id")
    public String parentId;

    @ApiModelProperty("菜单路径")
    public String path;

    @ApiModelProperty("菜单名称")
    public String name;

    @ApiModelProperty("菜单组件")
    public String component;

    @ApiModelProperty("菜单元数据")
    public VbenMeta meta;

    @ApiModelProperty("菜单重定向")
    public String redirect;

    @ApiModelProperty("子菜单")
    List<VbenMenu> children;

    @ApiModelProperty("排序")
    public Integer sort;

    public static VbenMenu toVueMenu(AnAnnoMenu anAnnoMenu) {
        VbenMenu vbenMenu = new VbenMenu();
        vbenMenu.setId(anAnnoMenu.getId());
        vbenMenu.setParentId(anAnnoMenu.getParentId());
        vbenMenu.setMeta(new VbenMeta());
        if (AnAnnoMenu.ParseTypeConstant.ANNO_MAIN.equals(anAnnoMenu.getParseType())) {
            if (StrUtil.isNotBlank(anAnnoMenu.getParseData())) {
                vbenMenu.setName(anAnnoMenu.getParseData());
                vbenMenu.setComponent("/anno/table/index");
                vbenMenu.setPath("/anView/anViewList/" + anAnnoMenu.getParseData());
            }
        } else if (AnAnnoMenu.ParseTypeConstant.IFRAME.equals(anAnnoMenu.getParseType())) {
            if (StrUtil.isNotBlank(anAnnoMenu.getParseData())) {
                vbenMenu.setName(MD5Util.digestHex(anAnnoMenu.getParseData()));
                vbenMenu.setComponent("IFrame");
                vbenMenu.setPath(vbenMenu.getName());
                vbenMenu.getMeta().setFrameSrc(anAnnoMenu.getParseData());
            }
        } else if (AnAnnoMenu.ParseTypeConstant.LINK.equals(anAnnoMenu.getParseType())) {
            if (StrUtil.isNotBlank(anAnnoMenu.getParseData())) {
                vbenMenu.setName(MD5Util.digestHex(anAnnoMenu.getParseData()));
                vbenMenu.setComponent("IFrame");
                vbenMenu.setPath(anAnnoMenu.getParseData());
            }
        }else {
            vbenMenu.setName(MD5Util.digestHex(anAnnoMenu.getTitle()));
            vbenMenu.setComponent("LAYOUT");
            vbenMenu.setPath("/anViewLayout/" + MD5Util.digestHex(anAnnoMenu.getTitle()));
        }
        if (StrUtil.isBlank(vbenMenu.getPath())){
            vbenMenu.setName(MD5Util.digestHex(anAnnoMenu.getTitle()));
            vbenMenu.setComponent("LAYOUT");
            vbenMenu.setPath("/anViewLayout/" + MD5Util.digestHex(anAnnoMenu.getTitle()));
        }
        vbenMenu.getMeta().setTitle(anAnnoMenu.getTitle());
        vbenMenu.getMeta().setIcon(anAnnoMenu.getIcon());
        vbenMenu.setSort(anAnnoMenu.getSort());
        return vbenMenu;
    }

    @Data
    public static class VbenMeta {

        @ApiModelProperty("内嵌参数")
        String frameSrc;

        @ApiModelProperty("菜单标题")
        public String title;

        @ApiModelProperty("菜单图标")
        public String icon;

        @ApiModelProperty("菜单是否固定")
        boolean affix;
    }
}
