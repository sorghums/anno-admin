package site.sorghum.anno.modular.system.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.common.util.MD5Util;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.system.ao.SysUser;

import java.util.Collection;
import java.util.List;

/**
 * 系统用户代理
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@Named
public class SysUserProxy implements AnnoBaseProxy<SysUser> {
    @Override
    public void beforeAdd(TableParam<SysUser> tableParam,SysUser data) {
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
    public void afterAdd(SysUser data) {
    }

    @Override
    public void beforeUpdate(TableParam<SysUser> tableParam, List<DbCondition> dbConditions, SysUser data) {
    }

    @Override
    public void afterUpdate(SysUser data) {
    }

    @Override
    public void beforeDelete(TableParam<SysUser> tableParam, List<DbCondition> dbConditions) {
    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {
    }

    @Override
    public void beforeFetch(TableParam<SysUser> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
    }

    @Override
    public void afterFetch(Collection<SysUser> dataList) {
    }
}
