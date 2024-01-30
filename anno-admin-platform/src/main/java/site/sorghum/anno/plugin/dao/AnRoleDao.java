package site.sorghum.anno.plugin.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.ao.AnRole;

import java.util.List;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Named
public class AnRoleDao implements AnnoBaseDao<AnRole> {

    /**
     * 通过用户id查询系统角色
     *
     * @param uid 用户id
     * @return {@link List}<{@link AnRole}>
     */
    public List<AnRole> querySysRoleByUserId(String uid) {
        return sqlList(
            "select * from an_role where id in (select role_id from an_user_role where user_id = ? and del_flag = 0 ) and del_flag = 0",
            uid
        );
    }
}