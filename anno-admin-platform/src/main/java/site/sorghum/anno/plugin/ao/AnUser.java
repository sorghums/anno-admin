package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.plugin.javacmd.ResetPwdJavaCmdSupplier;
import site.sorghum.anno.suppose.model.BaseOrgMetaModel;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "用户管理",
    tableName = "an_user",
    annoLeftTree = @AnnoLeftTree(leftTreeName = "组织", catKey = "orgId", treeClass = AnOrg.class),
    annoTree = @AnnoTree(label = "name", parentKey = "", key = "id", displayAsTree = false),
    annoPermission = @AnnoPermission(enable = true, baseCode = "an_user", baseCodeTranslate = "用户管理"),
    annoOrder = {@AnnoOrder(orderType = "desc", orderValue = "id")}
)
public class AnUser extends BaseOrgMetaModel implements Serializable {

    /**
     * 用户头像
     */
    @AnnoField(title = "用户头像",
        tableFieldName = "avatar",
        dataType = AnnoDataType.AVATAR,
        edit = @AnnoEdit(placeHolder = "请上传用户头像"),
        imageType = @AnnoImageType(width = 50, height = 50))
    private String avatar;
    /**
     * 手机号
     */
    @AnnoField(title = "手机号", tableFieldName = "mobile", search = @AnnoSearch(),
        edit = @AnnoEdit(placeHolder = "请输入手机号", notNull = true, editEnable = false))
    private String mobile;
    /**
     * 密码
     */
    @AnnoField(title = "密码", tableFieldName = "password",
        edit = @AnnoEdit(editEnable = false), show = false)
    private String password;
    /**
     * 用户名
     */
    @AnnoField(title = "用户名", tableFieldName = "name", search = @AnnoSearch(),
        edit = @AnnoEdit(placeHolder = "请输入用户名", notNull = true))
    private String name;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "状态", tableFieldName = "enable", search = @AnnoSearch(),
        dataType = AnnoDataType.RADIO,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "正常", value = "1"),
            @AnnoOptionType.OptionData(label = "封禁", value = "0")
        }),
        edit = @AnnoEdit(placeHolder = "请选择状态", notNull = true))
    private String enable;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "角色", icon = "ant-design:usergroup-add-outlined", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = AnRole.class,
        mediumTableClass = AnUserRole.class,
        mediumTargetField = "roleId",
        mediumThisField = "userId"
    ))
    private Object roleButton;

    /**
     * 重置密码按钮
     */
    @AnnoButton(permissionCode = "resetPwd", icon = "fluent:key-reset-20-filled", name = "重置密码",
        javaCmd = @AnnoButton.JavaCmd(runSupplier = ResetPwdJavaCmdSupplier.class))
    private Object resetPwdButton;

    @Serial
    private static final long serialVersionUID = 1;
}