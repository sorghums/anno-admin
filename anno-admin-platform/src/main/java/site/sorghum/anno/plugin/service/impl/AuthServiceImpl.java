package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._annotations.Proxy;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.CacheUtil;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno._metadata.PermissionContext;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermissionImpl;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoChartFieldImpl;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.pf4j.Pf4jRunner;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.plugin.ao.*;
import site.sorghum.anno.plugin.dao.AnAnnoMenuDao;
import site.sorghum.anno.plugin.dao.AnPermissionDao;
import site.sorghum.anno.plugin.dao.AnRoleDao;
import site.sorghum.anno.plugin.dao.AnUserDao;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;
import site.sorghum.anno.plugin.function.AuthFunction;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.Arrays;
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
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Inject
    AnUserDao anUserDao;

    @Inject
    AnRoleDao anRoleDao;

    @Inject
    AnPermissionDao anPermissionDao;

    @Inject
    AnAnnoMenuDao anAnnoMenuDao;

    @Inject
    @Named("dbServiceWood")
    DbService dbService;
    @Inject
    MetadataManager metadataManager;
    @Inject
    PermissionContext permissionContext;

    private final static String permissionKey = "permissionList:";

    public void initPermissions() {
        // 初始化的时候，进行Db的注入
        List<AnEntity> allEntity = metadataManager.getAllEntity();
        for (AnEntity anEntity : allEntity) {
            initPermissions(anEntity);
        }
        CacheUtil.delKeyPattern(permissionKey + "*");
    }

    public void initPermissions(AnEntity anEntity){
        AnnoPermissionImpl annoPermission = anEntity.getAnnoPermission();
        if (annoPermission.enable()) {
            String baseCode = annoPermission.baseCode();
            String baseName = annoPermission.baseCodeTranslate();
            // 按钮权限每次必查
            List<AnnoButtonImpl> anColumnButtons = anEntity.getColumnButtons();
            for (AnnoButtonImpl anColumnButton : anColumnButtons) {
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
                    anPermissionDao.insert(buttonPermission);
                }
            }

            List<AnnoTableButtonImpl> anEntityTableButtons = anEntity.getTableButtons();
            for (AnnoTableButtonImpl anButton : anEntityTableButtons) {
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
                    anPermissionDao.insert(buttonPermission);
                }
            }

            // TODO 图表权限每次必查
            List<AnnoChartFieldImpl> anChartFields = Arrays.asList(anEntity.getAnnoChart().getChartFields());
            for (AnnoChartFieldImpl anChartField : anChartFields) {
                if (StrUtil.isNotBlank(anChartField.getPermissionCode())) {
                    String chartFieldCode = baseCode + ":" + anChartField.getPermissionCode();
                    AnPermission anPermission = anPermissionDao.selectByCode(chartFieldCode);
                    if (anPermission != null && anPermission.getId() != null) {
                        continue;
                    }
                    AnPermission buttonPermission = new AnPermission();
                    buttonPermission.setParentId(baseCode);
                    buttonPermission.setCode(chartFieldCode);
                    buttonPermission.setName(baseName + ":" + anChartField.getName());
                    buttonPermission.setDelFlag(0);
                    anPermissionDao.insert(buttonPermission);
                }
            }

            AnPermission anPermission = anPermissionDao.selectByCode(baseCode);
            if (anPermission != null && anPermission.getId() != null) {
                return;
            }

            AnPermission basePermission = new AnPermission();
            basePermission.setId(baseCode);
            basePermission.setCode(baseCode);
            basePermission.setName(baseName);
            basePermission.setDelFlag(0);
            anPermissionDao.insert(basePermission);

            // 查看
            String viewCode = baseCode + ":" + PermissionProxy.VIEW;
            String viewName = baseName + ":" + PermissionProxy.VIEW_TRANSLATE;
            AnPermission viewPermission = new AnPermission();
            viewPermission.setId(viewCode);
            viewPermission.setParentId(baseCode);
            viewPermission.setCode(viewCode);
            viewPermission.setName(viewName);
            viewPermission.setDelFlag(0);

            anPermissionDao.insert(viewPermission);

            // 新增
            String addCode = baseCode + ":" + PermissionProxy.ADD;
            String addName = baseName + ":" + PermissionProxy.ADD_TRANSLATE;

            AnPermission addPermission = new AnPermission();
            addPermission.setId(addCode);
            addPermission.setParentId(baseCode);
            addPermission.setCode(addCode);
            addPermission.setName(addName);
            addPermission.setDelFlag(0);

            anPermissionDao.insert(addPermission);

            // 修改
            String updateCode = baseCode + ":" + PermissionProxy.UPDATE;
            String updateName = baseName + ":" + PermissionProxy.UPDATE_TRANSLATE;

            AnPermission updatePermission = new AnPermission();
            updatePermission.setId(updateCode);
            updatePermission.setParentId(baseCode);
            updatePermission.setCode(updateCode);
            updatePermission.setName(updateName);
            updatePermission.setDelFlag(0);

            anPermissionDao.insert(updatePermission);

            // 删除
            String deleteCode = baseCode + ":" + PermissionProxy.DELETE;
            String deleteName = baseName + ":" + PermissionProxy.DELETE_TRANSLATE;

            AnPermission deletePermission = new AnPermission();
            deletePermission.setId(deleteCode);
            deletePermission.setParentId(baseCode);
            deletePermission.setCode(deleteCode);
            deletePermission.setName(deleteName);
            deletePermission.setDelFlag(0);

            anPermissionDao.insert(deletePermission);

        }
    }

    @Override
    public AnUser verifyLogin(String mobile, String pwd) {
        AnUser anUser = anUserDao.queryByMobile(mobile);
        if (anUser == null) {
            throw new BizException("用户不存在");
        }
        // 清除缓存
        AuthFunction.removePermRoleCacheList.accept(anUser.getId());
        if (!anUser.getPassword().equals(MD5Util.digestHex(mobile + ":" + pwd))) {
            throw new BizException("密码错误");
        }
        return anUser;
    }

    @Override
    public AnUser getUserById(String id) {
        return anUserDao.findById(id);
    }

    @Override
    public List<String> permissionList(String userId) {
        String key = permissionKey + userId;
        if (CacheUtil.containsCache(key)) {
            return CacheUtil.getCacheList(key, String.class);
        }
        List<String> roleIds = AuthFunction.roleList.apply(userId);
        List<String> permissionCodes;
        List<AnPermission> anPermissions;
        if (roleIds.contains("admin")) {
            anPermissions = anPermissionDao.bizList();
        } else {
            anPermissions = anPermissionDao.querySysPermissionByUserId(userId);
        }
        permissionCodes = anPermissions.stream().map(AnPermission::getCode).collect(Collectors.toList());
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
        String keyTwo = permissionKey + userId;
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
    public void saveLoginLog(AnLoginLog anLoginLog) {
        try {
            dbService.insert(anLoginLog);
        } catch (Exception e) {
            log.error("登录日志保存失败===>" + e);
        }
    }

    @Override
    public void forceLogout(Map<String, Object> data) {
        String token = data.get("token").toString();
        String userId = data.get("userId").toString();
        List<String> tokenValues = AnnoStpUtil.searchTokenValue(token, 0, 20, false).stream().map(
            s -> s.split(":")[s.split(":").length - 1]
        ).toList();
        for (String tokenValue : tokenValues) {
            Object id = AnnoStpUtil.getLoginIdByToken(tokenValue);
            if (Objects.equals(userId, id)) {
                AnnoStpUtil.logoutByTokenValue(tokenValue);
            }
        }
    }

    @Override
    public void updatePwd(UpdatePwdReq req) {
        AnUser anUser = anUserDao.findById(req.getUserId());
        if (anUser == null) {
            throw new BizException("用户不存在");
        }
        if (!req.getFromAdmin()) {
            // 校验旧密码
            if (!anUser.getPassword().equals(MD5Util.digestHex(anUser.getMobile() + ":" + req.getOldPwd()))) {
                throw new BizException("密码错误");
            }
        }
        // 校验新密码
        if (!req.getNewPwd1().equals(req.getNewPwd2())) {
            throw new BizException("新密码不一致");
        }
        // 更新密码
        AnUser updatePwdEntity = new AnUser();
        updatePwdEntity.setId(req.getUserId());
        updatePwdEntity.setPassword(MD5Util.digestHex(anUser.getMobile() + ":" + req.getNewPwd1()));
        anUserDao.updateById(updatePwdEntity);
        // 清楚缓存
        AuthFunction.removePermRoleCacheList.accept(anUser.getId());
    }

    /**
     * 初始化菜单数据
     */
    @Override
    public void initMenus() {
        List<AnnoPlugin> annoPlugins = Pf4jRunner.PLUGIN_MANAGER.getExtensions(AnnoPlugin.class);
        for (AnnoPlugin annoPlugin : annoPlugins) {
            this.initMenus(annoPlugin);
        }
    }

    /**
     * 初始化菜单数据
     */
    public void initMenus(AnnoPlugin annoPlugin) {
        List<AnPluginMenu> anPluginMenus = annoPlugin.initEntityMenus();
        if (CollUtil.isEmpty(anPluginMenus)) {
            return;
        }
        for (AnPluginMenu anPluginMenu : anPluginMenus) {
            AnAnnoMenu anAnnoMenu = anAnnoMenuDao.findById(anPluginMenu.getId());
            AnAnnoMenu updateAnnoMenu = null;
            if (anAnnoMenu == null || anAnnoMenu.getId() == null) {
                anAnnoMenu = new AnAnnoMenu();
                anAnnoMenu.setId(anPluginMenu.getId());
                anAnnoMenu.setTitle(anPluginMenu.getTitle());
                anAnnoMenu.setType(anPluginMenu.getType());
                anAnnoMenu.setSort(anPluginMenu.getSort());
                anAnnoMenu.setIcon(anPluginMenu.getIcon());
                if (anPluginMenu.getEntity() != null) {
                    anAnnoMenu.setParseData(anPluginMenu.getEntity().getEntityName());
                    AnnoPermissionImpl annoPermission = anPluginMenu.getEntity().getAnnoPermission();
                    if (annoPermission.enable()) {
                        anAnnoMenu.setPermissionId(annoPermission.baseCode());
                    }
                }
                anAnnoMenu.setParseType(AnAnnoMenu.ParseTypeConstant.ANNO_MAIN);
                anAnnoMenu.setParentId(anPluginMenu.getParentId());
                anAnnoMenu.setDelFlag(0);
                anAnnoMenuDao.insert(anAnnoMenu);
            } else {
                int update = 0;
                updateAnnoMenu = new AnAnnoMenu();
                updateAnnoMenu.setId(anAnnoMenu.getId());
                if (!StrUtil.equals(anPluginMenu.getTitle(), anAnnoMenu.getTitle())) {
                    updateAnnoMenu.setTitle(anPluginMenu.getTitle());
                    update = 1;
                }
                if (!Objects.equals(anPluginMenu.getSort(), anAnnoMenu.getSort())) {
                    updateAnnoMenu.setSort(anPluginMenu.getSort());
                    update = 1;
                }
                if (!StrUtil.equals(anPluginMenu.getIcon(), anAnnoMenu.getIcon())) {
                    updateAnnoMenu.setIcon(anPluginMenu.getIcon());
                    update = 1;
                }
                if (!StrUtil.equals(anPluginMenu.getParentId(), anAnnoMenu.getParentId())) {
                    updateAnnoMenu.setParentId(anPluginMenu.getParentId());
                    update = 1;
                }
                if (anPluginMenu.getEntity() != null) {
                    AnnoPermissionImpl annoPermission = anPluginMenu.getEntity().getAnnoPermission();
                    if (annoPermission.enable() && !StrUtil.equals(annoPermission.getBaseCode(), anAnnoMenu.getPermissionId())) {
                        updateAnnoMenu.setPermissionId(annoPermission.getBaseCode());
                        update = 1;
                    }
                    if (!StrUtil.equals(anPluginMenu.getEntity().getEntityName(), anAnnoMenu.getParseData())) {
                        updateAnnoMenu.setParseData(anPluginMenu.getEntity().getEntityName());
                        update = 1;
                    }
                }
                if (update == 1) {
                    anAnnoMenuDao.updateById(updateAnnoMenu);
                }
            }

        }
    }
}
