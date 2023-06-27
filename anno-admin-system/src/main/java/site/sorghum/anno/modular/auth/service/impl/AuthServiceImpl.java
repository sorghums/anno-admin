package site.sorghum.anno.modular.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.base.anno.SysPermission;
import site.sorghum.anno.modular.system.anno.SysRole;
import site.sorghum.anno.modular.system.anno.SysRolePermission;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.modular.system.anno.SysUserRole;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 身份验证服务实现类
 *
 * @author Sorghum
 * @since 2023/06/27
 */
@ProxyComponent
public class AuthServiceImpl implements AuthService {
    @Inject
    AnnoService annoService;

    @Override
    public void resetPwd(Map<String, Object> props) {
        SysUser sysUser = new SysUser();
        sysUser.setId(props.get("id").toString());
        sysUser.setPassword("123456");
        annoService.updateById(sysUser);
    }

    @Override
    public SysUser verifyLogin(String mobile, String pwd) {
        SysUser sysUser = new SysUser();
        sysUser.setMobile(mobile);
        QueryRequest<SysUser> sysUserQueryRequest = new QueryRequest<>();
        sysUserQueryRequest.setClazz(SysUser.class);
        sysUserQueryRequest.setParam(sysUser);
        List<SysUser> users = annoService.list(sysUserQueryRequest);
        if (CollUtil.isEmpty(users)) {
            throw new BizException("用户不存在");
        }
        SysUser user = users.get(0);
        if (!user.getPassword().equals(pwd)) {
            throw new BizException("密码错误");
        }
        return user;
    }

    @Override
    public List<String> permissionList(String userId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        // 查询角色ID
        QueryRequest<SysUserRole> sysUserQueryRequest = new QueryRequest<>();
        sysUserQueryRequest.setClazz(SysUserRole.class);
        sysUserQueryRequest.setParam(sysUserRole);
        List<SysUserRole> list = annoService.list(sysUserQueryRequest);
        // 查询权限码
        List<String> roleIds = list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        QueryRequest<SysRolePermission> sysRolePermissionQueryRequest = new QueryRequest<>();
        sysRolePermissionQueryRequest.setClazz(SysRolePermission.class);
        sysRolePermissionQueryRequest.setAndSql("role_id in (" + String.join(",", roleIds) + ")");
        List<SysRolePermission> sysRolePermissions = annoService.list(sysRolePermissionQueryRequest);
        return sysRolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
    }
}
