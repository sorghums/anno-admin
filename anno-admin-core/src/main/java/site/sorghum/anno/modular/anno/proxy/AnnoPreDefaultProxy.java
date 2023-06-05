package site.sorghum.anno.modular.anno.proxy;

import org.noear.solon.annotation.Component;
import org.noear.wood.DbTableQuery;

import java.io.Serializable;
import java.util.Collection;

/**
 * Anno代理默认实现类
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Component
public class AnnoPreDefaultProxy<T> extends AnnoPreBaseProxy<T>{
    @Override
    public void beforeAdd(T data) {
        super.beforeAdd(data);
    }

    @Override
    public void afterAdd(T data) {
        super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(T data) {
        super.beforeUpdate(data);
    }

    @Override
    public void afterUpdate(T data) {
        super.afterUpdate(data);
    }

    @Override
    public void beforeDelete(Serializable id) {
        super.beforeDelete(id);
    }

    @Override
    public void afterDelete(Serializable id) {
        super.afterDelete(id);
    }

    @Override
    public void beforeFetch(DbTableQuery dbTableQuery) {
        super.beforeFetch(dbTableQuery);
    }

    @Override
    public void afterFetch(Collection<T> dataList) {
        super.afterFetch(dataList);
    }
}
