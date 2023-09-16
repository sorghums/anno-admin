package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.ao.AnUser;

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

    /**
     * 校验是否有权限
     *
     * @param permissionCode 权限码
     * @throws site.sorghum.anno._common.exception.BizException 没有权限时，会抛出此异常
     */
    void verifyPermission(String permissionCode);


    /**
     * 校验是否有按钮权限
     */
    void verifyButtonPermission(String className, String methodName);
}
