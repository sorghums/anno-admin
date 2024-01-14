package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.dao.SysUserDao;

import java.util.List;

/**
 * 系统用户代理
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@Named
public class AnUserProxy implements AnnoBaseProxy<AnUser> {

    @Db
    SysUserDao sysUserDao;
    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(AnUser.class)
        };
    }
    @Override
    public void beforeAdd(AnUser data) {
        if (StrUtil.isBlank(data.getMobile())) {
            throw new BizException("新增用户手机号不能为空");
        }
        if (StrUtil.isBlank(data.getPassword())) {
            throw new BizException("新增用户密码不能为空");
        }
        // 重新设置密码
        data.setPassword(MD5Util.digestHex(data.getMobile() + ":" + data.getPassword()));
    }

    @Override
    public void beforeUpdate(AnUser data, DbCriteria criteria) {
        // 根据ID查询用户
        AnUser anUser = sysUserDao.findById(data.getId()).orElseThrow(() -> new BizException("用户不存在"));
        if (StrUtil.isNotBlank(data.getPassword())) {
            // 重新设置密码
            data.setPassword(MD5Util.digestHex(anUser.getMobile() + ":" + data.getPassword()));
        }
    }

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<AnUser> page) {
        page.getList().forEach(
                anUser -> {
                    anUser.setPassword("");
                }
        );
    }
}
