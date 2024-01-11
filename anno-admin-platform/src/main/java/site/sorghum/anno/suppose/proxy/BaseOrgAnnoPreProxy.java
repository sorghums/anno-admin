package site.sorghum.anno.suppose.proxy;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.plugin.manager.AnnoOrgManager;
import site.sorghum.anno.suppose.model.BaseOrgMetaModel;

import java.util.List;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Component
@Slf4j
@org.springframework.stereotype.Component
public class BaseOrgAnnoPreProxy implements AnnoBaseProxy<BaseOrgMetaModel> {

    @Inject
    @Autowired
    BaseAnnoPreProxy baseAnnoPreProxy;

    @Inject
    @Autowired
    AnnoOrgManager annoOrgManager;

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(BaseOrgMetaModel.class)
        };
    }

    @Override
    public void beforeAdd(BaseOrgMetaModel data) {
        if (!annoOrgManager.isIgnoreFilter(data.getClass())) {
            if (!annoOrgManager.getLoginOrg().equals(data.getOrgId())) {
                throw new BizException("非法操作, 无法为其他组织添加数据。");
            }
        }
        baseAnnoPreProxy.beforeAdd(data);
        // 如果没有设置组织ID则设置当前登录组织ID
        if (data.getOrgId() == null) {
            data.setOrgId(annoOrgManager.getLoginOrg());
        }
    }

    @Override
    public void beforeFetch(Class<BaseOrgMetaModel> tClass, List<DbCondition> dbConditions, PageParam pageParam) {
        if (annoOrgManager.isIgnoreFilter(tClass)){
            return;
        }
        String orgId = annoOrgManager.getLoginOrg();
        dbConditions.add(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "org_id", orgId));
    }

}
