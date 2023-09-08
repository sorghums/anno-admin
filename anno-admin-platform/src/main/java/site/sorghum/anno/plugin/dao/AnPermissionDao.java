package site.sorghum.anno.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.plugin.ao.AnPermission;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface AnPermissionDao extends AnnoBaseMapper<AnPermission> {
    /**
     * 根据用户id查询系统权限
     *
     * @param uid 用户id
     * @return {@link List}<{@link AnPermission}>
     */
    @Sql("select * from an_permission where id in (select permission_id from an_role_permission where role_id in (select role_id from an_user_role where user_id = ?  and del_flag = 0 ) and del_flag = 0)")
    List<AnPermission> querySysPermissionByUserId(String uid);

    /**
     * 列表
     *
     * @return {@link List}<{@link AnPermission}>
     */
    @Sql("select * from an_permission where del_flag = 0")
    List<AnPermission> list();
}