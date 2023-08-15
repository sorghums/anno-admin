package site.sorghum.anno.anno.proxy;


import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import org.noear.wood.impl.IPageImpl;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Anno用户数据库操作处理程序
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Slf4j
@Named("dbServiceWithProxy")
public class DbServiceWithProxy implements DbService {

    @Inject
    @Named("dbServiceWood")
    DbService dbService;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    MetadataManager metadataManager;

    @Override
    public <T> IPage<T> page(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, pageParam);
        proxyInstance.beforeFetch(tableParam, dbConditions, pageParam);
        IPage<T> page = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.page(tableParam, dbConditions, pageParam),
            () -> new IPageImpl<>(new ArrayList<>(), 0, 0)
        );
        // 后置处理
        AnnoPage<T> annoPage = new AnnoPage<>(true, page.getList(), page.getTotal(), page.getSize(), page.getPages());
        preProxyInstance.afterFetch(tableParam, dbConditions, pageParam, annoPage);
        proxyInstance.afterFetch(tableParam, dbConditions, pageParam, annoPage);
        return new IPageImpl<>(annoPage.getList(), annoPage.getTotal(), annoPage.getSize());
    }

    @Override
    public <T> List<T> list(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, null);
        proxyInstance.beforeFetch(tableParam, dbConditions, null);
        List<T> list = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.list(tableParam, dbConditions),
            ArrayList::new
        );
        // 后置处理
        AnnoPage<T> annoPage = new AnnoPage<>(false, list, list.size(), list.size(), 0);
        preProxyInstance.afterFetch(tableParam, dbConditions, null, annoPage);
        proxyInstance.afterFetch(tableParam, dbConditions, null, annoPage);
        return annoPage.getList();
    }

    @Override
    public <T> T queryOne(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, null);
        proxyInstance.beforeFetch(tableParam, dbConditions, null);
        T item = virtualProcess(tableParam.isVirtualTable(),
            () -> dbService.queryOne(tableParam, dbConditions),
            () -> null
        );
        // 后置处理
        ArrayList<T> list = item == null ? new ArrayList<>() : CollUtil.newArrayList(item);
        AnnoPage<T> annoPage = new AnnoPage<>(false, list, list.size(), list.size(), 0);
        preProxyInstance.afterFetch(tableParam, dbConditions, null,annoPage);
        proxyInstance.afterFetch(tableParam, dbConditions, null, annoPage);
        return !annoPage.getList().isEmpty() ? annoPage.getList().get(0) : null;
    }

    @Override
    public <T> int update(TableParam<T> tableParam, List<DbCondition> dbConditions, T t) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.updatePermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeUpdate(tableParam, dbConditions, t);
        proxyInstance.beforeUpdate(tableParam, dbConditions, t);
        int update = virtualProcess(tableParam.isVirtualTable(),
            () ->  dbService.update(tableParam, dbConditions, t),
            () -> 0
        );
        // 后置处理
        preProxyInstance.afterUpdate(t);
        proxyInstance.afterUpdate(t);
        return update;
    }

    @Override
    public <T> long insert(TableParam<T> tableParam, T t) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.addPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeAdd(tableParam, t);
        proxyInstance.beforeAdd(tableParam, t);
        long insert = virtualProcess(tableParam.isVirtualTable(),
            () ->  dbService.insert(tableParam, t),
            () -> 0L
        );
        // 后置处理
        preProxyInstance.afterAdd(t);
        proxyInstance.afterAdd(t);
        return insert;
    }

    @Override
    public <T> int delete(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.deletePermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = AnnoBeanUtils.getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = AnnoBeanUtils.getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeDelete(tableParam, dbConditions);
        proxyInstance.beforeDelete(tableParam, dbConditions);
        int delete = virtualProcess(tableParam.isVirtualTable(),
            () ->  dbService.delete(tableParam, dbConditions),
            () -> 0
        );
        // 后置处理
        preProxyInstance.afterDelete(dbConditions);
        proxyInstance.afterDelete(dbConditions);
        return delete;
    }

    private <T> T virtualProcess(boolean isVirtual,Supplier<T> supplier,Supplier<T>  defaultSupplier){
        if (isVirtual){
            return defaultSupplier.get();
        }
        return supplier.get();
    }
}