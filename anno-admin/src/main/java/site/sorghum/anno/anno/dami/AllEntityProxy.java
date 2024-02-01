package site.sorghum.anno.anno.dami;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.plugin.join.util.InvokeUtil;

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
    public void beforeAdd(T data) {
        AnnoBaseProxy.super.beforeAdd(data);
    }

    @Override
    public void afterAdd(T data) {
        AnnoBaseProxy.super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(T data, DbCriteria criteria) {
        AnnoBaseProxy.super.beforeUpdate(data, criteria);
    }

    @Override
    public void afterUpdate(T data) {
        AnnoBaseProxy.super.afterUpdate(data);
    }

    @Override
    public void beforeDelete(DbCriteria criteria) {
        AnnoBaseProxy.super.beforeDelete(criteria);
    }

    @Override
    public void afterDelete(DbCriteria criteria) {
        AnnoBaseProxy.super.afterDelete(criteria);
    }

    @Override
    public void beforeFetch(DbCriteria criteria) {
        AnnoBaseProxy.super.beforeFetch(criteria);
    }

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<T> page) {
        AnnoBaseProxy.super.afterFetch(criteria, page);
    }
}
