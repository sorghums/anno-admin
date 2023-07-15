package site.sorghum.anno.modular.base.proxy;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.common.util.AnnoContextUtil;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.base.model.BaseMetaModel;
import site.sorghum.anno.modular.system.anno.SysUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Component
@Slf4j
public class BaseAnnoPreProxy extends AnnoPreBaseProxy<BaseMetaModel> {

    @Override
    public void beforeFetch(TableParam<BaseMetaModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        log.info("网络请求参数：{}", AnnoContextUtil.getContext().getRequestParams());
    }

    @Override
    public void beforeAdd(TableParam<BaseMetaModel> tableParam,BaseMetaModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
        data.setDelFlag(0);
        data.setCreateTime(LocalDateTime.now());
        data.setUpdateTime(LocalDateTime.now());
        data.setCreateBy(getLoginName());
        super.beforeAdd(tableParam,data);
    }

    @Override
    public void beforeUpdate(TableParam<BaseMetaModel> tableParam, List<DbCondition> dbConditions, BaseMetaModel data) {
        data.setUpdateTime(LocalDateTime.now());
        data.setUpdateBy(getLoginName());
        super.beforeUpdate(tableParam, dbConditions, data);
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
