package site.sorghum.anno.suppose.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.plugin.manager.AnnoOrgManager;
import site.sorghum.anno.suppose.model.BaseOrgMetaModel;

import java.util.List;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Named
@Slf4j
public class BaseOrgAnnoPreProxy implements AnnoBaseProxy<BaseOrgMetaModel> {

    @Inject
    BaseAnnoPreProxy baseAnnoPreProxy;

    @Inject
    AnnoOrgManager annoOrgManager;

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
    public void beforeFetch(DbCriteria criteria) {
        if (annoOrgManager.isIgnoreFilter(criteria.getEntityName())){
            return;
        }
        String orgId = annoOrgManager.getLoginOrg();
        criteria.eq("org_id", orgId);
    }

}
