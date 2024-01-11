package site.sorghum.anno.plugin.proxy;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import org.springframework.beans.factory.annotation.Autowired;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.plugin.ao.AnOrg;
import site.sorghum.anno.plugin.manager.AnnoOrgManager;

import java.util.List;

@Component
@org.springframework.stereotype.Component
public class AnOrgProxy implements AnnoBaseProxy<AnOrg> {

    @Inject
    @Autowired
    AnnoOrgManager annoOrgManager;

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(AnOrg.class)
        };
    }
    
    @Override
    public void beforeFetch(Class<AnOrg> tClass, List<DbCondition> dbConditions, PageParam pageParam) {
        if (annoOrgManager.isIgnoreFilter(tClass)){
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
