package site.sorghum.anno.plugin.controller;

import cn.dev33.satoken.session.SaSession;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.response.UserInfo;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.plugin.manager.CaptchaManager;
import site.sorghum.anno.plugin.service.AuthService;

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
        String username = user.get("username");
        String password = user.get("password");
        String codeKey = user.get("codeKey");
        String code = user.get("code");
        // 校验验证码
        captchaManager.verifyCaptcha(codeKey, code);
        if (username == null || password == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }
        // 校验用户名密码
        AnUser anUser = AuthFunctions.verifyLogin.apply(user);
        if ("0".equals(anUser.getEnable())){
            throw new BizException("账号已被禁用");
        }
        // 登录
        AnnoStpUtil.login(anUser.getId());
        AnnoStpUtil.setAuthUser(
            anUser.getId(),
            AnnoAuthUser.builder().
                userId(anUser.getId()).
                userName(anUser.getName()).
                userAccount(anUser.getMobile()).
                userMobile(anUser.getMobile()).
                userStatus(anUser.getEnable()).
                orgId(anUser.getOrgId()).
                build()
        );

        SaSession session = AnnoStpUtil.getSession(true);
        session.set("user", anUser);
        return AnnoResult.succeed(AnnoStpUtil.getTokenValue());
    }

    public AnnoResult<String> logout() {
        AnnoStpUtil.logout();
        return AnnoResult.succeed("退出成功");
    }

    public AnnoResult<String> clearSysUserCache() {
        String loginId = AnnoStpUtil.getLoginId("-1");
        if ("-1".equals(loginId)) {
            return AnnoResult.failure("请先登录");
        }
        // 登录用户
        AnUser anUser = AuthFunctions.getUserById.apply(loginId);
        AnnoStpUtil.setAuthUser(
            anUser.getId(),
            AnnoAuthUser.builder().
                userId(anUser.getId()).
                userName(anUser.getName()).
                userAccount(anUser.getMobile()).
                userMobile(anUser.getMobile()).
                userStatus(anUser.getEnable()).
                orgId(anUser.getOrgId()).
                build()
        );
        AuthFunctions.removePermRoleCacheList.accept(loginId);
        return AnnoResult.succeed("清除成功");
    }

    public AnnoResult<UserInfo> me() {
        UserInfo userInfo = new UserInfo();
        AnUser anUser = (AnUser) AnnoStpUtil.getSession().get("user");
        userInfo.setName(anUser.getName());
        userInfo.setAvatar(anUser.getAvatar());
        userInfo.setPerms(AnnoStpUtil.getPermissionList());
        userInfo.setRoles(AnnoStpUtil.getRoleList());
        return AnnoResult.succeed(userInfo);
    }
}
