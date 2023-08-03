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
    public void resetPwd(Map<String, Object> props);

    public SysUser verifyLogin(String mobile, String pwd);

    public SysUser getUserByMobile(String mobile);

    public SysUser getUserById(String id);

    public List<String> permissionList(String userId);

    List<String> roleList(String userId);

    void removePermRoleCacheList(String userId);
}
