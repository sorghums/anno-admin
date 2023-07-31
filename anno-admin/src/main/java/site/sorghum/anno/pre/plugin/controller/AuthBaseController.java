package site.sorghum.anno.pre.plugin.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.entity.response.UserInfo;
import site.sorghum.anno.pre.plugin.service.AuthService;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.plugin.manager.CaptchaManager;

import java.util.Map;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
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
        SysUser sysUser = authService.verifyLogin(mobile, password);
        // 登录
        StpUtil.login(sysUser.getId());
        SaSession session = StpUtil.getSession(true);
        session.set("user", sysUser);
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
        SysUser sysUser = authService.getUserById(loginId);
        sysUser.setPassword(null);
        StpUtil.getSession().set("user", sysUser);
        authService.removePermRoleCacheList(loginId);
        return AnnoResult.succeed("清除成功");
    }

    public AnnoResult<UserInfo> me() {
        UserInfo userInfo = new UserInfo();
        SysUser sysUser = (SysUser) StpUtil.getSession().get("user");
        userInfo.setName(sysUser.getName());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setPerms(StpUtil.getPermissionList());
        userInfo.setRoles(StpUtil.getRoleList());
        return AnnoResult.succeed(userInfo);
    }
}
