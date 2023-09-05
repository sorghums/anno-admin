package site.sorghum.anno.pre.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.suppose.model.PrimaryKeyModel;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/8/18 19:08
 */
public class PrimaryKeyPreProxy implements AnnoPreBaseProxy<PrimaryKeyModel> {
    @Override
    public void beforeAdd(TableParam<PrimaryKeyModel> tableParam, PrimaryKeyModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
    }

    @Override
    public void afterAdd(PrimaryKeyModel data) {

    }

    @Override
    public void beforeUpdate(TableParam<PrimaryKeyModel> tableParam, List<DbCondition> dbConditions, PrimaryKeyModel data) {

    }

    @Override
    public void afterUpdate(PrimaryKeyModel data) {

    }

    @Override
    public void beforeDelete(TableParam<PrimaryKeyModel> tableParam, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }

    @Override
    public void beforeFetch(TableParam<PrimaryKeyModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void afterFetch(TableParam<PrimaryKeyModel> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<PrimaryKeyModel> page) {

    }
}
