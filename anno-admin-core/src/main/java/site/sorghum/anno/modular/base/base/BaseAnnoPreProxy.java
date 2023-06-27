package site.sorghum.anno.modular.base.base;

import cn.hutool.core.util.IdUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;

import java.time.LocalDateTime;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Component
public class BaseAnnoPreProxy extends AnnoPreBaseProxy<BaseMetaModel> {
    @Inject
    AnnoService annoService;

    @Override
    public void beforeAdd(BaseMetaModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
        data.setDelFlag(0);
        data.setCreateTime(LocalDateTime.now());
        data.setUpdateTime(LocalDateTime.now());
        super.beforeAdd(data);
    }

    @Override
    public void beforeUpdate(BaseMetaModel data) {
        data.setUpdateTime(LocalDateTime.now());
        super.beforeUpdate(data);
    }

}
