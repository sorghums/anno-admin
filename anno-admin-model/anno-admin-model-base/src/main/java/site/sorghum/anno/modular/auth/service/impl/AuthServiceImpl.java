package site.sorghum.anno.modular.auth.service.impl;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.data.annotation.CacheRemove;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.common.util.MD5Util;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.auth.service.AuthService;
import site.sorghum.anno.modular.system.anno.SysPermission;
import site.sorghum.anno.modular.system.anno.SysRole;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.modular.system.dao.SysPermissionDao;
import site.sorghum.anno.modular.system.dao.SysRoleDao;
import site.sorghum.anno.modular.system.dao.SysUserDao;

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
public class AuthServiceImpl implements AuthService, EventListener<AppLoadEndEvent> {
    @Db
    SysUserDao sysUserDao;

    @Db
    SysRoleDao sysRoleDao;

    @Db
    SysPermissionDao sysPermissionDao;
    @Inject
    MetadataManager metadataManager;

    public void initPermissions() {
        // 初始化的时候，进行Db的注入
        List<AnEntity> allEntity = metadataManager.getAllEntity();
        for (AnEntity anEntity : allEntity) {
            if (anEntity.isEnablePermission()) {
                String baseCode = anEntity.getPermissionCode();
                String baseName = anEntity.getPermissionCodeTranslate();
                SysPermission sysPermission = sysPermissionDao.selectById(baseCode);
                if (sysPermission != null && sysPermission.getId() != null) {
                    return;
                }

                SysPermission basePermission = new SysPermission();
                basePermission.setId(baseCode);
                basePermission.setCode(baseCode);
                basePermission.setName(baseName);
                basePermission.setDelFlag(0);
                sysPermissionDao.insert(basePermission, true);


                // 新增
                String addCode = baseCode + ":" + PermissionProxy.ADD;
                String addName = baseName + ":" + PermissionProxy.ADD_TRANSLATE;

                SysPermission addPermission = new SysPermission();
                addPermission.setId(addCode);
                addPermission.setParentId(baseCode);
                addPermission.setCode(addCode);
                addPermission.setName(addName);
                addPermission.setDelFlag(0);

                sysPermissionDao.insert(addPermission, true);

                // 修改
                String updateCode = baseCode + ":" + PermissionProxy.UPDATE;
                String updateName = baseName + ":" + PermissionProxy.UPDATE_TRANSLATE;

                SysPermission updatePermission = new SysPermission();
                updatePermission.setId(updateCode);
                updatePermission.setParentId(baseCode);
                updatePermission.setCode(updateCode);
                updatePermission.setName(updateName);
                updatePermission.setDelFlag(0);

                sysPermissionDao.insert(updatePermission, true);

                // 删除
                String deleteCode = baseCode + ":" + PermissionProxy.DELETE;
                String deleteName = baseName + ":" + PermissionProxy.DELETE_TRANSLATE;

                SysPermission deletePermission = new SysPermission();
                deletePermission.setId(deleteCode);
                deletePermission.setParentId(baseCode);
                deletePermission.setCode(deleteCode);
                deletePermission.setName(deleteName);
                deletePermission.setDelFlag(0);

                sysPermissionDao.insert(deletePermission, true);
            }
        }

    }

    @Override
    public void resetPwd(Map<String, Object> props) {
        SysUser sysUser = new SysUser();
        String mobile = props.get("mobile").toString();
        String id = props.get("id").toString();
        sysUser.setId(id);
        sysUser.setPassword(MD5Util.digestHex(mobile + ":" + "123456"));
        sysUserDao.updateById(sysUser, true);
    }

    @Override
    public SysUser verifyLogin(String mobile, String pwd) {
        SysUser sysUser = sysUserDao.queryByMobile(mobile);
        if (sysUser == null) {
            throw new BizException("用户不存在");
        }
        // 清除缓存
        this.removePermissionCacheList(sysUser.getId());
        if (!sysUser.getPassword().equals(MD5Util.digestHex(mobile + ":" + pwd))) {
            throw new BizException("密码错误");
        }
        return sysUser;
    }

    @Override
    public SysUser getUserByMobile(String mobile) {
        SysUser sysUser = sysUserDao.queryByMobile(mobile);
        if (sysUser == null) {
            throw new BizException("用户不存在");
        }
        return sysUser;
    }

    @Override
    public SysUser getUserById(String id) {
        return sysUserDao.selectById(id);
    }

    @Override
    @Cache(key = "permissionList", seconds = 60 * 60 * 2)
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

    @Override
    @CacheRemove(keys = "permissionList")
    public void removePermissionCacheList(String userId) {
        // 清除缓存
    }

    @Override
    public void onEvent(AppLoadEndEvent appLoadEndEvent) throws Throwable {
        initPermissions();
    }
}
