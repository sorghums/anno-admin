package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.controller.AuthBaseController;
import site.sorghum.anno.plugin.entity.response.UserInfo;

import java.util.Map;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = AnnoConstants.BASE_URL + "/system/auth")
@Controller
@SaIgnore
@Condition(onClass = AnnoPlatform.class)
public class AuthController {

    @Inject
    AuthBaseController authBaseController;

    @Mapping(value = "/login", method = MethodType.POST, consumes = "application/json")
    @Post
    public AnnoResult<String> login(@Body Map<String, String> user) {
        return authBaseController.login(user);
    }

    @Mapping(value = "/logout", method = MethodType.POST)
    public AnnoResult<String> logout() {
        return authBaseController.logout();
    }

    @SaCheckLogin
    @Mapping(value = "/clearSysUserCache", method = MethodType.POST)
    public AnnoResult<String> clearSysUserCache() {
        return authBaseController.clearSysUserCache();
    }

    @Mapping(value = "/me", method = MethodType.GET)
    public AnnoResult<UserInfo> me() {
        return authBaseController.me();
    }
}
