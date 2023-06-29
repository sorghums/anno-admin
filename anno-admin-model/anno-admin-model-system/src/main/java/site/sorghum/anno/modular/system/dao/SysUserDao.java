package site.sorghum.anno.modular.system.dao;

import org.noear.wood.BaseMapper;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.modular.system.anno.SysUser;

/**
 * 系统用户
 *
 * @author Sorghum
 * @since 2023/06/29
 */
@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysUserDao extends BaseMapper<SysUser> {

}