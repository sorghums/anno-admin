package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.controller.AuthBaseController;
import site.sorghum.anno.plugin.entity.common.LoginInfo;
import site.sorghum.anno.plugin.entity.request.LoginReq;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;
import site.sorghum.anno.plugin.entity.response.UserInfo;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = AnnoConstants.BASE_URL + "/system/auth")
@Controller
@SaIgnore
@Api(tags = "登录控制器")
@Condition(onClass = AnnoPlatform.class)
public class AuthController {

    @Inject
    AuthBaseController authBaseController;

    @Mapping(value = "/login", method = MethodType.POST, consumes = "application/json")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "登录")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeKey", value = "验证码的key", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
        }
    )
    public AnnoResult<String> login(@Body LoginReq loginReq) {
        return authBaseController.login(loginReq, LoginInfo.builder().ip(Context.current().realIp()).userAgent(Context.current().header("User-Agent")).build());
    }

    @Mapping(value = "/logout", method = MethodType.POST)
    @Post
    @ApiOperation(value = "退出")
    public AnnoResult<String> logout() {
        return authBaseController.logout();
    }

    @SaCheckLogin(type = AnnoStpUtil.TYPE)
    @Mapping(value = "/clearSysUserCache", method = MethodType.POST)
    @Post
    @ApiOperation(value = "清除用户缓存")
    public AnnoResult<String> clearSysUserCache() {
        return authBaseController.clearSysUserCache();
    }

    @Mapping(value = "/me", method = MethodType.POST)
    @ApiOperation("我的信息")
    public AnnoResult<UserInfo> me() {
        return authBaseController.me();
    }

    @Mapping(value = "/updatePwd", method = MethodType.POST)
    @ApiOperation("更新密码")
    public AnnoResult<String> updatePwd(@Body UpdatePwdReq req) {
        req.setUserId(AnnoStpUtil.getLoginIdAsString());
        return authBaseController.updatePwd(req);
    }

}
