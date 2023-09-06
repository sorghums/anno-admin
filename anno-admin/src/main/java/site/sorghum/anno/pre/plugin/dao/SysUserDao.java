package site.sorghum.anno.pre.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.utils.StringUtils;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.pre.plugin.ao.AnUser;
import site.sorghum.anno.pre.suppose.mapper.AnnoBaseMapper;

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
@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysUserDao extends AnnoBaseMapper<AnUser> {

    /**
     * 根据用户手机号查询用户
     * @param mobile 手机号
     * @return {@link AnUser}
     */
    @Sql("SELECT * FROM an_user WHERE mobile = ? and del_flag = 0 limit 1")
    AnUser queryByMobile(String mobile);

    default List<AnUser> selectUserList(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return new LinkedList<>();
        }
        List<String> collect = Arrays.stream(userIds.split(",")).collect(Collectors.toList());
        return selectList(m -> m.whereIn("user_id", collect));
    }
}