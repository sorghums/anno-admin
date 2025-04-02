package site.sorghum.anno.plugin.dao;

import jakarta.inject.Named;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.plugin.ao.AnPermission;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Named
public class AnPermissionDao implements AnnoBaseDao<AnPermission> {

    /**
     * 根据用户id查询系统权限
     *
     * @param uid 用户id
     * @return {@link List}<{@link AnPermission}>
     */
    public List<AnPermission> querySysPermissionByUserId(String uid){
        return this.sqlList(
            "select * from an_permission where id in (select permission_id from an_role_permission where role_id in (select role_id from an_user_role where user_id = ?  and del_flag = 0 ) and del_flag = 0)",
            uid
        );
    }

    /**
     * 列表
     *
     * @return {@link List}<{@link AnPermission}>
     */
    public List<AnPermission> bizList(){
        return this.sqlList("select * from an_permission where del_flag = 0");
    }

    /**
     * 按代码选择
     *
     * @param code 密码
     * @return {@link AnPermission}
     */
    public AnPermission selectByCode(String code){
        return this.sqlOne("select * from an_permission where code = ? and del_flag = 0", code);
    }
}