package site.sorghum.anno.modular.anno.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDto;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Anno服务
 *
 * @author sorghum
 * @since 2023/05/20
 */
@SuppressWarnings("unchecked")
@ProxyComponent
@Slf4j
public class AnnoServiceImpl implements AnnoService {

    @Inject
    PermissionProxy permissionProxy;
    @Db
    DbContext dbContext;

    @Override
    public <T> IPage<T> page(QueryRequest<T> queryRequest) {
        try {
            permissionProxy.fetchPermission(queryRequest.getClazz());
            Class<T> clazz = queryRequest.getClazz();
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(clazz);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(clazz);
            String tableName = AnnoUtil.getTableName(clazz);
            String catKey = AnnoUtil.getCatKey(clazz);
            String columns = String.join(",", AnnoUtil.getTableFields(clazz));
            DbTableQuery tableQuery = dbContext.table(tableName)
                    .whereEntity(queryRequest.getParam());
            if (queryRequest.getParam() == null) {
                tableQuery.where("1=1");
            }
            // 如果有分类查询
            if (StrUtil.isNotBlank(catKey) && StrUtil.isNotBlank(queryRequest.getCat())) {
                tableQuery.andEq(catKey, queryRequest.getCat());
            }
            // 如果有配置逻辑删除
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 1) {
                tableQuery.andEq(annoRemove.removeField(), annoRemove.notRemoveValue());
            }
            if (StrUtil.isNotBlank(queryRequest.getAndSql())) {
                tableQuery.and(queryRequest.getAndSql());
            }
            tableQuery = tableQuery.limit(queryRequest.getPage() * queryRequest.getPerPage(), queryRequest.getPerPage());
            if (StrUtil.isNotBlank(queryRequest.getOrderBy())) {
                if (queryRequest.isAsc()) {
                    tableQuery.orderBy(queryRequest.getOrderBy());
                } else {
                    tableQuery.orderByDesc(queryRequest.getOrderBy());
                }
            }
            // 查询前置处理
            preProxyInstance.beforeFetch(tableQuery);
            proxyInstance.beforeFetch(tableQuery);
            IPage<T> iPage = tableQuery.selectPage(columns, clazz);
            // 查询后置处理
            preProxyInstance.afterFetch(iPage.getList());
            proxyInstance.afterFetch(iPage.getList());
            return iPage;
        } catch (Exception e) {
            log.error("AnnoService.page error:{}", e.getMessage());
            throw new BizException("AnnoService.page error",e);
        }
    }

    @Override
    public <T> String sql(Class<T> clazz,String sql) {
        try {
            // 如果有配置逻辑删除
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 1) {
                sql = sql + " and " + annoRemove.removeField() + " = " + annoRemove.notRemoveValue();
            }
            return sql;
        } catch (Exception e) {
            log.error("AnnoService.page error:{}", e.getMessage());
            throw new BizException("AnnoService.page error",e);
        }
    }

    @Override
    public <T> String m2mSql(Map<?,String > param) {
        if (StrUtil.isBlank(param.get("mediumTableClass"))){
            return "";
        }
        String mediumOtherField = param.get("mediumOtherField");
        String otherValue = param.get("joinValue");
        String mediumThisField = param.get("mediumThisField");

        Class<?> mediumCLass = AnnoClazzCache.get(param.get("mediumTableClass"));
        String mediumTable = AnnoUtil.getTableName(mediumCLass);
        String sql = "select "+mediumThisField+" from " + mediumTable + " where " + mediumOtherField + " = '" + otherValue + "'";
        return sql(mediumCLass,sql);
    }

    @Override
    public <T> List<T> list(QueryRequest<T> queryRequest) {
        try {
            permissionProxy.fetchPermission(queryRequest.getClazz());
            Class<T> clazz = queryRequest.getClazz();
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(clazz);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(clazz);
            String tableName = AnnoUtil.getTableName(clazz);
            String catKey = AnnoUtil.getCatKey(clazz);
            String columns = String.join(",", AnnoUtil.getTableFields(clazz));
            DbTableQuery tableQuery = dbContext.table(tableName)
                    .whereEntity(queryRequest.getParam());
            if (queryRequest.getParam() == null) {
                tableQuery.where("1=1");
            }
            // 如果有分类查询
            if (StrUtil.isNotBlank(catKey) && StrUtil.isNotBlank(queryRequest.getCat())) {
                tableQuery.andEq(catKey, queryRequest.getCat());
            }
            // 如果有配置逻辑删除
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 1) {
                tableQuery.andEq(annoRemove.removeField(), annoRemove.notRemoveValue());
            }
            if (StrUtil.isNotBlank(queryRequest.getAndSql())) {
                tableQuery.and(queryRequest.getAndSql());
            }
            if (StrUtil.isNotBlank(queryRequest.getOrderBy())) {
                if (queryRequest.isAsc()) {
                    tableQuery.orderBy(queryRequest.getOrderBy());
                } else {
                    tableQuery.orderByDesc(queryRequest.getOrderBy());
                }
            }
            // 查询前置处理
            preProxyInstance.beforeFetch(tableQuery);
            proxyInstance.beforeFetch(tableQuery);
            List<T> list = tableQuery.selectList(columns, clazz);
            // 查询后置处理
            preProxyInstance.afterFetch(list);
            proxyInstance.afterFetch(list);
            return list;
        } catch (Exception e) {
            log.error("AnnoService.list error:{}", e.getMessage());
            throw new BizException("AnnoService.list error", e);
        }
    }

    @Override
    public <T> T queryById(Class<T> clazz, Serializable id) {
        try {
            permissionProxy.fetchPermission(clazz);
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(clazz);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(clazz);
            String tableName = AnnoUtil.getTableName(clazz);
            String pkField = AnnoUtil.getPkField(clazz);
            String columns = String.join(",", AnnoUtil.getTableFields(clazz));
            DbTableQuery tableQuery = dbContext.table(tableName)
                    .whereEq(pkField, id);
            // 查询前置处理
            preProxyInstance.beforeFetch(tableQuery);
            proxyInstance.beforeFetch(tableQuery);
            T selectItem = tableQuery.selectItem(columns, clazz);
            // 查询后置处理
            preProxyInstance.afterFetch(CollUtil.toList(selectItem));
            proxyInstance.afterFetch(CollUtil.toList(selectItem));
            return selectItem;
        } catch (Exception e) {
            log.error("AnnoService.queryById error:{}", e.getMessage());
            throw new BizException("AnnoService.queryById error", e);
        }
    }

    /**
     * 通过id删除
     *
     * @param id id
     */
    @Override
    public <T> void removeById(Class<T> clazz, Serializable id) {
        try {
            permissionProxy.deletePermission(clazz);
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(clazz);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(clazz);
            String pkField = AnnoUtil.getPkField(clazz);
            String tableName = AnnoUtil.getTableName(clazz);
            // 删除前置处理
            preProxyInstance.beforeDelete(id);
            proxyInstance.beforeDelete(id);
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 0) {
                // 物理删除

                dbContext.table(tableName)
                        .whereEq(pkField, id).delete();
            } else {
                // 逻辑删除
                dbContext.table(tableName)
                        .whereEq(pkField, id).set(annoRemove.removeField(), annoRemove.removeValue()).update();

            }
            // 删除后置处理
            preProxyInstance.afterDelete(id);
            proxyInstance.afterDelete(id);
        } catch (Exception e) {
            log.error("AnnoService.deleteById error:{}", e.getMessage());
            throw new BizException("AnnoService.deleteById error", e);
        }
    }

    /**
     * 通过组合条件删除
     *
     * @param clazz  clazz
     * @param tuples 元组
     */
    @Override
    public <T> void removeByKvs(Class<T> clazz, List<Tuple> tuples) {
        try {
            permissionProxy.deletePermission(clazz);
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(clazz);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(clazz);
            String pkField = AnnoUtil.getPkField(clazz);
            String tableName = AnnoUtil.getTableName(clazz);
            // 删除前置处理
            preProxyInstance.beforeDelete(tuples);
            proxyInstance.beforeDelete(tuples);
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 0) {
                // 物理删除
                DbTableQuery dbTableQuery = dbContext.table(tableName)
                        .where("1=1");
                for (Tuple tuple : tuples) {
                    dbTableQuery.andEq(tuple.get(0), tuple.get(1));
                }
                dbTableQuery.delete();
            } else {
                // 逻辑删除
                DbTableQuery dbTableQuery = dbContext.table(tableName).where("1=1");
                for (Tuple tuple : tuples) {
                    dbTableQuery.andEq(tuple.get(0), tuple.get(1));
                }
                dbTableQuery.set(annoRemove.removeField(), annoRemove.removeValue()).update();

            }
            // 删除后置处理
            preProxyInstance.afterDelete(tuples);
            proxyInstance.afterDelete(tuples);
        } catch (Exception e) {
            log.error("AnnoService.deleteById error:{}", e.getMessage());
            throw new BizException("AnnoService.deleteById error", e);
        }
    }


    @Override
    public <T> void updateById(T param) {
        try {
            permissionProxy.updatePermission(param);
            Class<T> aClass = (Class<T>) param.getClass();
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(aClass);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(aClass);
            // 更新前置处理
            preProxyInstance.beforeUpdate(param);
            proxyInstance.beforeUpdate(param);
            String tableName = AnnoUtil.getTableName(aClass);
            DbTableQuery table = dbContext.table(tableName);
            table.setEntity(param);
            table.whereEq(AnnoUtil.getPkField(aClass), ReflectUtil.getFieldValue(param, AnnoUtil.getPkField(aClass)));
            table.update();
            // 更新后置处理
            preProxyInstance.afterUpdate(param);
            proxyInstance.afterUpdate(param);
        } catch (Exception e) {
            log.error("AnnoService.updateById error:{}", e.getMessage());
            throw new BizException("AnnoService.updateById error", e);
        }
    }

    @Override
    public <T> void save(T param) {
        try {
            permissionProxy.addPermission(param);
            Class<T> aClass = (Class<T>) param.getClass();
            AnnoPreBaseProxy<T> preProxyInstance = AnnoUtil.getPreProxyInstance(aClass);
            AnnoBaseProxy<T> proxyInstance = AnnoUtil.getProxyInstance(aClass);
            // 新增前置处理
            preProxyInstance.beforeAdd(param);
            proxyInstance.beforeAdd(param);
            String tableName = AnnoUtil.getTableName(aClass);
            DbTableQuery table = dbContext.table(tableName);
            table.setEntity(param);
            table.insert();
            // 新增后置处理
            preProxyInstance.afterAdd(param);
            proxyInstance.afterAdd(param);
        } catch (Exception e) {
            log.error("AnnoService.save error:{}", e.getMessage());
            throw new BizException("AnnoService.save error", e);
        }
    }

    @Override
    public <T> void onlySave(T param) {
        try {
            Class<T> aClass = (Class<T>) param.getClass();
            String tableName = AnnoUtil.getTableName(aClass);
            DbTableQuery table = dbContext.table(tableName);
            table.setEntity(param);
            table.insert();
        } catch (Exception e) {
            log.error("AnnoService.onlySave error:{}", e.getMessage());
            throw new BizException("AnnoService.onlySave error", e);
        }
    }

    @Override
    public <T> List<AnnoTreeDto<String>> annoTrees(QueryRequest<T> request) {
        try {
            AnnoMain annoMain = AnnoUtil.getAnnoMain(request.getClazz());
            AnnoTree annoTree = annoMain.annoTree();
            List<T> data = list(request);
            return AnnoUtil.buildAnnoTree(data, annoTree.label(), annoTree.key(), annoTree.parentKey());
        } catch (Exception e) {
            log.error("AnnoService.annoTrees error:{}", e.getMessage());
            throw new BizException("AnnoService.annoTrees error", e);
        }
    }
}
