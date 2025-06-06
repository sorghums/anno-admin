package site.sorghum.anno;


import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.anno.functions.AnnoFunction;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.plugin.ao.*;
import site.sorghum.anno.plugin.dao.AnPlatformDao;
import site.sorghum.anno.plugin.manager.OnlineDictManager;
import site.sorghum.anno.plugin.service.AuthService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Extension
public class BaseAnnoPlugin extends AnnoPlugin {

    DbService dbService;

    AnPlatformDao anPlatformDao;

    AnnoProperty annoProperty;

    OnlineDictManager onlineDictManager;

    public BaseAnnoPlugin() {
        super("管理端插件", "包含B端用户，角色，组织，权限等。");
    }

    @Override
    public int runOrder() {
        return 100;
    }

    @Override
    public void run() {
        dbService = AnnoBeanUtils.getBean(DbService.class);
        anPlatformDao = AnnoBeanUtils.getBean(AnPlatformDao.class);
        annoProperty = AnnoBeanUtils.getBean(AnnoProperty.class);
        onlineDictManager = AnnoBeanUtils.getBean(OnlineDictManager.class);
        // 权限校验
        AnnoFunction.permissionCheckFunction = (permissionCode) -> {
            AuthService authService = AnnoBeanUtils.getBean(AuthService.class);
            authService.verifyPermission(permissionCode);
        };
        // 权限初始化
        List<AnPermission> anPermissions = getAnPermissions();
        for (AnPermission anPermission : anPermissions) {
            DbCriteria criteria = DbCriteria.fromClass(AnPermission.class).eq("id", anPermission.getId());
            AnPermission one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anPermission);
            } else {
                dbService.update(anPermission, criteria);
            }
        }
        // 菜单初始化
        List<AnAnnoMenu> anMenus = getAnMenus();
        for (AnAnnoMenu anMenu : anMenus) {
            DbCriteria criteria = DbCriteria.fromClass(AnAnnoMenu.class).eq("id", anMenu.getId());
            AnAnnoMenu one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anMenu);
            } else {
                dbService.update(anMenu, criteria);
            }
        }
        // 组织初始化
        List<AnOrg> anOrgs = getAnOrgs();
        for (AnOrg anOrg : anOrgs) {
            DbCriteria criteria = DbCriteria.fromClass(AnOrg.class).eq("id", anOrg.getId());
            AnOrg one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anOrg);
            } else {
                dbService.update(anOrg, criteria);
            }
        }
        // 角色初始化
        List<AnRole> anRoles = getAnRoles();
        for (AnRole anRole : anRoles) {
            DbCriteria criteria = DbCriteria.fromClass(AnRole.class).eq("id", anRole.getId());
            AnRole one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anRole);
            } else {
                dbService.update(anRole, criteria);
            }
        }
        // 管理用户初始化
        List<AnUser> anUsers = getAnUsers();
        for (AnUser anUser : anUsers) {
            DbCriteria criteria = DbCriteria.fromClass(AnUser.class).eq("id", anUser.getId());
            AnUser one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anUser);
            }
        }
        // 用户角色初始化
        List<AnUserRole> anUserRoles = getAnUserRoles();
        for (AnUserRole anUserRole : anUserRoles) {
            DbCriteria criteria = DbCriteria.fromClass(AnUserRole.class).eq("id", anUserRole.getId());
            AnUserRole one = dbService.queryOne(criteria);
            if (one == null) {
                dbService.insert(anUserRole);
            }
        }

        // 基础平台信息初始化
        List<AnPlatform> list = anPlatformDao.list();
        if (list.isEmpty()) {
            AnPlatform platform = new AnPlatform();
            platform.setId("1");
            platform.setName(annoProperty.getPlatformTitle());
            platform.setPlatformLogo(annoProperty.getPlatformLogo());
            platform.setDescription(annoProperty.getPlatformDesc());
            anPlatformDao.insert(platform);
        }

        // 在线字典初始化
        onlineDictManager.loadDict();

    }

    private List<AnPermission> getAnPermissions() {
        AnPermission permission1 = new AnPermission();
        permission1.setId("an_api_doc");
        permission1.setName("接口文档");
        permission1.setCode("an_api_doc");
        permission1.setCreateTime(LocalDateTime.now());
        return CollUtil.newArrayList(permission1);
    }

    private List<AnAnnoMenu> getAnMenus() {
        AnAnnoMenu menu1 = new AnAnnoMenu();
        menu1.setId("10");
        menu1.setParentId("");
        menu1.setTitle("系统管理");
        menu1.setType(0);
        menu1.setSort(10000);
        menu1.setIcon("ant-design:setting-filled");
        menu1.setPermissionId("");
        menu1.setParseType("");
        menu1.setParseData(null);

        AnAnnoMenu menu2 = new AnAnnoMenu();
        menu2.setId("11");
        menu2.setParentId("10");
        menu2.setTitle("菜单管理");
        menu2.setType(1);
        menu2.setIcon("ant-design:menu-unfold-outlined");
        menu2.setPermissionId("an_anno_menu");
        menu2.setParseType("annoMain");
        menu2.setParseData("AnAnnoMenu");

        AnAnnoMenu menu3 = new AnAnnoMenu();
        menu3.setId("12");
        menu3.setParentId("10");
        menu3.setTitle("权限管理");
        menu3.setType(1);
        menu3.setIcon("ant-design:unlock-outlined");
        menu3.setPermissionId("an_permission");
        menu3.setParseType("annoMain");
        menu3.setParseData("AnPermission");

        AnAnnoMenu menu4 = new AnAnnoMenu();
        menu4.setId("13");
        menu4.setParentId("10");
        menu4.setTitle("用户管理");
        menu4.setType(1);
        menu4.setIcon("ant-design:user-outlined");
        menu4.setPermissionId("an_user");
        menu4.setParseType("annoMain");
        menu4.setParseData("AnUser");

        AnAnnoMenu menu5 = new AnAnnoMenu();
        menu5.setId("14");
        menu5.setParentId("10");
        menu5.setTitle("角色管理");
        menu5.setType(1);
        menu5.setIcon("ant-design:apartment-outlined");
        menu5.setPermissionId("an_role");
        menu5.setParseType("annoMain");
        menu5.setParseData("AnRole");

        AnAnnoMenu menu6 = new AnAnnoMenu();
        menu6.setId("15");
        menu6.setParentId("10");
        menu6.setTitle("组织管理");
        menu6.setType(1);
        menu6.setIcon("ant-design:trophy-outlined");
        menu6.setPermissionId("an_org");
        menu6.setParseType("annoMain");
        menu6.setParseData("AnOrg");

        AnAnnoMenu menu6_001 = new AnAnnoMenu();
        menu6_001.setId("15_001");
        menu6_001.setTitle("渲染管理");
        menu6_001.setParentId("10");
        menu6_001.setType(1);
        menu6_001.setIcon("ant-design:trophy-outlined");
        menu6_001.setPermissionId("an_render");
        menu6_001.setParseType("annoMain");
        menu6_001.setParseData("AnRender");

        AnAnnoMenu menu6_00 = new AnAnnoMenu();
        menu6_00.setId("15_00");
        menu6_00.setParentId("10");
        menu6_00.setTitle("字典管理");
        menu6_00.setType(0);
        menu6_00.setIcon("material-symbols-light:dictionary");
        menu6_00.setPermissionId("");
        menu6_00.setParseType("");
        menu6_00.setParseData(null);

        AnAnnoMenu menu6_01 = new AnAnnoMenu();
        menu6_01.setId("15_01");
        menu6_01.setParentId("15_00");
        menu6_01.setTitle("系统字典");
        menu6_01.setType(1);
        menu6_01.setIcon("arcticons:thai-dict");
        menu6_01.setPermissionId("an_dict_system");
        menu6_01.setParseType("annoMain");
        menu6_01.setParseData("AnDictSystem");

        AnAnnoMenu menu6_02 = new AnAnnoMenu();
        menu6_02.setId("15_02");
        menu6_02.setParentId("15_00");
        menu6_02.setTitle("业务字典");
        menu6_02.setType(1);
        menu6_02.setIcon("arcticons:thai-dict");
        menu6_02.setPermissionId("an_dict_biz");
        menu6_02.setParseType("annoMain");
        menu6_02.setParseData("AnDictBiz");

        AnAnnoMenu menu6_0 = new AnAnnoMenu();
        menu6_0.setId("15_0");
        menu6_0.setParentId("10");
        menu6_0.setTitle("C端管理");
        menu6_0.setType(1);
        menu6_0.setIcon("ant-design:mobile-outlined");
        menu6_0.setPermissionId("an_client_user");
        menu6_0.setParseType("annoMain");
        menu6_0.setParseData("AnClientUser");

        AnAnnoMenu menu6_1 = new AnAnnoMenu();
        menu6_1.setId("15_1");
        menu6_1.setParentId("10");
        menu6_1.setTitle("脚本管理");
        menu6_1.setType(1);
        menu6_1.setIcon("ant-design:copy-outlined");
        menu6_1.setPermissionId("an_sql");
        menu6_1.setParseType("annoMain");
        menu6_1.setParseData("AnSql");

        AnAnnoMenu menu6_2 = new AnAnnoMenu();
        menu6_2.setId("15_2");
        menu6_2.setParentId("10");
        menu6_2.setTitle("平台信息");
        menu6_2.setType(1);
        menu6_2.setIcon("ant-design:hdd-outlined");
        menu6_2.setPermissionId("an_platform");
        menu6_2.setParseType("annoMain");
        menu6_2.setParseData("AnPlatform");


        AnAnnoMenu menu7 = new AnAnnoMenu();
        menu7.setId("16");
        menu7.setParentId("10");
        menu7.setTitle("接口文档");
        menu7.setType(1);
        menu7.setIcon("ant-design:read-outlined");
        menu7.setPermissionId("an_api_doc");
        menu7.setParseType("iframe");
        menu7.setParseData("[[[apiServerUrl]]]/doc.html");

        AnAnnoMenu menu8 = new AnAnnoMenu();
        menu8.setId("20");
        menu8.setParentId("");
        menu8.setTitle("会话管理");
        menu8.setType(0);
        menu8.setSort(9000);
        menu8.setIcon("ant-design:wechat-filled");
        menu8.setPermissionId(null);
        menu8.setParseType("");
        menu8.setParseData(null);

        AnAnnoMenu menu9 = new AnAnnoMenu();
        menu9.setId("21");
        menu9.setParentId("20");
        menu9.setTitle("在线用户");
        menu9.setType(1);
        menu9.setIcon("ant-design:user-switch-outlined");
        menu9.setPermissionId("an_online_user");
        menu9.setParseType("annoMain");
        menu9.setParseData("AnOnlineUser");

        AnAnnoMenu menu10 = new AnAnnoMenu();
        menu10.setId("22");
        menu10.setParentId("20");
        menu10.setTitle("登录日志");
        menu10.setType(1);
        menu10.setIcon("ant-design:exception-outlined");
        menu10.setPermissionId("an_login_log");
        menu10.setParseType("annoMain");
        menu10.setParseData("AnLoginLog");
        ArrayList<AnAnnoMenu> anAnnoMenus = CollUtil.newArrayList(
            menu1,
            menu2,
            menu3,
            menu4,
            menu5,
            menu6,
            menu6_001,
            menu6_00,
            menu6_01,
            menu6_02,
            menu6_0,
            menu6_1,
            menu6_2,
            menu7,
            menu8,
            menu9,
            menu10
        );
        // 重写排序
        int beginSort = 100000;
        for (int i = 0; i < anAnnoMenus.size(); i++) {
            AnAnnoMenu anAnnoMenu = anAnnoMenus.get(i);
            if (anAnnoMenu.getSort() == null) {
                anAnnoMenu.setSort(beginSort + i);
            }
        }
        return anAnnoMenus;
    }

    private List<AnOrg> getAnOrgs() {
        AnOrg org = new AnOrg();
        org.setId("1674391485808001024");
        org.setOrgName("标准组织");
        org.setCreateBy(null);
        org.setCreateTime(LocalDateTime.now());
        org.setDelFlag(0);
        org.setUpdateBy(null);
        org.setUpdateTime(LocalDateTime.now());
        return CollUtil.newArrayList(org);
    }

    private List<AnRole> getAnRoles() {
        AnRole role = new AnRole();
        role.setId("admin");
        role.setRoleName("超级管理员（默认所有权限）");
        role.setSort(0);
        role.setEnable(1);
        role.setCreateBy(null);
        role.setCreateTime(LocalDateTime.now());
        role.setDelFlag(0);
        role.setUpdateBy(null);
        role.setUpdateTime(LocalDateTime.now());

        return CollUtil.newArrayList(role);
    }


    private List<AnUser> getAnUsers() {
        AnUser user = new AnUser();
        user.setId("1666356287765979136");
        user.setAvatar("https://solon.noear.org/img/solon/favicon.png");
        user.setMobile("18888888888");
        user.setPassword("caf545f0cdd499df43d34613dcfa70c0");
        user.setName("超级管理员");
        user.setEnable("1");
        user.setCreateBy(null);
        user.setCreateTime(LocalDateTime.now());
        user.setDelFlag(0);
        user.setUpdateBy(null);
        user.setUpdateTime(LocalDateTime.now());
        user.setOrgId("1674391485808001024");

        return CollUtil.newArrayList(user);
    }

    private List<AnUserRole> getAnUserRoles() {
        AnUserRole userRole = new AnUserRole();
        userRole.setId("1674390418047348736");
        userRole.setRoleId("admin");
        userRole.setUserId("1666356287765979136");
        userRole.setCreateBy(null);
        userRole.setCreateTime(LocalDateTime.now());
        userRole.setDelFlag(0);
        userRole.setUpdateBy(null);
        userRole.setUpdateTime(LocalDateTime.now());
        return CollUtil.newArrayList(userRole);
    }


}
