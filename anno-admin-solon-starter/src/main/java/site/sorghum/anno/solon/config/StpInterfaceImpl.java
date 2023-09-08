package site.sorghum.anno.solon.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Condition;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;

import java.util.List;

/**
 * 权限验证接口扩展
 *
 * @author Sorghum
 * @since 2023/04/27
 */
@Component
@Slf4j
@Condition(onProperty = "${anno-admin.class.SaTokenInterceptor:true} = true")
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return AuthFunctions.permissionList.apply(loginId.toString());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return AuthFunctions.roleList.apply(loginId.toString());
    }

}