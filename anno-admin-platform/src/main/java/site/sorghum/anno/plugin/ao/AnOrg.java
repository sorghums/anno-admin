package site.sorghum.anno.plugin.ao;


import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;

/**
 * 系统组织
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "部门管理",
    tableName = "an_org",
    annoTree = @AnnoTree(label = "orgName", parentKey = "parentOrgId", key = "id", displayAsTree = true),
    annoPermission = @AnnoPermission(enable = true, baseCode = "an_org", baseCodeTranslate = "组织管理"))
public class AnOrg extends BaseMetaModel {

    /**
     * 组织描述
     */
    @AnnoField(title = "组织名字", tableFieldName = "org_name",
        search = @AnnoSearch(),
        edit = @AnnoEdit(editEnable = true, addEnable = true, placeHolder = "请输入部门名字"))
    private String orgName;

    @AnnoField(title = "父组织", tableFieldName = "parent_org_id", search = @AnnoSearch()
        , dataType = AnnoDataType.TREE
        , edit = @AnnoEdit
        , treeType = @AnnoTreeType(treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = AnOrg.class, labelKey = "orgName", pidKey = "parentOrgId", idKey = "id")))
    private String parentOrgId;

    /**
     * 组织描述
     */
    @AnnoField(title = "组织描述", tableFieldName = "org_desc",
        edit = @AnnoEdit, fieldSize = 512)
    private String orgDesc;


    @AnnoButton(name = "组织用户",
        o2mJoinButton = @AnnoButton.O2MJoinButton(targetClass = AnUser.class, thisJavaField = "id", targetJavaField = "orgId", enable = true))
    private Object userButton;
}
