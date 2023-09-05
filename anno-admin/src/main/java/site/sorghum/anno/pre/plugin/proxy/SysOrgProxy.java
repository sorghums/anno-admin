package site.sorghum.anno.pre.plugin.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.ao.SysOrg;
import site.sorghum.anno.pre.plugin.manager.AnnoOrgManager;

import java.util.List;

@Named
public class SysOrgProxy  implements AnnoBaseProxy<SysOrg> {

    @Inject
    AnnoOrgManager annoOrgManager;

    @Override
    public void beforeFetch(TableParam<SysOrg> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        if (annoOrgManager.isIgnoreFilter(tableParam.getClazz())){
            return;
        }
        String orgId = annoOrgManager.getLoginOrg();
        dbConditions.add(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "id", orgId));
    }

    @Override
    public void beforeAdd(TableParam<SysOrg> tableParam, SysOrg data) {
        if (!annoOrgManager.isIgnoreFilter(tableParam.getClazz())) {
            throw new BizException("非管理员无法添加组织。");
        }
    }

}
