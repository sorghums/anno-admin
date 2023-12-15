package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.web.bind.annotation.*;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.controller.AuthBaseController;
import site.sorghum.anno.plugin.entity.response.UserInfo;

import java.util.Map;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/auth")
@RestController
@SaIgnore
public class AuthController extends AuthBaseController {

    @PostMapping(value = "/login",  consumes = "application/json")
    public AnnoResult<String> login(@RequestBody Map user) {
        return super.login(user);
    }

    @PostMapping(value = "/logout")
    public AnnoResult<String> logout() {
        return super.logout();
    }

    @SaCheckLogin(type = AnnoStpUtil.TYPE)
    @PostMapping(value = "/clearSysUserCache")
    public AnnoResult<String> clearSysUserCache() {
        return super.clearSysUserCache();
    }

    @GetMapping(value = "/me")
    public AnnoResult<UserInfo> me() {
        return super.me();
    }
}
