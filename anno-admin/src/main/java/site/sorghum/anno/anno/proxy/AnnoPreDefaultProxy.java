package site.sorghum.anno.anno.proxy;

import jakarta.inject.Named;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;

import java.util.List;

/**
 * Anno代理默认实现类
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Named
public class AnnoPreDefaultProxy<T> implements AnnoPreBaseProxy<T> {
    @Override
    public void beforeAdd(T data) {

    }

    @Override
    public void afterAdd(T data) {

    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, T data) {

    }

    @Override
    public void afterUpdate(T data) {

    }


    @Override
    public void beforeDelete(Class<T> tClass, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(Class<T> tClass, List<DbCondition> dbConditions) {

    }

    @Override
    public void beforeFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void afterFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page) {

    }
}
