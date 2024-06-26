package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.db.BaseMetaModel;

import java.io.Serializable;

/**
 * 系统角色用户关联表
 *
 * @author Sorghum
 * @since 2023/06/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "角色用户关联管理",
    tableName = "an_user_role",
    annoPermission = @AnnoPermission(enable = true, baseCode = "an_user_role", baseCodeTranslate = "角色用户关联管理"))
@AnnoRemove(removeType = 0)
public class AnUserRole extends BaseMetaModel implements Serializable {

    /**
     * 用户ID
     */
    @AnnoField(title = "用户ID", tableFieldName = "user_id")
    String userId;

    /**
     * 角色ID
     */
    @AnnoField(title = "角色ID", tableFieldName = "role_id")
    String roleId;
}
