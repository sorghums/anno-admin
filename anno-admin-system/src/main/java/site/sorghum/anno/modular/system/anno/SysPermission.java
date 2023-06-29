package site.sorghum.anno.modular.system.anno;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.base.model.BaseMetaModel;

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
    @AnnoField(title = "权限码", tableFieldName = "code")
    private String code;

    /**
     * 父权限id
     */
    @AnnoField(title = "父权限", tableFieldName = "parent_id",
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(sql = "select id as value,name as label from sys_permission where del_flag = 0 order by id desc"))
    private String parentId;
}