package site.sorghum.anno.modular.auth.service.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.system.anno.SysPermission;
import site.sorghum.anno.modular.system.anno.SysRolePermission;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.modular.system.anno.SysUserRole;

import java.util.ArrayList;
import java.util.Collection;
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

    @Init
    public void init() {
        // 初始化的时候，进行Db的注入
        List<AnnoPermission> annoPermissions = new ArrayList<>();
        Collection<Class<?>> classes = AnnoClazzCache.fetchAllClazz();
        for (Class<?> aClass : classes) {
            // 获取类上注解
            AnnoMain annoMain = AnnotationUtil.getAnnotation(aClass, AnnoMain.class);
            AnnoPermission annoPermission = annoMain.annoPermission();
            if (annoPermission.enable()) {
                annoPermissions.add(annoPermission);
            }
        }
        // 插入数据库
        annoPermissions.forEach(
                annoPermission -> {
                    String baseCode = annoPermission.baseCode();
                    String baseName = annoPermission.baseCodeTranslate();

                    SysPermission sysPermission = annoService.queryById(SysPermission.class, baseCode);
                    if (sysPermission != null && sysPermission.getId() != null) {
                        return;
                    }

                    SysPermission basePermission = new SysPermission();
                    basePermission.setId(baseCode);
                    basePermission.setCode(baseCode);
                    basePermission.setName(baseName);
                    basePermission.setDelFlag(0);

                    annoService.onlySave(basePermission);


                    // 新增
                    String addCode = baseCode + ":" + PermissionProxy.ADD;
                    String addName = baseName + ":" + PermissionProxy.ADD_TRANSLATE;

                    SysPermission addPermission = new SysPermission();
                    addPermission.setId(addCode);
                    addPermission.setParentId(baseCode);
                    addPermission.setCode(addCode);
                    addPermission.setName(addName);
                    addPermission.setDelFlag(0);

                    annoService.onlySave(addPermission);

                    // 修改
                    String updateCode = baseCode + ":" + PermissionProxy.UPDATE;
                    String updateName = baseName + ":" + PermissionProxy.UPDATE_TRANSLATE;

                    SysPermission updatePermission = new SysPermission();
                    updatePermission.setId(updateCode);
                    updatePermission.setParentId(baseCode);
                    updatePermission.setCode(updateCode);
                    updatePermission.setName(updateName);
                    updatePermission.setDelFlag(0);

                    annoService.onlySave(updatePermission);

                    // 删除
                    String deleteCode = baseCode + ":" + PermissionProxy.DELETE;
                    String deleteName = baseName + ":" + PermissionProxy.DELETE_TRANSLATE;

                    SysPermission deletePermission = new SysPermission();
                    deletePermission.setId(deleteCode);
                    deletePermission.setParentId(baseCode);
                    deletePermission.setCode(deleteCode);
                    deletePermission.setName(deleteName);
                    deletePermission.setDelFlag(0);

                    annoService.onlySave(deletePermission);
                }
        );
    }
}
