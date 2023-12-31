package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._annotations.Proxy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.CacheUtil;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno._metadata.*;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.ao.AnPermission;
import site.sorghum.anno.plugin.ao.AnRole;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.dao.AnAnnoMenuDao;
import site.sorghum.anno.plugin.dao.AnPermissionDao;
import site.sorghum.anno.plugin.dao.AnRoleDao;
import site.sorghum.anno.plugin.dao.SysUserDao;
import site.sorghum.anno.plugin.interfaces.AuthFunctions;
import site.sorghum.anno.plugin.service.AuthService;

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
    AnRoleDao anRoleDao;

    @Db
    AnPermissionDao anPermissionDao;

    @Db
    AnAnnoMenuDao anAnnoMenuDao;

    @Db
    DbContext dbContext;

    @Inject
    MetadataManager metadataManager;
    @Inject
    PermissionContext permissionContext;

    public void initPermissions() {
        // 初始化的时候，进行Db的注入
        List<AnEntity> allEntity = metadataManager.getAllEntity();
        for (AnEntity anEntity : allEntity) {
            if (anEntity.isEnablePermission()) {
                String baseCode = anEntity.getPermissionCode();
                String baseName = anEntity.getPermissionCodeTranslate();
                // 按钮权限每次必查
                List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
                for (AnColumnButton anColumnButton : anColumnButtons) {
                    if (StrUtil.isNotBlank(anColumnButton.getPermissionCode())) {
                        String buttonCode = baseCode + ":" + anColumnButton.getPermissionCode();
                        AnPermission anPermission = anPermissionDao.selectByCode(buttonCode);
                        if (anPermission != null && anPermission.getId() != null) {
                            continue;
                        }
                        AnPermission buttonPermission = new AnPermission();
                        buttonPermission.setParentId(baseCode);
                        buttonPermission.setCode(buttonCode);
                        buttonPermission.setName(baseName + ":" + anColumnButton.getName());
                        buttonPermission.setDelFlag(0);
                        anPermissionDao.insert(buttonPermission, true);
                    }
                }

                List<AnButton> anEntityTableButtons = anEntity.getTableButtons();
                for (AnButton anButton : anEntityTableButtons) {
                    if (StrUtil.isNotBlank(anButton.getPermissionCode())) {
                        String buttonCode = baseCode + ":" + anButton.getPermissionCode();
                        AnPermission anPermission = anPermissionDao.selectByCode(buttonCode);
                        if (anPermission != null && anPermission.getId() != null) {
                            continue;
                        }
                        AnPermission buttonPermission = new AnPermission();
                        buttonPermission.setParentId(baseCode);
                        buttonPermission.setCode(buttonCode);
                        buttonPermission.setName(baseName + ":" + anButton.getName());
                        buttonPermission.setDelFlag(0);
                        anPermissionDao.insert(buttonPermission, true);
                    }
                }
                AnPermission anPermission = anPermissionDao.selectByCode(baseCode);
                if (anPermission != null && anPermission.getId() != null) {
                    continue;
                }

                AnPermission basePermission = new AnPermission();
                basePermission.setId(baseCode);
                basePermission.setCode(baseCode);
                basePermission.setName(baseName);
                basePermission.setDelFlag(0);
                anPermissionDao.insert(basePermission, true);

                // 查看
                String viewCode = baseCode + ":" + PermissionProxy.VIEW;
                String viewName = baseName + ":" + PermissionProxy.VIEW_TRANSLATE;
                AnPermission viewPermission = new AnPermission();
                viewPermission.setId(viewCode);
                viewPermission.setParentId(baseCode);
                viewPermission.setCode(viewCode);
                viewPermission.setName(viewName);
                viewPermission.setDelFlag(0);

                anPermissionDao.insert(viewPermission, true);

                // 新增
                String addCode = baseCode + ":" + PermissionProxy.ADD;
                String addName = baseName + ":" + PermissionProxy.ADD_TRANSLATE;

                AnPermission addPermission = new AnPermission();
                addPermission.setId(addCode);
                addPermission.setParentId(baseCode);
                addPermission.setCode(addCode);
                addPermission.setName(addName);
                addPermission.setDelFlag(0);

                anPermissionDao.insert(addPermission, true);

                // 修改
                String updateCode = baseCode + ":" + PermissionProxy.UPDATE;
                String updateName = baseName + ":" + PermissionProxy.UPDATE_TRANSLATE;

                AnPermission updatePermission = new AnPermission();
                updatePermission.setId(updateCode);
                updatePermission.setParentId(baseCode);
                updatePermission.setCode(updateCode);
                updatePermission.setName(updateName);
                updatePermission.setDelFlag(0);

                anPermissionDao.insert(updatePermission, true);

                // 删除
                String deleteCode = baseCode + ":" + PermissionProxy.DELETE;
                String deleteName = baseName + ":" + PermissionProxy.DELETE_TRANSLATE;

                AnPermission deletePermission = new AnPermission();
                deletePermission.setId(deleteCode);
                deletePermission.setParentId(baseCode);
                deletePermission.setCode(deleteCode);
                deletePermission.setName(deleteName);
                deletePermission.setDelFlag(0);

                anPermissionDao.insert(deletePermission, true);

                // 按钮权限

            }
        }

    }

    @Override
    public AnUser verifyLogin(String mobile, String pwd) {
        AnUser anUser = sysUserDao.queryByMobile(mobile);
        if (anUser == null) {
            throw new BizException("用户不存在");
        }
        // 清除缓存
        AuthFunctions.removePermRoleCacheList.accept(anUser.getId());
        if (!anUser.getPassword().equals(MD5Util.digestHex(mobile + ":" + pwd))) {
            throw new BizException("密码错误");
        }
        return anUser;
    }

    @Override
    public AnUser getUserById(String id) {
        return sysUserDao.selectById(id);
    }

    @Override
    public List<String> permissionList(String userId) {
        String key = "permissionList:" + userId;
        if (CacheUtil.containsCache(key)) {
            return CacheUtil.getCacheList(key, String.class);
        }
        List<String> roleIds = AuthFunctions.roleList.apply(userId);
        List<String> permissionCodes;
        if (roleIds.contains("admin")) {
            List<AnPermission> anPermissions = anPermissionDao.list();
            permissionCodes =  anPermissions.stream().map(AnPermission::getCode).collect(Collectors.toList());
        }else {
            List<AnPermission> anPermissions = anPermissionDao.querySysPermissionByUserId(userId);
            permissionCodes = anPermissions.stream().map(AnPermission::getCode).collect(Collectors.toList());
        }
        CacheUtil.putCache(key, permissionCodes, 60 * 60 * 2);
        return permissionCodes;
    }

    @Override
    public List<String> roleList(String userId) {
        String key = "roleList:" + userId;
        if (CacheUtil.containsCache(key)) {
            return CacheUtil.getCacheList(key, String.class);
        }
        List<AnRole> anRoles = anRoleDao.querySysRoleByUserId(userId);
        List<String> roleList = anRoles.stream().map(AnRole::getId).collect(Collectors.toList());
        CacheUtil.putCache(key, roleList, 60 * 60 * 2);
        return roleList;
    }

    @Override
    public void removePermRoleCacheList(String userId) {
        String keyOne = "roleList:" + userId;
        String keyTwo = "permissionList:" + userId;
        CacheUtil.delKey(keyOne);
        CacheUtil.delKey(keyTwo);
    }

    @Override
    public void verifyPermission(String permissionCode) {
        String userId = (String) AnnoStpUtil.getLoginId();
        List<String> permissionList = permissionList(userId);
        if (!permissionList.contains(permissionCode)) {
            throw new BizException("400", "没有权限:" + permissionContext.getPermissionName(permissionCode));
        }
    }

    @Override
    public void resetPwd(Map<String, Object> props) {
        AnUser anUser = new AnUser();
        String mobile = props.get("mobile").toString();
        String id = props.get("id").toString();
        anUser.setId(id);
        anUser.setPassword(MD5Util.digestHex(mobile + ":" + "123456"));
        sysUserDao.updateById(anUser, true);
    }

    @Override
    public void verifyButtonPermission(String className, String methodName) {
        String permissionCode = permissionContext.getPermissionCode(className, methodName);
        if (StrUtil.isBlank(permissionCode)) {
            throw new BizException("该方法不是按钮权限");
        }
        verifyPermission(permissionCode);
    }

    /**
     * 初始化菜单数据
     */
    public void initMenus() throws SQLException {
        List<AnnoPlugin> annoPlugins = AnnoBeanUtils.getBeansOfType(AnnoPlugin.class);
        for (AnnoPlugin annoPlugin : annoPlugins) {
            List<AnPluginMenu> anPluginMenus = annoPlugin.initEntityMenus();
            if (CollUtil.isEmpty(anPluginMenus)) {
                continue;
            }
            for (AnPluginMenu anPluginMenu : anPluginMenus) {
                AnAnnoMenu anAnnoMenu = anAnnoMenuDao.selectById(anPluginMenu.getId());
                Map<String, Object> map = new HashMap<>();
                if (anAnnoMenu == null) {
                    anAnnoMenu = new AnAnnoMenu();
                    anAnnoMenu.setId(anPluginMenu.getId());
                    anAnnoMenu.setTitle(anPluginMenu.getTitle());
                    anAnnoMenu.setType(anPluginMenu.getType());
                    anAnnoMenu.setSort(anPluginMenu.getSort());
                    anAnnoMenu.setIcon(anPluginMenu.getIcon());
                    if (anPluginMenu.getEntity() != null) {
                        anAnnoMenu.setParseData(anPluginMenu.getEntity().getEntityName());
                        anAnnoMenu.setHref("/system/config/amis/" + anPluginMenu.getEntity().getEntityName());
                        if (anPluginMenu.getEntity().isEnablePermission()) {
                            anAnnoMenu.setPermissionId(anPluginMenu.getEntity().getTableName());
                        }
                    }
                    anAnnoMenu.setParseType("annoMain");
                    anAnnoMenu.setParentId(anPluginMenu.getParentId());
                    anAnnoMenu.setDelFlag(0);
                    anAnnoMenuDao.insert(anAnnoMenu, true);
                } else {
                    if (!StrUtil.equals(anPluginMenu.getTitle(), anAnnoMenu.getTitle())) {
                        map.put(metadataManager.getEntityField(AnAnnoMenu.class, "title").getTableFieldName(), anPluginMenu.getTitle());
                    }
                    if (!Objects.equals(anPluginMenu.getSort(), anAnnoMenu.getSort())) {
                        map.put(metadataManager.getEntityField(AnAnnoMenu.class, "sort").getTableFieldName(), anPluginMenu.getSort());
                    }
                    if (!StrUtil.equals(anPluginMenu.getIcon(), anAnnoMenu.getIcon())) {
                        map.put(metadataManager.getEntityField(AnAnnoMenu.class, "icon").getTableFieldName(), anPluginMenu.getIcon());
                    }
                    if (!StrUtil.equals(anPluginMenu.getParentId(), anAnnoMenu.getParentId())) {
                        map.put(metadataManager.getEntityField(AnAnnoMenu.class, "parentId").getTableFieldName(), anPluginMenu.getParentId());
                    }
                    if (CollUtil.isNotEmpty(map)) {
                        dbContext.table(metadataManager.getEntity(AnAnnoMenu.class).getTableName())
                                .setMap(map)
                                .whereEq("id", anPluginMenu.getId())
                                .update();
                    }
                }

            }
        }
    }
}
