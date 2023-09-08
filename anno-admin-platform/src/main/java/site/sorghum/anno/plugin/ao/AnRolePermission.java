package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.suppose.model.BaseMetaModel;

import java.io.Serializable;

/**
 * 系统角色权限关联表
 *
 * @author Sorghum
 * @since 2023/06/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "角色权限管理",
        annoPermission = @AnnoPermission(enable = true, baseCode = "an_role_permission", baseCodeTranslate = "角色权限管理"))
@Table("an_role_permission")
@AnnoRemove(removeType = 0)
public class AnRolePermission extends BaseMetaModel implements Serializable {

    /**
     * 角色ID
     */
    @AnnoField(title = "角色ID", tableFieldName = "role_id")
    String roleId;

    /**
     * 角色ID
     */
    @AnnoField(title = "权限id", tableFieldName = "permission_id")
    String permissionId;
}
