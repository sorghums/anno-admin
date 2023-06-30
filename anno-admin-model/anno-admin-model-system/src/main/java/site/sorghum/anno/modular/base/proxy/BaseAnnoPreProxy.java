package site.sorghum.anno.modular.base.proxy;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.base.model.BaseMetaModel;
import site.sorghum.anno.modular.system.anno.SysUser;

import java.time.LocalDateTime;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Component
public class BaseAnnoPreProxy extends AnnoPreBaseProxy<BaseMetaModel> {

    @Override
    public void beforeAdd(BaseMetaModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
        data.setDelFlag(0);
        data.setCreateTime(LocalDateTime.now());
        data.setUpdateTime(LocalDateTime.now());
        data.setCreateBy(getLoginName());
        super.beforeAdd(data);
    }

    @Override
    public void beforeUpdate(BaseMetaModel data) {
        data.setUpdateTime(LocalDateTime.now());
        data.setUpdateBy(getLoginName());
        super.beforeUpdate(data);
    }

    private String getLoginName() {
        try {
            SaSession session = StpUtil.getSession(false);
            SysUser sysUser = session.get("user", new SysUser() {{
                setName("system");
            }});
            return sysUser.getName();
        } catch (Exception e) {
            return "system";
        }
    }

}
