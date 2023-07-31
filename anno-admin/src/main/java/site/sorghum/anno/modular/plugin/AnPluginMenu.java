package site.sorghum.anno.modular.plugin;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import site.sorghum.anno.metadata.AnEntity;

/**
 * @author songyinyin
 * @since 2023/7/22 20:09
 */
@Data
public class AnPluginMenu {

    /**
     * 菜单id，父级菜单需要设置该字段
     */
    private String id;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单关联的实体
     */
    private AnEntity entity;

    /**
     * 菜单类型：0 目录，1 页面
     */
    private Integer type;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 父级菜单
     */
    private String parentId;

    public String getId() {
        if (StrUtil.isBlank(id)) {
            return entity.getEntityName();
        }
        return id;
    }

    public String getTitle() {
        if (StrUtil.isBlank(title)) {
            return entity.getTitle();
        }
        return title;
    }
}
