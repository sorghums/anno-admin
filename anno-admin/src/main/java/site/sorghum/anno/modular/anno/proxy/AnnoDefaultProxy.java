package site.sorghum.anno.modular.anno.proxy;

import jakarta.inject.Named;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;

import java.util.Collection;
import java.util.List;

/**
 * Anno代理默认实现类
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Named
public class AnnoDefaultProxy<T> implements AnnoBaseProxy<T> {
    @Override
    public void beforeAdd(TableParam<T> tableParam, T data) {

    }

    @Override
    public void afterAdd(T data) {

    }

    @Override
    public void beforeUpdate(TableParam<T> tableParam, List<DbCondition> dbConditions, T data) {

    }

    @Override
    public void afterUpdate(T data) {

    }

    @Override
    public void beforeDelete(TableParam<T> tableParam, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }

    @Override
    public void beforeFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void afterFetch(Collection<T> dataList) {

    }
}
