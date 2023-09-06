package site.sorghum.anno.pre.plugin.service;

import site.sorghum.anno.pre.plugin.ao.AnUser;

import java.util.List;

/**
 * 身份验证服务
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public interface AuthService {

    AnUser verifyLogin(String mobile, String pwd);

    AnUser getUserById(String id);

    List<String> permissionList(String userId);

    List<String> roleList(String userId);

    void removePermRoleCacheList(String userId);
}
