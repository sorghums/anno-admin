package site.sorghum.anno.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.suppose.model.PrimaryKeyModel;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/8/18 19:08
 */
public class PrimaryKeyPreProxy implements AnnoPreBaseProxy<PrimaryKeyModel> {
    @Override
    public void beforeAdd(PrimaryKeyModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
    }

    @Override
    public void afterAdd(PrimaryKeyModel data) {

    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, PrimaryKeyModel data) {

    }

    @Override
    public void afterUpdate(PrimaryKeyModel data) {

    }

    @Override
    public void beforeDelete(Class<PrimaryKeyModel> tClass, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(Class<PrimaryKeyModel> tClass, List<DbCondition> dbConditions) {

    }

    @Override
    public void beforeFetch(Class<PrimaryKeyModel> tClass, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void afterFetch(Class<PrimaryKeyModel> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<PrimaryKeyModel> page) {

    }
}
