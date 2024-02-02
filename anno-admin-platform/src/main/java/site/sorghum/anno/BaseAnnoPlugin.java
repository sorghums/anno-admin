package site.sorghum.anno;


import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.plugin.ao.*;
import site.sorghum.anno.plugin.service.AuthService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Named
public class BaseAnnoPlugin extends AnnoPlugin {
    @Inject
    @Named("dbServiceWood")
    DbService dbService;

    public BaseAnnoPlugin() {
        super("管理端插件", "包含B端用户，角色，组织，权限等。");
    }

    @Override
    public int runOrder() {
        return 100;
    }

    @Override
    public void run() {
        CheckPermissionFunction.permissionCheckFunction = (permissionCode) -> {
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
        menu1.setIcon("ant-design:bars-outlined");
        menu1.setPermissionId("");
        menu1.setParseType("");
        menu1.setParseData(null);

        AnAnnoMenu menu2 = new AnAnnoMenu();
        menu2.setId("11");
        menu2.setParentId("10");
        menu2.setTitle("菜单管理");
        menu2.setType(1);
        menu2.setSort(10005);
        menu2.setIcon("ant-design:bars-outlined");
        menu2.setPermissionId("an_anno_menu");
        menu2.setParseType("annoMain");
        menu2.setParseData("AnAnnoMenu");

        AnAnnoMenu menu3 = new AnAnnoMenu();
        menu3.setId("12");
        menu3.setParentId("10");
        menu3.setTitle("权限管理");
        menu3.setType(1);
        menu3.setSort(10006);
        menu3.setIcon("ant-design:bars-outlined");
        menu3.setPermissionId("an_permission");
        menu3.setParseType("annoMain");
        menu3.setParseData("AnPermission");

        AnAnnoMenu menu4 = new AnAnnoMenu();
        menu4.setId("13");
        menu4.setParentId("10");
        menu4.setTitle("用户管理");
        menu4.setType(1);
        menu4.setSort(10007);
        menu4.setIcon("ant-design:bars-outlined");
        menu4.setPermissionId("an_user");
        menu4.setParseType("annoMain");
        menu4.setParseData("AnUser");

        AnAnnoMenu menu5 = new AnAnnoMenu();
        menu5.setId("14");
        menu5.setParentId("10");
        menu5.setTitle("角色管理");
        menu5.setType(1);
        menu5.setSort(10008);
        menu5.setIcon("ant-design:bars-outlined");
        menu5.setPermissionId("an_role");
        menu5.setParseType("annoMain");
        menu5.setParseData("AnRole");

        AnAnnoMenu menu6 = new AnAnnoMenu();
        menu6.setId("15");
        menu6.setParentId("10");
        menu6.setTitle("组织管理");
        menu6.setType(1);
        menu6.setSort(10009);
        menu6.setIcon("ant-design:bars-outlined");
        menu6.setPermissionId("an_org");
        menu6.setParseType("annoMain");
        menu6.setParseData("AnOrg");

        AnAnnoMenu menu6_1 = new AnAnnoMenu();
        menu6_1.setId("15_1");
        menu6_1.setParentId("10");
        menu6_1.setTitle("脚本管理");
        menu6_1.setType(1);
        menu6_1.setSort(10010);
        menu6_1.setIcon("ant-design:bars-outlined");
        menu6_1.setPermissionId("an_sql");
        menu6_1.setParseType("annoMain");
        menu6_1.setParseData("AnSql");

        AnAnnoMenu menu7 = new AnAnnoMenu();
        menu7.setId("16");
        menu7.setParentId("10");
        menu7.setTitle("接口文档");
        menu7.setType(1);
        menu7.setSort(10011);
        menu7.setIcon("ant-design:bars-outlined");
        menu7.setPermissionId("an_api_doc");
        menu7.setParseType("iframe");
        menu7.setParseData("[[[apiServerUrl]]]/doc.html");

        AnAnnoMenu menu7_1 = new AnAnnoMenu();
        menu7.setId("16_1");
        menu7.setParentId("10");
        menu7.setTitle("登录图表");
        menu7.setType(1);
        menu7.setSort(10012);
        menu7.setIcon("ant-design:bars-outlined");
        menu7.setPermissionId("an_login_chart");
        menu7.setParseType("annoMain");
        menu7.setParseData("AnOnlineUser");

        AnAnnoMenu menu8 = new AnAnnoMenu();
        menu8.setId("20");
        menu8.setParentId("");
        menu8.setTitle("会话管理");
        menu8.setType(0);
        menu8.setSort(9000);
        menu8.setIcon("ant-design:bars-outlined");
        menu8.setPermissionId(null);
        menu8.setParseType("");
        menu8.setParseData(null);

        AnAnnoMenu menu9 = new AnAnnoMenu();
        menu9.setId("21");
        menu9.setParentId("20");
        menu9.setTitle("在线用户");
        menu9.setType(1);
        menu9.setSort(9001);
        menu9.setIcon("ant-design:bars-outlined");
        menu9.setPermissionId("an_online_user");
        menu9.setParseType("annoMain");
        menu9.setParseData("AnOnlineUser");

        AnAnnoMenu menu10 = new AnAnnoMenu();
        menu10.setId("22");
        menu10.setParentId("20");
        menu10.setTitle("登录日志");
        menu10.setType(1);
        menu10.setSort(9002);
        menu10.setIcon("ant-design:bars-outlined");
        menu10.setPermissionId("an_login_log");
        menu10.setParseType("annoMain");
        menu10.setParseData("AnLoginLog");

        return CollUtil.newArrayList(
            menu1,
            menu2,
            menu3,
            menu4,
            menu5,
            menu6,
            menu6_1,
            menu7,
            menu8,
            menu9,
            menu10
        );
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
