package site.sorghum.anno.pre.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._annotations.Cache;
import site.sorghum.anno._annotations.CacheRemove;
import site.sorghum.anno._annotations.Proxy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno._metadata.AnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.pre.plugin.service.AuthService;
import site.sorghum.anno.pre.plugin.ao.SysAnnoMenu;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.pre.plugin.ao.SysPermission;
import site.sorghum.anno.pre.plugin.ao.SysRole;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.plugin.dao.SysAnnoMenuDao;
import site.sorghum.anno.pre.plugin.dao.SysPermissionDao;
import site.sorghum.anno.pre.plugin.dao.SysRoleDao;
import site.sorghum.anno.pre.plugin.dao.SysUserDao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 身份验证服务实现类
 *
 * @author Sorghum
 * @since 2023/06/27
 */
@Named
@Proxy
public class AuthServiceImpl implements AuthService {
    @Db
    SysUserDao sysUserDao;

    @Db
    SysRoleDao sysRoleDao;

    @Db
    SysPermissionDao sysPermissionDao;

    @Db
    SysAnnoMenuDao sysAnnoMenuDao;

    @Db
    DbContext dbContext;

    @Inject
    MetadataManager metadataManager;

    public void initPermissions() {
        // 初始化的时候，进行Db的注入
        List<AnEntity> allEntity = metadataManager.getAllEntity();
        for (AnEntity anEntity : allEntity) {
            if (anEntity.isEnablePermission()) {
                String baseCode = anEntity.getPermissionCode();
                String baseName = anEntity.getPermissionCodeTranslate();
                // 按钮权限每次必查
                List<AnButton> anButtons = anEntity.getButtons();
                for (AnButton anButton : anButtons) {
                    if (StrUtil.isNotBlank(anButton.getPermissionCode())) {
                        String buttonCode = baseCode + ":" + anButton.getPermissionCode();
                        SysPermission sysPermission = sysPermissionDao.selectById(buttonCode);
                        if (sysPermission != null && sysPermission.getId() != null) {
                            continue;
                        }
                        SysPermission buttonPermission = new SysPermission();
                        buttonPermission.setId(buttonCode);
                        buttonPermission.setParentId(baseCode);
                        buttonPermission.setCode(buttonCode);
                        buttonPermission.setName(baseName + ":" + anButton.getName());
                        buttonPermission.setDelFlag(0);
                        sysPermissionDao.insert(buttonPermission, true);
                    }
                }
                SysPermission sysPermission = sysPermissionDao.selectById(baseCode);
                if (sysPermission != null && sysPermission.getId() != null) {
                    continue;
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

                // 按钮权限

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
        this.removePermRoleCacheList(sysUser.getId());
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
        List<String> roleIds = AnnoBeanUtils.getBean(AuthService.class).roleList(userId);
        if (roleIds.contains("admin")) {
            List<SysPermission> sysPermissions = sysPermissionDao.list();
            return sysPermissions.stream().map(SysPermission::getCode).collect(Collectors.toList());
        }
        List<SysPermission> sysPermissions = sysPermissionDao.querySysPermissionByUserId(userId);
        return sysPermissions.stream().map(SysPermission::getCode).collect(Collectors.toList());
    }

    @Override
    @Cache(key = "roleList", seconds = 60 * 60 * 2)
    public List<String> roleList(String userId) {
        List<SysRole> sysRoles = sysRoleDao.querySysRoleByUserId(userId);
        return sysRoles.stream().map(SysRole::getId).collect(Collectors.toList());
    }

    @Override
    @CacheRemove(keys = "permissionList,roleList")
    public void removePermRoleCacheList(String userId) {
        // 清除缓存
    }

    /**
     * 初始化菜单数据
     */
    public void initMenus() throws SQLException {
        List<AnnoPlugin> annoPlugins = AnnoBeanUtils.getBeansOfType(AnnoPlugin.class);
        annoPlugins.forEach(AnnoPlugin::printPluginInfo);
        annoPlugins.forEach(AnnoPlugin::run);
        for (AnnoPlugin annoPlugin : annoPlugins) {
            List<AnPluginMenu> anPluginMenus = annoPlugin.initEntityMenus();
            if (CollUtil.isEmpty(anPluginMenus)) {
                continue;
            }
            for (AnPluginMenu anPluginMenu : anPluginMenus) {
                SysAnnoMenu sysAnnoMenu = sysAnnoMenuDao.selectById(anPluginMenu.getId());
                Map<String, Object> map = new HashMap<>();
                if (sysAnnoMenu == null) {
                    sysAnnoMenu = new SysAnnoMenu();
                    sysAnnoMenu.setId(anPluginMenu.getId());
                    sysAnnoMenu.setTitle(anPluginMenu.getTitle());
                    sysAnnoMenu.setType(anPluginMenu.getType());
                    sysAnnoMenu.setSort(anPluginMenu.getSort());
                    sysAnnoMenu.setOpenType("_iframe");
                    sysAnnoMenu.setIcon(anPluginMenu.getIcon());
                    if (anPluginMenu.getEntity() != null) {
                        sysAnnoMenu.setParseData(anPluginMenu.getEntity().getEntityName());
                        sysAnnoMenu.setHref("/system/config/amis/" + anPluginMenu.getEntity().getEntityName());
                        if (anPluginMenu.getEntity().isEnablePermission()) {
                            sysAnnoMenu.setPermissionId(anPluginMenu.getEntity().getTableName());
                        }
                    }
                    sysAnnoMenu.setParseType("annoMain");
                    sysAnnoMenu.setParentId(anPluginMenu.getParentId());
                    sysAnnoMenu.setDelFlag(0);
                    sysAnnoMenuDao.insert(sysAnnoMenu, true);
                } else {
                    if (!StrUtil.equals(anPluginMenu.getTitle(), sysAnnoMenu.getTitle())) {
                        map.put(metadataManager.getEntityField(SysAnnoMenu.class, "title").getTableFieldName(), anPluginMenu.getTitle());
                    }
                    if (!Objects.equals(anPluginMenu.getSort(), sysAnnoMenu.getSort())) {
                        map.put(metadataManager.getEntityField(SysAnnoMenu.class, "sort").getTableFieldName(), anPluginMenu.getSort());
                    }
                    if (!StrUtil.equals(anPluginMenu.getIcon(), sysAnnoMenu.getIcon())) {
                        map.put(metadataManager.getEntityField(SysAnnoMenu.class, "icon").getTableFieldName(), anPluginMenu.getIcon());
                    }
                    if (!StrUtil.equals(anPluginMenu.getParentId(), sysAnnoMenu.getParentId())) {
                        map.put(metadataManager.getEntityField(SysAnnoMenu.class, "parentId").getTableFieldName(), anPluginMenu.getParentId());
                    }
                    if (CollUtil.isNotEmpty(map)) {
                        dbContext.table(metadataManager.getEntity(SysAnnoMenu.class).getTableName())
                            .setMap(map)
                            .whereEq("id", anPluginMenu.getId())
                            .update();
                    }
                }

            }
        }
    }
}