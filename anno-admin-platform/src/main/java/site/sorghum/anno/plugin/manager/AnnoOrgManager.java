package site.sorghum.anno.plugin.manager;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
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
            String loginId = StpUtil.getLoginId("-1");
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
