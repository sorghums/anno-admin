package site.sorghum.anno.plugin.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.plugin.ao.AnOrg;
import site.sorghum.anno.plugin.manager.AnnoOrgManager;

@Named
public class AnOrgProxy implements AnnoBaseProxy<AnOrg> {

    @Inject
    AnnoOrgManager annoOrgManager;

    @Override
    public void beforeFetch(DbCriteria criteria) {
        if (annoOrgManager.isIgnoreFilter(criteria.getEntityName())) {
            return;
        }
//        String orgId = annoOrgManager.getLoginOrg();
//        dbConditions.add(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "id", orgId));
    }

    @Override
    public void beforeAdd(AnOrg data) {
        if (!annoOrgManager.isIgnoreFilter(AnOrg.class)) {
            throw new BizException("非管理员无法添加组织。");
        }
    }

}
