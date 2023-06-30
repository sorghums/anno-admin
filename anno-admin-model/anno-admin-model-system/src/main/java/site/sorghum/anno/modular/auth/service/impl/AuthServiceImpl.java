package site.sorghum.anno.modular.auth.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.SneakyThrows;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.base.model.BaseMetaModel;
import site.sorghum.anno.modular.system.anno.*;
import site.sorghum.anno.modular.system.dao.SysPermissionDao;
import site.sorghum.anno.modular.system.dao.SysRoleDao;
import site.sorghum.anno.modular.system.dao.SysUserDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 身份验证服务实现类
 *
 * @author Sorghum
 * @since 2023/06/27
 */
@ProxyComponent
public class AuthServiceImpl implements AuthService {
    @Db
    DbContext dbContext;

    @Inject
    AnnoService annoService;

    @Db
    SysUserDao sysUserDao;

    @Db
    SysRoleDao sysRoleDao;

    @Db
    SysPermissionDao sysPermissionDao;

    @Override
    public void resetPwd(Map<String, Object> props) {
        SysUser sysUser = new SysUser();
        sysUser.setId(props.get("id").toString());
        sysUser.setPassword("123456");
        sysUserDao.updateById(sysUser,true);
    }

    @Override
    public SysUser verifyLogin(String mobile, String pwd) {
        HashMap<String, Object> queryMap = MapUtil.newHashMap();
        queryMap.put("mobile", mobile);
        List<SysUser> sysUsers = sysUserDao.selectByMap(queryMap);
        if (sysUsers.isEmpty()){
            throw new BizException("用户不存在");
        }
        SysUser user = sysUsers.get(0);
        if (!user.getPassword().equals(pwd)) {
            throw new BizException("密码错误");
        }
        return user;
    }

    @Override
    public List<String> permissionList(String userId) {
        List<SysRole> sysRoles = sysRoleDao.querySysRoleByUserId(userId);
        List<String> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toList());
        if (roleIds.contains("admin")) {
            List<SysPermission> sysPermissions = sysPermissionDao.list();
            return sysPermissions.stream().map(SysPermission::getCode).collect(Collectors.toList());
        }
        List<SysPermission> sysPermissions = sysPermissionDao.querySysPermissionByUserId(userId);
        return sysPermissions.stream().map(SysPermission::getCode).collect(Collectors.toList());
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
                    SysPermission sysPermission = sysPermissionDao.selectById(baseCode);
                    if (sysPermission != null && sysPermission.getId() != null) {
                        return;
                    }

                    SysPermission basePermission = new SysPermission();
                    basePermission.setId(baseCode);
                    basePermission.setCode(baseCode);
                    basePermission.setName(baseName);
                    basePermission.setDelFlag(0);
                    sysPermissionDao.insert(basePermission,true);


                    // 新增
                    String addCode = baseCode + ":" + PermissionProxy.ADD;
                    String addName = baseName + ":" + PermissionProxy.ADD_TRANSLATE;

                    SysPermission addPermission = new SysPermission();
                    addPermission.setId(addCode);
                    addPermission.setParentId(baseCode);
                    addPermission.setCode(addCode);
                    addPermission.setName(addName);
                    addPermission.setDelFlag(0);

                    sysPermissionDao.insert(addPermission,true);

                    // 修改
                    String updateCode = baseCode + ":" + PermissionProxy.UPDATE;
                    String updateName = baseName + ":" + PermissionProxy.UPDATE_TRANSLATE;

                    SysPermission updatePermission = new SysPermission();
                    updatePermission.setId(updateCode);
                    updatePermission.setParentId(baseCode);
                    updatePermission.setCode(updateCode);
                    updatePermission.setName(updateName);
                    updatePermission.setDelFlag(0);

                    sysPermissionDao.insert(updatePermission,true);

                    // 删除
                    String deleteCode = baseCode + ":" + PermissionProxy.DELETE;
                    String deleteName = baseName + ":" + PermissionProxy.DELETE_TRANSLATE;

                    SysPermission deletePermission = new SysPermission();
                    deletePermission.setId(deleteCode);
                    deletePermission.setParentId(baseCode);
                    deletePermission.setCode(deleteCode);
                    deletePermission.setName(deleteName);
                    deletePermission.setDelFlag(0);

                    sysPermissionDao.insert(deletePermission,true);

                }
        );
    }
}
