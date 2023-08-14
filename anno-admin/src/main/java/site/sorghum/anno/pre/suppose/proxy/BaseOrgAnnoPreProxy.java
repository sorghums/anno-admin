package site.sorghum.anno.pre.suppose.proxy;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.plugin.ao.SysUserRole;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;
import site.sorghum.anno.pre.suppose.model.BaseOrgMetaModel;

import java.time.LocalDateTime;
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
    DbServiceWithProxy dbServiceWithProxy;

    @Inject
    MetadataManager metadataManager;

    @Override
    public void beforeAdd(TableParam<BaseOrgMetaModel> tableParam, BaseOrgMetaModel data) {
        if (!isIgnoreFilter(tableParam.getClazz())) {
            if (!getLoginOrg().equals(data.getOrgId())) {
                throw new BizException("非法操作, 无法为其他组织添加数据。");
            }
        }
        baseAnnoPreProxy.beforeAdd(null, data);
        // 如果没有设置组织ID则设置当前登录组织ID
        if (data.getOrgId() == null) {
            data.setOrgId(getLoginOrg());
        }
    }

    @Override
    public void beforeFetch(TableParam<BaseOrgMetaModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        if (isIgnoreFilter(tableParam.getClazz())){
            return;
        }
        String orgId = getLoginOrg();
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
    public void afterFetch(AnnoPage<BaseOrgMetaModel> page) {

    }

    private String getLoginOrg() {
        try {
            SaSession session = StpUtil.getSession(false);
            SysUser sysUser = session.get("user", new SysUser() {{
                setName("system");
                setOrgId("-1");
            }});
            if (sysUser.getOrgId() == null || sysUser.getOrgId().equals("-1")) {
                throw new BizException("获取用户组织失败,请先绑定组织或者点击右上角清除缓存。");
            }
            return sysUser.getOrgId();
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

    private boolean isIgnoreFilter(Class<?> clazz) {
        try {
            String loginId = (String) StpUtil.getLoginId();
            TableParam<SysUserRole> tableParam = metadataManager.getTableParam(SysUserRole.class);
            List<SysUserRole> list = dbServiceWithProxy.list(tableParam, CollUtil.newArrayList(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "user_id", loginId)));
            boolean isAdmin = list.stream().anyMatch(
                sysUserRole -> sysUserRole.getRoleId().equals("admin")
            );
            AnEntity entity = metadataManager.getEntity(clazz);
            boolean orgFilter = entity.isOrgFilter();
            // admin 或者 不需要过滤 则不过滤
            return orgFilter || isAdmin;
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

}
