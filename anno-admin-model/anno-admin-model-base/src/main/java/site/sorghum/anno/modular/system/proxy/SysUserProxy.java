package site.sorghum.anno.modular.system.proxy;

import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.wood.DbTableQuery;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.system.anno.SysUser;

import java.io.Serializable;
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
    public void beforeAdd(SysUser data) {
        if (StrUtil.isBlank(data.getMobile())) {
            throw new BizException("新增用户手机号不能为空");
        }
        if (StrUtil.isBlank(data.getPassword())) {
            throw new BizException("新增用户密码不能为空");
        }
        // 重新设置密码
        MD5 md5 = MD5.create();
        data.setPassword(md5.digestHex(data.getMobile() + ":" + data.getPassword()));
        super.beforeAdd(data);
    }

    @Override
    public void afterAdd(SysUser data) {
        super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(SysUser data) {
        super.beforeUpdate(data);
    }

    @Override
    public void afterUpdate(SysUser data) {
        super.afterUpdate(data);
    }

    @Override
    public void beforeDelete(Serializable id) {
        super.beforeDelete(id);
    }

    @Override
    public void beforeDelete(List<Tuple> tuples) {
        super.beforeDelete(tuples);
    }

    @Override
    public void afterDelete(Serializable id) {
        super.afterDelete(id);
    }

    @Override
    public void afterDelete(List<Tuple> tuples) {
        super.afterDelete(tuples);
    }

    @Override
    public void beforeFetch(DbTableQuery dbTableQuery) {
        super.beforeFetch(dbTableQuery);
    }

    @Override
    public void afterFetch(Collection<SysUser> dataList) {
        super.afterFetch(dataList);
    }
}
