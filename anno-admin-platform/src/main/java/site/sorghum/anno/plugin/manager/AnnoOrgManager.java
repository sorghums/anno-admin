package site.sorghum.anno.plugin.manager;

import cn.dev33.satoken.session.SaSession;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.ao.AnUser;

import java.util.List;

@Named
public class AnnoOrgManager {

    @Inject
    DbServiceWithProxy dbServiceWithProxy;

    @Inject
    MetadataManager metadataManager;

    @Db
    DbContext dbContext;


    public String getLoginOrg() {
        try {
            AnnoAuthUser authUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getLoginId());
            if (authUser == null || authUser.getOrgId() == null){
                throw new BizException("获取用户组织失败,请先绑定组织或者点击右上角清除缓存。");
            }
            return authUser.getOrgId();
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

    public boolean isIgnoreFilter(Class<?> clazz) {
        try {
            String loginId = AnnoStpUtil.getLoginId("-1");
            List<String> roleIds = dbContext.table("an_user_role").where("user_id=?", loginId).selectArray("role_id");
            boolean isAdmin = roleIds.stream().anyMatch("admin"::equals);
            AnEntity entity = metadataManager.getEntity(clazz);
            boolean orgFilter = entity.isOrgFilter();
            // admin 或者 不需要过滤 则不过滤
            return !orgFilter || isAdmin;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}
