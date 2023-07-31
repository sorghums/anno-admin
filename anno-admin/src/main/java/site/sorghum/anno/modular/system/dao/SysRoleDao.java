package site.sorghum.anno.modular.system.dao;

import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.modular.system.ao.SysRole;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysRoleDao extends BaseMapper<SysRole> {
    /**
     * 通过用户id查询系统角色
     *
     * @param uid 用户id
     * @return {@link List}<{@link SysRole}>
     */
    @Sql("select * from sys_role where id in (select role_id from sys_user_role where user_id = ? and del_flag = 0 ) and del_flag = 0")
    List<SysRole> querySysRoleByUserId(String uid);
}