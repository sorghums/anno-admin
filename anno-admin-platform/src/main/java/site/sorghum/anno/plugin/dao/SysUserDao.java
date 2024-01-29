package site.sorghum.anno.plugin.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jdk.jfr.Name;
import org.noear.wood.utils.StringUtils;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.ao.AnUser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Named
public class SysUserDao implements AnnoBaseDao<AnUser> {

    @Inject
    DbService dbService;

    @Inject
    MetadataManager metadataManager;


    /**
     * 根据用户手机号查询用户
     *
     * @param mobile 手机号
     * @return {@link AnUser}
     */
    public AnUser queryByMobile(String mobile) {
        return sqlOne("SELECT * FROM an_user WHERE mobile = ? and del_flag = 0 limit 1", mobile);
    }

    /**
     * 选择用户列表
     *
     * @param userIds 用户ID
     * @return {@link List}<{@link AnUser}>
     */
    public List<AnUser> selectUserList(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return new LinkedList<>();
        }
        List<String> collect = Arrays.stream(userIds.split(",")).collect(Collectors.toList());
        return findByIds(collect);
    }

    @Override
    public DbService dbService() {
        return dbService;
    }

    ;

    @Override
    public MetadataManager metadataManager() {
        return metadataManager;
    }

    @Override
    public Class<AnUser> entityClass() {
        return AnUser.class;
    }
}