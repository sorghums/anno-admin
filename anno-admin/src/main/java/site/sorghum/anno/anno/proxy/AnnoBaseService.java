package site.sorghum.anno.anno.proxy;


import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.Dami;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbTableContext;
import site.sorghum.anno.db.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.method.MethodTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Anno用户数据库操作处理程序
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Slf4j
@Named
public class AnnoBaseService {

    public static final String BASE_ENTITY_TOPIC = "anno.entity.";

    @Inject
    DbService dbService;

    @Inject
    MetadataManager metadataManager;
    @Inject
    DbTableContext dbTableContext;

    public <T> AnnoPage<T> page(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeFetch(criteria);

        AnnoPage<T> page = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.page(criteria),
            () -> new AnnoPage<>(true, new ArrayList<>(), 0, 0, 0)
        );
        // 后置处理
        damiProxy.afterFetch(criteria, page);
        return page;
    }

    public <T> List<T> list(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeFetch(criteria);
        List<T> list = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.list(criteria),
            ArrayList::new
        );
        // 后置处理
        damiProxy.afterFetch(criteria, new AnnoPage<>(false, list, list.size(), 0, 1));
        return list;
    }

    public <T> T queryOne(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeFetch(criteria);

        T item = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.queryOne(criteria),
            () -> null
        );
        // 后置处理
        ArrayList<T> list = item == null ? new ArrayList<>() : CollUtil.newArrayList(item);
        AnnoPage<T> annoPage = new AnnoPage<>(false, list, list.size(), 0, 0);
        damiProxy.afterFetch(criteria, annoPage);
        return !annoPage.getList().isEmpty() ? annoPage.getList().get(0) : null;
    }

    public <T> int update(T t, DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        // 前置处理
        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeUpdate(t, criteria);

        int update = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.update(t, criteria),
            () -> 0
        );
        // 后置处理
        damiProxy.afterUpdate(t);
        return update;
    }

    public <T> long insert(T t) {
        DbCriteria criteria = DbCriteria.fromObject(t);
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        // 前置处理
        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeAdd(t);

        long insert = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.insert(t),
            () -> 0L
        );
        // 后置处理
        damiProxy.afterAdd(t);
        return insert;
    }

    public <T> int delete(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        AnEntity managerEntity = metadataManager.getEntity(criteria.getEntityName());

        // 前置处理
        AnnoBaseProxy<T> damiProxy = getDamiProxy(managerEntity.getEntityName());
        damiProxy.beforeDelete(criteria);

        int delete = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.delete(criteria),
            () -> 0
        );
        // 后置处理
        damiProxy.afterDelete(criteria);
        return delete;
    }

    public List<Map<String, Object>> sql2MapList(String actualSql) {
        return dbService.sql2MapList(actualSql);
    }

    private <T> T virtualProcess(boolean isVirtual, Supplier<T> supplier, Supplier<T> defaultSupplier) {
        if (isVirtual) {
            return defaultSupplier.get();
        }
        return supplier.get();
    }

    private <T> AnnoBaseProxy<T> getDamiProxy(String entityName) {
        return (AnnoBaseProxy<T>) MethodTemplateManager.create(AnnoBaseProxy.class);
    }
}