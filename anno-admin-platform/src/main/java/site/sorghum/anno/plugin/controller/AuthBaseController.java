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
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.ao.AnLoginLog;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.common.LoginInfo;
import site.sorghum.anno.plugin.entity.request.LoginReq;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;
import site.sorghum.anno.plugin.entity.response.UserInfo;
import site.sorghum.anno.plugin.entity.response.VbenMenu;
import site.sorghum.anno.plugin.function.AuthFunction;
import site.sorghum.anno.plugin.manager.CaptchaManager;
import site.sorghum.anno.plugin.service.AnnoMenuService;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.Objects;
import java.util.Optional;

/**
 * 认证基础控制器
 * <br>处理用户登录、登出、信息获取、密码修改等认证相关操作
 */
@Named
@Slf4j
public class AuthBaseController {

    @Inject
    private AuthService authService;
    @Inject
    private PermissionProxy permissionProxy;
    @Inject
    private CaptchaManager captchaManager;
    @Inject
    private AnnoMenuService annoMenuService;

    /**
     * 构建登录日志
     * @param info 登录信息
     * @return 登录日志对象
     */
    private static AnLoginLog buildLoginLog(LoginInfo info) {
        AnLoginLog loginLog = new AnLoginLog();
        loginLog.setId(IdUtil.getSnowflakeNextIdStr());

        // 处理IP地址
        String ip = info.getIp().contains("0:0:0:0:0:0:0:1") ? "127.0.0.1" : info.getIp();
        loginLog.setLatestIp(ip);
        loginLog.setLatestTime(DateUtil.date());

        // 解析UserAgent
        UserAgent ua = UserAgentUtil.parse(info.getUserAgent());
        loginLog.setBrowser(ua.getBrowser().getName() + " " + ua.getVersion());
        loginLog.setOs(ua.getPlatform().getName() + " " + ua.getOsVersion());
        loginLog.setDevice(ua.isMobile() ? "mobile" : "computer");

        return loginLog;
    }

    /**
     * 用户登录
     * @param req 登录请求参数
     * @param loginInfo 登录附加信息(IP、UserAgent等)
     * @return 登录结果(包含token)
     */
    public AnnoResult<String> login(LoginReq req, LoginInfo loginInfo) {
        // 参数校验
        if (req.getUsername() == null || req.getPassword() == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }

        // 验证码校验
        captchaManager.verifyCaptcha(req.getCodeKey(), req.getCode());

        // 用户认证
        AnUser user = AuthFunction.verifyLogin.apply(req);
        if ("0".equals(user.getEnable())) {
            throw new BizException("账号已被禁用");
        }

        // 执行登录
        AnnoStpUtil.login(user.getId());

        // 记录登录日志
        AnLoginLog loginLog = buildLoginLog(loginInfo);
        loginLog.setUserId(user.getId());
        authService.saveLoginLog(loginLog);

        // 获取用户首页路径
        String homePath = Optional.ofNullable(annoMenuService.getById(user.getHomeMenu()))
            .map(VbenMenu::toVbenMenu)
            .map(VbenMenu::getPath)
            .orElse("");

        // 构建并缓存用户认证信息
        cacheAuthUserInfo(user, loginLog, homePath);

        return AnnoResult.succeed(AnnoStpUtil.getTokenValue());
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    public AnnoResult<String> logout() {
        AnnoStpUtil.logout();
        return AnnoResult.succeed("退出成功");
    }

    /**
     * 清除用户缓存
     * @return 操作结果
     */
    public AnnoResult<String> clearSysUserCache() {
        String loginId = AnnoStpUtil.getLoginId("-1");
        if ("-1".equals(loginId)) {
            return AnnoResult.failure("请先登录");
        }

        // 重新加载用户信息并更新缓存
        refreshAuthUserCache(loginId);

        // 清除权限角色缓存
        AuthFunction.removePermRoleCacheList.accept(loginId);

        return AnnoResult.succeed("清除成功");
    }

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    public AnnoResult<UserInfo> me() {
        // 登录校验
        permissionProxy.checkLogin();

        UserInfo userInfo = new UserInfo();
        AnnoAuthUser authUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getTokenValue());

        if (Objects.isNull(authUser)) {
            userInfo.setName("外部用户");
        } else {
            buildUserInfo(userInfo, authUser);
        }

        return AnnoResult.succeed(userInfo);
    }

    /**
     * 修改密码
     * @param req 密码修改请求
     * @return 操作结果
     */
    public AnnoResult<String> updatePwd(UpdatePwdReq req) {
        // 登录校验
        permissionProxy.checkLogin();
        authService.updatePwd(req);
        return AnnoResult.succeed("密码修改成功");
    }

    /**
     * 缓存用户认证信息
     * @param user 用户实体
     * @param loginLog 登录日志
     * @param homePath 首页路径
     */
    private void cacheAuthUserInfo(AnUser user, AnLoginLog loginLog, String homePath) {
        AnnoStpUtil.setAuthUser(
            AnnoAuthUser.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userAccount(user.getMobile())
                .userMobile(user.getMobile())
                .userStatus(user.getEnable())
                .avatar(user.getAvatar())
                .orgId(user.getOrgId())
                .ip(loginLog.getLatestIp())
                .loginTime(loginLog.getLatestTime())
                .browser(loginLog.getBrowser())
                .os(loginLog.getOs())
                .device(loginLog.getDevice())
                .homePath(homePath)
                .build()
        );
    }

    /**
     * 刷新用户认证缓存
     * @param loginId 登录ID
     */
    private void refreshAuthUserCache(String loginId) {
        AnUser user = AuthFunction.getUserById.apply(loginId);
        AnnoAuthUser authUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getTokenValue());
        if (authUser == null) {
            return;
        }
        AnnoStpUtil.setAuthUser(
            AnnoAuthUser.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userAccount(user.getMobile())
                .userMobile(user.getMobile())
                .userStatus(user.getEnable())
                .avatar(user.getAvatar())
                .orgId(user.getOrgId())
                .ip(authUser.getIp())
                .loginTime(authUser.getLoginTime())
                .browser(authUser.getBrowser())
                .os(authUser.getOs())
                .device(authUser.getDevice())
                .build()
        );
    }

    /**
     * 构建用户信息
     * @param userInfo 用户信息对象
     * @param authUser 认证用户对象
     */
    private void buildUserInfo(UserInfo userInfo, AnnoAuthUser authUser) {
        userInfo.setName(authUser.getUserName());
        userInfo.setAvatar(authUser.getAvatar());
        userInfo.setHomePath(authUser.getHomePath());
        userInfo.setPerms(AnnoStpUtil.getPermissionList());
        userInfo.setRoles(AnnoStpUtil.getRoleList());
    }
}