package site.sorghum.anno.modular.system.dao;

import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.modular.system.anno.SysPermission;
import site.sorghum.anno.modular.system.anno.SysUser;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysPermissionDao extends BaseMapper<SysPermission> {
    /**
     * 根据用户id查询系统权限
     *
     * @param uid 用户id
     * @return {@link List}<{@link SysPermission}>
     */
    @Sql("select * from sys_permission where id in (select permission_id from sys_role_permission where role_id in (select role_id from sys_user_role where user_id = ?  and del_flag = 0 ) and del_flag = 0)")
    List<SysPermission> querySysPermissionByUserId(String uid);

    /**
     * 列表
     *
     * @return {@link List}<{@link SysPermission}>
     */
    @Sql("select * from sys_permission where del_flag = 0")
    List<SysPermission> list();
}