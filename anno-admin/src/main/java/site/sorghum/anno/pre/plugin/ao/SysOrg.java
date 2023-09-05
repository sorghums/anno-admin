package site.sorghum.anno.pre.plugin.ao;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.pre.plugin.proxy.SysOrgProxy;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

/**
 * 系统组织
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "组织管理",
        orgFilter = true,
        annoProxy = @AnnoProxy(value = SysOrgProxy.class),
        annoPermission = @AnnoPermission(enable = true, baseCode = "sys_org", baseCodeTranslate = "组织管理"))
@Table("sys_org")
public class SysOrg extends BaseMetaModel {

    /**
     * 组织描述
     */
    @AnnoField(title = "组织名字", tableFieldName = "org_name",
            search = @AnnoSearch(),
            edit = @AnnoEdit(editEnable = true, addEnable = true, placeHolder = "请输入部门名字"))
    private String orgName;


    /**
     * 组织描述
     */
    @AnnoField(title = "组织描述", tableFieldName = "org_desc",
        edit = @AnnoEdit, fieldSize = 512)
    private String orgDesc;


    @AnnoButton(name = "组织用户",
            o2mJoinButton = @AnnoButton.O2MJoinButton(joinAnnoMainClazz = SysUser.class, joinThisClazzField = "id", joinOtherClazzField = "orgId", enable = true))
    private Object userButton;
}
