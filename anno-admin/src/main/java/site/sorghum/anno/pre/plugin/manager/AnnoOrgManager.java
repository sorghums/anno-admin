package site.sorghum.anno.pre.plugin.manager;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.ao.AnUser;
import site.sorghum.anno.pre.plugin.ao.AnUserRole;

import java.util.List;

@Named
public class AnnoOrgManager {

    @Inject
    DbServiceWithProxy dbServiceWithProxy;

    @Inject
    MetadataManager metadataManager;


    public String getLoginOrg() {
        try {
            SaSession session = StpUtil.getSession(false);
            AnUser anUser = session.get("user", new AnUser() {{
                setName("system");
                setOrgId("-1");
            }});
            if (anUser.getOrgId() == null || anUser.getOrgId().equals("-1")) {
                throw new BizException("获取用户组织失败,请先绑定组织或者点击右上角清除缓存。");
            }
            return anUser.getOrgId();
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

    public boolean isIgnoreFilter(Class<?> clazz) {
        try {
            String loginId = (String) StpUtil.getLoginId();
            List<AnUserRole> list = dbServiceWithProxy.list(AnUserRole.class, CollUtil.newArrayList(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "user_id", loginId)));
            boolean isAdmin = list.stream().anyMatch(
                anUserRole -> "admin".equals(anUserRole.getRoleId())
            );
            AnEntity entity = metadataManager.getEntity(clazz);
            boolean orgFilter = entity.isOrgFilter();
            // admin 或者 不需要过滤 则不过滤
            return !orgFilter || isAdmin;
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }
}
