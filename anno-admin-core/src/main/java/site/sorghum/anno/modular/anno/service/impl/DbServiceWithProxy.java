package site.sorghum.anno.modular.anno.service.impl;


import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.wood.IPage;
import site.sorghum.anno.db.exception.AnnoDbException;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Anno用户数据库操作处理程序
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Component(typed = true)
@Slf4j
public class DbServiceWithProxy implements DbService {

    DbService dbService;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    MetadataManager metadataManager;

    @Init
    public void setDbService(@Inject(value = "${anno.db.type:wood}") String dbType) {
        if ("wood".equals(dbType)) {
            Solon.context().getBeanAsync(
                "woodDbService", ds -> {
                    dbService = (DbService) ds;
                }
            );
        } else {
            throw new AnnoDbException("不支持的数据库类型，仅支持wood。");
        }
    }

    @Override
    public <T> IPage<T> page(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, pageParam);
        proxyInstance.beforeFetch(tableParam, dbConditions, pageParam);
        IPage<T> page = dbService.page(tableParam, dbConditions, pageParam);
        // 后置处理
        proxyInstance.afterFetch(page.getList());
        return page;
    }

    @Override
    public <T> List<T> list(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, null);
        proxyInstance.beforeFetch(tableParam, dbConditions, null);
        List<T> list = dbService.list(tableParam, dbConditions);
        // 后置处理
        preProxyInstance.afterFetch(list);
        proxyInstance.afterFetch(list);
        return list;
    }

    @Override
    public <T> T queryOne(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.fetchPermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeFetch(tableParam, dbConditions, null);
        proxyInstance.beforeFetch(tableParam, dbConditions, null);
        T item = dbService.queryOne(tableParam, dbConditions);
        // 后置处理
        ArrayList<T> list = item == null ? new ArrayList<>() : CollUtil.newArrayList(item);
        preProxyInstance.afterFetch(list);
        proxyInstance.afterFetch(list);
        return item;
    }

    @Override
    public <T> int update(TableParam<T> tableParam, List<DbCondition> dbConditions, T t) {
        Class<T> clazz = tableParam.getClazz();
        permissionProxy.updatePermission(clazz);
        AnEntity managerEntity = metadataManager.getEntity(clazz);
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeUpdate(tableParam, dbConditions, t);
        proxyInstance.beforeUpdate(tableParam, dbConditions, t);
        int update = dbService.update(tableParam, dbConditions, t);
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
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeAdd(tableParam, t);
        proxyInstance.beforeAdd(tableParam, t);
        long insert = dbService.insert(tableParam, t);
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
        AnnoPreBaseProxy<T> preProxyInstance = Solon.context().getBean(managerEntity.getPreProxy());
        AnnoBaseProxy<T> proxyInstance = Solon.context().getBean(managerEntity.getProxy());
        // 前置处理
        preProxyInstance.beforeDelete(tableParam, dbConditions);
        proxyInstance.beforeDelete(tableParam, dbConditions);
        int delete = dbService.delete(tableParam, dbConditions);
        // 后置处理
        preProxyInstance.afterDelete(dbConditions);
        proxyInstance.afterDelete(dbConditions);
        return delete;
    }
}