package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/auth")
@RestController
@SaIgnore
public class AuthController extends AuthBaseController {

    @RequestMapping(value = "/login", consumes = "application/json")
    public AnnoResult<String> login(@RequestBody LoginReq loginReq, HttpServletRequest request) {
        return super.login(loginReq, LoginInfo.builder().ip(request.getRemoteAddr()).userAgent(request.getHeader("User-Agent")).build());
    }

    @Override
    @RequestMapping(value = "/logout")
    public AnnoResult<String> logout() {
        return super.logout();
    }

    @Override
    @SaCheckLogin(type = AnnoStpUtil.TYPE)
    @RequestMapping(value = "/clearSysUserCache")
    public AnnoResult<String> clearSysUserCache() {
        return super.clearSysUserCache();
    }

    @PostMapping(value = "/me")
    public AnnoResult<UserInfo> me() {
        return super.me();
    }

    @PostMapping(value = "/updatePwd")
    public AnnoResult<String> updatePwd(@RequestBody UpdatePwdReq req) {
        req.setUserId(AnnoStpUtil.getLoginIdAsString());
        req.setFromAdmin(false);
        return super.updatePwd(req);
    }
}
