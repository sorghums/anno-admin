package site.sorghum.anno.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.plugin.ao.AnRole;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface AnRoleDao extends AnnoBaseMapper<AnRole> {
    /**
     * 通过用户id查询系统角色
     *
     * @param uid 用户id
     * @return {@link List}<{@link AnRole}>
     */
    @Sql("select * from an_role where id in (select role_id from an_user_role where user_id = ? and del_flag = 0 ) and del_flag = 0")
    List<AnRole> querySysRoleByUserId(String uid);
}