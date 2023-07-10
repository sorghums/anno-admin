package site.sorghum.anno.modular.system.proxy;

import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.ProxyComponent;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.util.MD5Util;

import java.util.Collection;
import java.util.List;

/**
 * 系统用户代理
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@ProxyComponent
public class SysUserProxy extends AnnoBaseProxy<SysUser> {
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
        super.beforeAdd(tableParam,data);
    }

    @Override
    public void afterAdd(SysUser data) {
        super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(TableParam<SysUser> tableParam, List<DbCondition> dbConditions, SysUser data) {
        super.beforeUpdate(tableParam, dbConditions, data);
    }

    @Override
    public void afterUpdate(SysUser data) {
        super.afterUpdate(data);
    }

    @Override
    public void beforeDelete(TableParam<SysUser> tableParam, List<DbCondition> dbConditions) {
        super.beforeDelete(tableParam, dbConditions);
    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {
        super.afterDelete(dbConditions);
    }

    @Override
    public void beforeFetch(TableParam<SysUser> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        super.beforeFetch(tableParam, dbConditions, pageParam);
    }

    @Override
    public void afterFetch(Collection<SysUser> dataList) {
        super.afterFetch(dataList);
    }
}
