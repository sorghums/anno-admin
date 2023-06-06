package site.sorghum.anno.modular.system.anno;


import cn.dev33.satoken.annotation.SaCheckPermission;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.system.base.BaseMetaModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统组织
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "组织机构", tableName = "sys_org", annoPermission = @AnnoPermission(enable = true, baseCode = "sys_org", baseCodeTranslate = "系统组织"))
public class SysOrg extends BaseMetaModel {

    /**
     * 部门名字
     */
    @AnnoField(title = "部门名字", tableFieldName = "org_name",
            search = @AnnoSearch(),
            edit = @AnnoEdit(editEnable = true, addEnable = true,placeHolder = "请输入部门名字"))
    private String orgName;
}
