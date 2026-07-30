package site.sorghum.anno.plugin.dao;

import jakarta.inject.Named;
import org.noear.wood.utils.StringUtils;
import site.sorghum.anno.db.dao.AnnoBaseDao;
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
public class AnUserDao implements AnnoBaseDao<AnUser> {

    /**
     * 只更新用户密码，避免部分实体更新时清空其他字段。
     */
    public void updatePassword(String userId, String password) {
        dbService().executeSql(
            "UPDATE an_user SET password = ? WHERE id = ? AND del_flag = 0",
            password,
            userId
        );
    }

    /**
     * 根据用户手机号查询用户
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
}