package site.sorghum.anno.anno.dami;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;

import java.util.List;

/**
 * 一个示例：所有实体代理的空实现
 *
 * @author songyinyin
 * @since 2023/10/7 17:50
 */
@Named
@Slf4j
public class AllEntityProxy<T> implements AnnoBaseProxy<T> {
    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(Object.class)
        };
    }

    @Override
    public int index() {
        return AnnoBaseProxy.super.index();
    }

    @Override
    public void beforeAdd(T data) {
        AnnoBaseProxy.super.beforeAdd(data);
    }

    @Override
    public void afterAdd(T data) {
        AnnoBaseProxy.super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, T data) {
        AnnoBaseProxy.super.beforeUpdate(dbConditions, data);
    }

    @Override
    public void afterUpdate(T data) {
        AnnoBaseProxy.super.afterUpdate(data);
    }

    @Override
    public void beforeDelete(Class<T> tClass, List<DbCondition> dbConditions) {
        AnnoBaseProxy.super.beforeDelete(tClass, dbConditions);
    }

    @Override
    public void afterDelete(Class<T> tClass, List<DbCondition> dbConditions) {
        AnnoBaseProxy.super.afterDelete(tClass, dbConditions);
    }

    @Override
    public void beforeFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam) {
        AnnoBaseProxy.super.beforeFetch(tClass, dbConditions, pageParam);
    }

    @Override
    public void afterFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page) {
        AnnoBaseProxy.super.afterFetch(tClass, dbConditions, pageParam, page);
    }
}
