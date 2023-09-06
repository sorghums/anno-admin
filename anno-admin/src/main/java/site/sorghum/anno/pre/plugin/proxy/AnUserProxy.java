package site.sorghum.anno.pre.plugin.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.ao.AnUser;

/**
 * 系统用户代理
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@Named
public class AnUserProxy implements AnnoBaseProxy<AnUser> {
    @Override
    public void beforeAdd(TableParam<AnUser> tableParam, AnUser data) {
        if (StrUtil.isBlank(data.getMobile())) {
            throw new BizException("新增用户手机号不能为空");
        }
        if (StrUtil.isBlank(data.getPassword())) {
            throw new BizException("新增用户密码不能为空");
        }
        // 重新设置密码
        data.setPassword(MD5Util.digestHex(data.getMobile() + ":" + data.getPassword()));
    }

}
