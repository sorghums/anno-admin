package site.sorghum.anno.modular.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.MethodType;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.modular.system.manager.CaptchaManager;
import site.sorghum.anno.response.AnnoResult;

import java.util.Map;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = "/system/auth")
@Controller
@SaIgnore
public class AuthController {

    @Inject
    AuthService authService;

    @Inject
    CaptchaManager captchaManager;

    @Mapping(value = "/login", method = MethodType.POST,consumes = "application/json")
    public AnnoResult<String> login(@Body Map<String ,String> user) {
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

    @Mapping(value = "/logout", method = MethodType.POST)
    public AnnoResult<String> logout() {
        StpUtil.logout();
        return AnnoResult.succeed("退出成功");
    }

    @SaCheckLogin
    @Mapping(value = "/clearSysUserCache", method = MethodType.POST)
    public AnnoResult<String> clearSysUserCache() {
        String loginId = StpUtil.getLoginId("-1");
        if ("-1".equals(loginId)) {
            return AnnoResult.failure("请先登录");
        }
        // 登录用户
        SysUser sysUser = authService.getUserById(loginId);
        StpUtil.getSession().set("user", sysUser);
        authService.removePermissionCacheList(loginId);
        return AnnoResult.succeed("清除成功");
    }
}
