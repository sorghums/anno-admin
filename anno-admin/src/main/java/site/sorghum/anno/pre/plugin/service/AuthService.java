package site.sorghum.anno.pre.plugin.service;

import site.sorghum.anno.pre.plugin.ao.SysUser;

import java.util.List;
import java.util.Map;

/**
 * 身份验证服务
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public interface AuthService {

    SysUser verifyLogin(String mobile, String pwd);

    SysUser getUserById(String id);

    List<String> permissionList(String userId);

    List<String> roleList(String userId);

    void removePermRoleCacheList(String userId);
}
