package site.sorghum.anno.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.modular.auth.service.AuthService;

import java.util.Collections;
import java.util.List;

/**
 * 权限验证接口扩展
 *
 * @author Sorghum
 * @since 2023/04/27
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Inject
    AuthService authService;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("loginId: {}, loginType: {}", loginId, loginType);
        return authService.permissionList(loginId.toString());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

}