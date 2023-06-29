package site.sorghum.anno.modular.auth.controller;

import cn.dev33.satoken.annotation.SaIgnore;
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
        String mobile = user.get("mobile");
        String password = user.get("password");
        String codeKey = user.get("codeKey");
        String code = user.get("code");
        captchaManager.verifyCaptcha(codeKey, code);
        if (mobile == null || password == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }
        SysUser sysUser = authService.verifyLogin(mobile, password);
        StpUtil.login(sysUser.getId());
        return AnnoResult.succeed(StpUtil.getTokenValue());
    }

    @Mapping(value = "/logout", method = MethodType.POST)
    public AnnoResult<String> logout() {
        StpUtil.logout();
        return AnnoResult.succeed("退出成功");
    }
}
