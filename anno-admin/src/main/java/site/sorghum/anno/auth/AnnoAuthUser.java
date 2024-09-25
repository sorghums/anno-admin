package site.sorghum.anno.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Anno验证用户
 *
 * @author Sorghum
 * @since 2023/12/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnoAuthUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    String userId;

    /**
     * 用户名
     */
    String userName;

    /**
     * 用户帐户
     */
    String userAccount;

    /**
     * 头像
     */
    String avatar;

    /**
     * 用户手机
     */
    String userMobile;

    /**
     * 用户状态
     */
    String userStatus;

    /**
     * 组织id
     */
    String orgId;

    /**
     * ip
     */
    String ip;

    /**
     * 登录时间
     */
    Date loginTime;

    /**
     * 浏览器
     */
    String browser;

    /**
     * 系统
     */
    String os;

    /**
     * 设备
     */
    String device;

    /**
     * 首页菜单
     */
    String homePath;
}
