package site.sorghum.anno.pre.suppose.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.manager.AnnoOrgManager;
import site.sorghum.anno.pre.suppose.model.BaseOrgMetaModel;

import java.util.List;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Named
@Slf4j
public class BaseOrgAnnoPreProxy implements AnnoPreBaseProxy<BaseOrgMetaModel> {

    @Inject
    BaseAnnoPreProxy baseAnnoPreProxy;

    @Inject
    AnnoOrgManager annoOrgManager;

    @Override
    public void beforeAdd(TableParam<BaseOrgMetaModel> tableParam, BaseOrgMetaModel data) {
        if (!annoOrgManager.isIgnoreFilter(tableParam.getClazz())) {
            if (!annoOrgManager.getLoginOrg().equals(data.getOrgId())) {
                throw new BizException("非法操作, 无法为其他组织添加数据。");
            }
        }
        baseAnnoPreProxy.beforeAdd(null, data);
        // 如果没有设置组织ID则设置当前登录组织ID
        if (data.getOrgId() == null) {
            data.setOrgId(annoOrgManager.getLoginOrg());
        }
    }

    @Override
    public void beforeFetch(TableParam<BaseOrgMetaModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        if (annoOrgManager.isIgnoreFilter(tableParam.getClazz())){
            return;
        }
        String orgId = annoOrgManager.getLoginOrg();
        dbConditions.add(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "org_id", orgId));
    }

    @Override
    public void afterAdd(BaseOrgMetaModel data) {

    }

    @Override
    public void beforeUpdate(TableParam<BaseOrgMetaModel> tableParam, List<DbCondition> dbConditions, BaseOrgMetaModel data) {

    }

    @Override
    public void afterUpdate(BaseOrgMetaModel data) {

    }

    @Override
    public void beforeDelete(TableParam<BaseOrgMetaModel> tableParam, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }

    @Override
    public void afterFetch(TableParam<BaseOrgMetaModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<BaseOrgMetaModel> page) {

    }

}
