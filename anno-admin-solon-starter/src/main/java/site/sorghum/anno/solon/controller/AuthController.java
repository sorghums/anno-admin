package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.MethodType;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.controller.AuthBaseController;
import site.sorghum.anno.pre.plugin.entity.response.UserInfo;

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
public class AuthController extends AuthBaseController {

    @Mapping(value = "/login", method = MethodType.POST, consumes = "application/json")
    @Post
    public AnnoResult<String> login(@Body Map<String, String> user) {
        return super.login(user);
    }

    @Mapping(value = "/logout", method = MethodType.POST)
    public AnnoResult<String> logout() {
        return super.logout();
    }

    @SaCheckLogin
    @Mapping(value = "/clearSysUserCache", method = MethodType.POST)
    public AnnoResult<String> clearSysUserCache() {
        return super.clearSysUserCache();
    }

    @Mapping(value = "/me", method = MethodType.GET)
    public AnnoResult<UserInfo> me() {
        return super.me();
    }
}
