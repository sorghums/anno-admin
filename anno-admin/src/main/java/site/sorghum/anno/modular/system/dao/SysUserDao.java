package site.sorghum.anno.modular.system.dao;

import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.modular.system.ao.SysUser;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * 根据用户手机号查询用户
     * @param mobile 手机号
     * @return {@link SysUser}
     */
    @Sql("SELECT * FROM sys_user WHERE mobile = ? and del_flag = 0 limit 1")
    SysUser queryByMobile(String mobile);
}