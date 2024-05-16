package site.sorghum.anno.plugin.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.anno.chart.AnChartService;
import site.sorghum.anno.plugin.ao.AnLoginLog;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.common.LoginInfo;
import site.sorghum.anno.plugin.entity.request.LoginReq;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;
import site.sorghum.anno.plugin.entity.response.UserInfo;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.plugin.manager.CaptchaManager;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.Objects;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Named
@Slf4j
public class AuthBaseController {

    @Inject
    AuthService authService;
    @Inject
    MetadataManager manager;
    @Inject
    AnChartService anChartService;
    @Inject
    CaptchaManager captchaManager;

    public AnnoResult<String> login(LoginReq req, LoginInfo loginInfo) {
        // 校验验证码
        captchaManager.verifyCaptcha(req.getCodeKey(), req.getCode());
        if (req.getUsername() == null || req.getPassword() == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }
        // 校验用户名密码
        AnUser anUser = AuthFunctions.verifyLogin.apply(req);
        if ("0".equals(anUser.getEnable())) {
            throw new BizException("账号已被禁用");
        }
        // 登录
        AnnoStpUtil.login(anUser.getId());
        // 保存登录日志
        AnLoginLog anLoginLog = buildLoginLog(loginInfo);
        anLoginLog.setUserId(anUser.getId());
        authService.saveLoginLog(anLoginLog);
        // 缓存用户信息
        AnnoStpUtil.setAuthUser(
            AnnoAuthUser.builder().
                userId(anUser.getId()).
                userName(anUser.getName()).
                userAccount(anUser.getMobile()).
                userMobile(anUser.getMobile()).
                userStatus(anUser.getEnable()).
                avatar(anUser.getAvatar()).
                orgId(anUser.getOrgId()).
                ip(anLoginLog.getLatestIp()).
                loginTime(anLoginLog.getLatestTime()).
                browser(anLoginLog.getBrowser()).
                os(anLoginLog.getOs()).
                device(anLoginLog.getDevice()).
                build()
        );
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
        // 现有session
        AnnoAuthUser authUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getTokenValue());
        AnnoStpUtil.setAuthUser(
            AnnoAuthUser.builder().
                userId(anUser.getId()).
                userName(anUser.getName()).
                userAccount(anUser.getMobile()).
                userMobile(anUser.getMobile()).
                userStatus(anUser.getEnable()).
                avatar(anUser.getAvatar()).
                orgId(anUser.getOrgId()).
                ip(authUser.getIp()).
                loginTime(authUser.getLoginTime()).
                browser(authUser.getBrowser()).
                os(authUser.getOs()).
                device(authUser.getDevice()).
                build()
        );
        AuthFunctions.removePermRoleCacheList.accept(loginId);
        return AnnoResult.succeed("清除成功");
    }

    public AnnoResult<UserInfo> me() {
        UserInfo userInfo = new UserInfo();
        // 校验登录
        CheckPermissionFunction.loginCheckFunction.run();
        AnnoAuthUser anUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getTokenValue());
        // 但是anno系统未登录，则返回外部用户
        if (Objects.isNull(anUser)){
            userInfo.setName("外部用户");
        }else{
            userInfo.setName(anUser.getUserName());
            userInfo.setAvatar(anUser.getAvatar());
            userInfo.setPerms(AnnoStpUtil.getPermissionList());
            userInfo.setRoles(AnnoStpUtil.getRoleList());
        }
        return AnnoResult.succeed(userInfo);
    }

    public AnnoResult<String> updatePwd(UpdatePwdReq req){
        // 校验登录
        CheckPermissionFunction.loginCheckFunction.run();
        authService.updatePwd(req);
        return AnnoResult.succeed();
    }
    private static AnLoginLog buildLoginLog(LoginInfo info) {
        AnLoginLog loginLog = new AnLoginLog();

        loginLog.setId(IdUtil.getSnowflakeNextIdStr());
        // ip
        loginLog.setLatestIp(info.getIp().contains("0:0:0:0:0:0:0:1") ? "127.0.0.1" : info.getIp());
        loginLog.setLatestTime(DateUtil.date());
        // userAgent
        UserAgent ua = UserAgentUtil.parse(info.getUserAgent());
        loginLog.setBrowser(ua.getBrowser().getName() + " " + ua.getVersion());
        loginLog.setOs(ua.getPlatform().getName() + " " + ua.getOsVersion());
        loginLog.setDevice(ua.isMobile() ? "mobile" : "computer");

        return loginLog;
    }
}
