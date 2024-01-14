package site.sorghum.anno.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.suppose.model.BaseMetaModel;

import java.time.LocalDateTime;

/**
 * 基础前置代理
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Named
@Slf4j
public class BaseAnnoPreProxy implements AnnoBaseProxy<BaseMetaModel> {

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(BaseMetaModel.class)
        };
    }


    @Override
    public void beforeFetch(DbCriteria criteria) {
        log.debug("网络请求参数：{}", AnnoContextUtil.getContext().getRequestParams());
    }

    @Override
    public void beforeAdd(BaseMetaModel data) {

        data.setId(IdUtil.getSnowflakeNextIdStr());
        data.setDelFlag(0);
        data.setCreateTime(LocalDateTime.now());
        data.setUpdateTime(LocalDateTime.now());
        data.setCreateBy(getLoginName());
    }

    @Override
    public void beforeUpdate(BaseMetaModel data, DbCriteria criteria) {
        data.setUpdateTime(LocalDateTime.now());
        data.setUpdateBy(getLoginName());
    }


    private String getLoginName() {
        try {
            AnnoAuthUser authUser = AnnoStpUtil.getAuthUser(AnnoStpUtil.getLoginId());
            if (authUser == null || authUser.getUserName() == null) {
                return null;
            }
            return authUser.getUserName();
        } catch (Exception e) {
            return "system";
        }
    }

}
