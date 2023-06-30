package site.sorghum.anno.modular.system.anno;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.base.model.BaseOrgMetaModel;
import site.sorghum.anno.modular.system.proxy.SysUserProxy;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "用户管理",
        annoPermission = @AnnoPermission(enable = true, baseCode = "sys_user", baseCodeTranslate = "用户管理"),
        annoProxy = @AnnoProxy(value = SysUserProxy.class))
@Table("sys_user")
public class SysUser extends BaseOrgMetaModel implements Serializable {

    /**
     * 用户头像
     */
    @AnnoField(title = "用户头像",
            tableFieldName = "avatar",
            dataType = AnnoDataType.IMAGE,
            edit = @AnnoEdit(placeHolder = "请上传用户头像"),
            imageType = @AnnoImageType(thumbMode = AnnoImageType.ThumbMode.COVER, thumbRatio = AnnoImageType.ThumbRatio.RATE_ONE))
    private String avatar;
    /**
     * 手机号
     */
    @AnnoField(title = "手机号", tableFieldName = "mobile", search = @AnnoSearch(),
            edit = @AnnoEdit(placeHolder = "请输入手机号", notNull = true))
    private String mobile;
    /**
     * 密码
     */
    @AnnoField(title = "密码", tableFieldName = "password",
            edit = @AnnoEdit(placeHolder = "请输入密码", notNull = true, editEnable = false), show = false)
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
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(value = {
                    @AnnoOptionType.OptionData(label = "正常", value = "1"),
                    @AnnoOptionType.OptionData(label = "封禁", value = "0")
            }),
            edit = @AnnoEdit(placeHolder = "请选择状态", notNull = true))
    private Integer enable;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "角色", m2mJoinButton = @AnnoButton.M2MJoinButton(
            joinAnnoMainClazz = SysRole.class,
            mediumTable = "sys_user_role",
            mediumTableClass = SysUserRole.class,
            mediumOtherField = "user_id",
            mediumThisFiled = "role_id",
            joinThisClazzField = "id"
    ))
    private Object roleButton;

    /**
     * 重置密码按钮
     */
    @AnnoButton(name = "重置密码", javaCmd = @AnnoButton.JavaCmd(beanClass = AuthService.class, methodName = "resetPwd"))
    private Object resetPwdButton;

}