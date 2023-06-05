package site.sorghum.anno.modular.system.anno;

import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.system.base.BaseMetaModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统权限
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "权限信息",
        tableName = "sys_permission",
        annoTree = @AnnoTree(label = "name", parentKey = "parentId", key = "id", displayAsTree = true))
public class SysPermission extends BaseMetaModel implements Serializable {

    /**
    * 权限名称
    */
    @AnnoField(title = "权限名称", tableFieldName = "name")
    private String name;

    /**
     * 权限Code
     */
    @AnnoField(title = "权限Code", tableFieldName = "code")
    private String code;

    /**
    * 父权限id
    */
    @AnnoField(title = "父权限id", tableFieldName = "parent_id")
    private String parentId;
}