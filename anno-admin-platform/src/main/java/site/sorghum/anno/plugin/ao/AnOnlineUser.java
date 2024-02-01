package site.sorghum.anno.plugin.ao;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.plugin.service.AuthService;
import site.sorghum.anno.suppose.model.PrimaryKeyModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(
        name = "在线用户",
        tableName = "an_online_user",
        virtualTable = true,
        annoPermission = @AnnoPermission(
                enable = true,
                baseCode = "an_online_user",
                baseCodeTranslate = "在线用户"
        ),
        canRemove = false
)
public class AnOnlineUser extends PrimaryKeyModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    @AnnoField(
            title = "用户名"
    )
    private String userName;

    /**
     * 用户账户
     */
    @AnnoField(
            title = "账户"
    )
    private String userAccount;

    /**
     * 用户手机
     */
    private String userMobile;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 组织id
     */
    @AnnoField(
            title = "组织",
            dataType = AnnoDataType.TREE,
            treeType = @AnnoTreeType(
                    treeAnno = @AnnoTreeType.TreeAnnoClass(
                            annoClass = AnOrg.class,
                            idKey = "id",
                            labelKey = "orgName",
                            pidKey = "parentOrgId"
                    )
            )
    )
    private String orgId;

    /**
     * ip
     */
    @AnnoField(
            title = "ip"
    )
    private String ip;

    /**
     * 登录时间
     */
    @AnnoField(
            title = "登录时间"
    )
    private Date loginTime;

    /**
     * 浏览器
     */
    @AnnoField(
        title = "浏览器"
    )
    String browser;

    /**
     * 系统
     */
    @AnnoField(
        title = "系统"
    )
    String os;

    /**
     * 设备
     */
    @AnnoField(
        title = "设备"
    )
    String device;

    /**
     * 退出
     */
    @AnnoButton(
        name = "强制退出",
        javaCmd = @AnnoButton.JavaCmd(
            beanClass = AuthService.class,
            methodName = "forceLogout"
        ),
        permissionCode = "forceLogout"
    )
    private String offlineButton;

    public static AnOnlineUser authToOnlineUser(AnnoAuthUser authUser) {
        AnOnlineUser onLineUser = new AnOnlineUser();
        BeanUtil.copyProperties(authUser, onLineUser);
        return onLineUser;
    }

}
