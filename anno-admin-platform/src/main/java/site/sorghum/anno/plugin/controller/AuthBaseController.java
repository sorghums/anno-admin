package site.sorghum.anno.plugin.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jdk.jfr.Name;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.plugin.manager.CaptchaManager;
import site.sorghum.anno.plugin.service.AuthService;
import site.sorghum.anno.plugin.entity.response.UserInfo;

import java.util.Map;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Named
public class AuthBaseController {

    @Inject
    AuthService authService;

    @Inject
    CaptchaManager captchaManager;

    public AnnoResult<String> login(Map<String, String> user) {
        // 获得系列参数
        String mobile = user.get("mobile");
        String password = user.get("password");
        String codeKey = user.get("codeKey");
        String code = user.get("code");
        // 校验验证码
        captchaManager.verifyCaptcha(codeKey, code);
        if (mobile == null || password == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }
        // 校验用户名密码
        AnUser anUser = AuthFunctions.verifyLogin.apply(user);
        // 登录
        StpUtil.login(anUser.getId());
        SaSession session = StpUtil.getSession(true);
        session.set("user", anUser);
        return AnnoResult.succeed(StpUtil.getTokenValue());
    }

    public AnnoResult<String> logout() {
        StpUtil.logout();
        return AnnoResult.succeed("退出成功");
    }

    public AnnoResult<String> clearSysUserCache() {
        String loginId = StpUtil.getLoginId("-1");
        if ("-1".equals(loginId)) {
            return AnnoResult.failure("请先登录");
        }
        // 登录用户
        AnUser anUser = AuthFunctions.getUserById.apply(loginId);
        anUser.setPassword(null);
        StpUtil.getSession().set("user", anUser);
        AuthFunctions.removePermRoleCacheList.accept(loginId);
        return AnnoResult.succeed("清除成功");
    }

    public AnnoResult<UserInfo> me() {
        UserInfo userInfo = new UserInfo();
        AnUser anUser = (AnUser) StpUtil.getSession().get("user");
        userInfo.setName(anUser.getName());
        userInfo.setAvatar(anUser.getAvatar());
        userInfo.setPerms(StpUtil.getPermissionList());
        userInfo.setRoles(StpUtil.getRoleList());
        return AnnoResult.succeed(userInfo);
    }
}
