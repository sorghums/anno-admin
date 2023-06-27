package site.sorghum.anno.modular.auth.service;

import site.sorghum.anno.modular.system.anno.SysRole;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.modular.system.anno.SysUserRole;

import java.util.List;
import java.util.Map;

/**
 * 身份验证服务
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public interface AuthService {
    public void resetPwd(Map<String,Object> props);

    public SysUser verifyLogin(String mobile, String pwd);

    public List<String> permissionList(String userId);
}
