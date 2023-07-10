package site.sorghum.anno.modular.anno.proxy;

import org.noear.solon.annotation.Component;
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
@Component
public class AnnoPreDefaultProxy<T> extends AnnoPreBaseProxy<T>{
    @Override
    public void beforeAdd(TableParam<T> tableParam, T data) {
        super.beforeAdd(tableParam, data);
    }

    @Override
    public void afterAdd(T data) {
        super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(TableParam<T> tableParam, List<DbCondition> dbConditions, T data) {
        super.beforeUpdate(tableParam, dbConditions, data);
    }

    @Override
    public void afterUpdate(T data) {
        super.afterUpdate(data);
    }


    @Override
    public void beforeDelete(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        super.beforeDelete(tableParam, dbConditions);
    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {
        super.afterDelete(dbConditions);
    }

    @Override
    public void beforeFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        super.beforeFetch(tableParam, dbConditions, pageParam);
    }

    @Override
    public void afterFetch(Collection<T> dataList) {
        super.afterFetch(dataList);
    }
}
