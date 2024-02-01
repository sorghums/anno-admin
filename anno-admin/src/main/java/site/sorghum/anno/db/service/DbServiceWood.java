package site.sorghum.anno.db.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.db.*;
import site.sorghum.anno.db.exception.AnnoDbException;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.plugin.join.util.InvokeUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库服务
 *
 * @author sorghum
 * @since 2023/07/07
 */
@Named("dbServiceWood")
public class DbServiceWood implements DbService {

    /**
     * Wood数据库上下文
     */
    @Db
    DbContext dbContext;
    @Inject
    DbTableContext dbTableContext;
    @Inject
    MetadataManager metadataManager;

    @Override
    @SneakyThrows
    public <T> AnnoPage<T> page(DbCriteria criteria) {

        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        if (criteria.getPage() == null) {
            criteria.page(1, 10);
        }
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(criteria);
        IPage<T> page = dbTableQuery.selectPage(tableParam.getColumnStr(), tableParam.getClazz());
        return new AnnoPage<>(true, page.getList(), page.getTotal(), criteria.getPage().getPageSize(), criteria.getPage().getPage());
    }

    @SneakyThrows
    @Override
    public <T> List<T> list(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(criteria);
        return dbTableQuery.selectList(tableParam.getColumnStr(), tableParam.getClazz());
    }

    @Override
    public <T> T queryOne(DbCriteria criteria) {
        List<T> ts = list(criteria);
        if (ts.size() > 1) {
            throw new AnnoDbException(I18nUtil.getMessage("exception.db.out-one"));
        }
        if (ts.isEmpty()) {
            return null;
        }
        return ts.get(0);
    }

    @SneakyThrows
    @Override
    public <T> int update(T t, DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(criteria);
        // 执行值
        AnEntity entity = metadataManager.getEntity(tableParam.getClazz());
        preProcess(t, entity, false);
        dbTableQuery.setEntityIf(t, (k, v) -> filterField(entity, tableParam, k, v));
        return dbTableQuery.update();
    }

    @SneakyThrows
    @Override
    public <T> long insert(T t) {
        TableParam<T> tableParam = dbTableContext.getTableParam(t.getClass());
        DbRemove dbRemove = tableParam.getDbRemove();
        AnEntity entity = metadataManager.getEntity(tableParam.getClazz());
        preProcess(t, entity, true);
        DbTableQuery dbTableQuery = dbContext.table(tableParam.getTableName())
            .setEntityIf(t, (k, v) -> filterField(entity, tableParam, k, v));
        if (dbRemove.getLogic()) {
            // 查询当前字段的类型
            Field field = ReflectUtil.getField(tableParam.getClazz(), AnnoFieldCache.getFieldNameBySqlColumn(tableParam.getClazz(), dbRemove.getRemoveColumn()));
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(dbRemove.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), dbRemove.getNotRemoveValue());
            dbTableQuery.set(dbRemove.getRemoveColumn(), converted);
        }
        return dbTableQuery.insert();
    }

    @SneakyThrows
    @Override
    public <T> int delete(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(criteria);
        DbRemove dbRemove = tableParam.getDbRemove();
        if (dbRemove.getLogic()) {
            // 查询当前字段的类型
            Field field = ReflectUtil.getField(tableParam.getClazz(), AnnoFieldCache.getFieldNameBySqlColumn(tableParam.getClazz(), dbRemove.getRemoveColumn()));
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(dbRemove.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), dbRemove.getNotRemoveValue());
            return dbTableQuery.set(dbRemove.getRemoveColumn(), converted).update();
        } else {
            return dbTableQuery.delete();
        }
    }

    @Override
    public List<Map<String, Object>> sql2MapList(String actualSql) {
        return executeSql2MapList(actualSql);
    }

    @Override
    public List<Map<String, Object>> executeSql2MapList(String sql, Object... params) {
        try {
            return dbContext.sql(sql, params).getDataList().getMapList();
        } catch (SQLException e) {
            throw new AnnoDbException(e.getMessage()).withCause(e);
        }
    }

    @Override
    public <T> long count(DbCriteria criteria) {
        try {
            DbTableQuery dbTableQuery = buildCommonDbTableQuery(criteria);
            return dbTableQuery.selectCount();
        } catch (SQLException e) {
            throw new AnnoDbException(e.getMessage()).withCause(e);
        }
    }

    @Override
    public Object executeSql(String sql, Object... params) {
        try {
            return dbContext.exe(sql, params);
        } catch (Exception e) {
            throw new AnnoDbException(e.getMessage()).withCause(e);
        }
    }

    @Override
    public <T> T sqlQueryOne(Class<T> clazz, String sql, Object... params) {
        try {
            return dbContext.sql(sql, params).getItem(clazz);
        } catch (SQLException e) {
            throw new AnnoDbException(e.getMessage()).withCause(e);
        }
    }

    @Override
    public <T> List<T> sqlQueryList(Class<T> clazz, String sql, Object... params) {
        try {
            return dbContext.sql(sql, params).getList(clazz);
        } catch (SQLException e) {
            throw new AnnoDbException(e.getMessage()).withCause(e);
        }
    }

    /**
     * entity 转数据库字段时，过滤掉不需要的字段
     */
    private boolean filterField(AnEntity entity, TableParam<?> tableParam, String tableFieldName, Object fieldValue) {
        if (!tableParam.getColumns().contains(tableFieldName)) {
            return false;
        }
        if (tableFieldName == null) {
            return false;
        }
        if (fieldValue == null) {
            AnField anField = entity.getFieldMap().get(AnnoFieldCache.getFieldNameBySqlColumn(tableParam.getClazz(), tableFieldName));
            return anField.isEditCanClear();
        }
        return tableParam.getColumns().contains(tableFieldName);
    }


    public <T> DbTableQuery buildCommonDbTableQuery(DbCriteria criteria) {
        TableParam<T> tableParam = dbTableContext.getTableParam(criteria.getEntityName());
        DbRemove dbRemove = tableParam.getDbRemove();
        DbTableQuery dbTableQuery = toTbQuery(tableParam);
        if (dbRemove.getLogic() && !tableParam.isVirtualTable()) {
            // 查询当前字段的类型
            String fieldName = AnnoFieldCache.getFieldNameBySqlColumn(tableParam.getClazz(), dbRemove.getRemoveColumn());
            Field field = ReflectUtil.getField(tableParam.getClazz(), fieldName);
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(dbRemove.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), dbRemove.getNotRemoveValue());
            criteria.condition().eq(dbRemove.getRemoveColumn(), converted);
        }

        DbCondition condition = criteria.getCondition();
        if (condition != null) {
            dbTableQuery.where("1=1");
            for (Object value : condition.getValues()) {
                if (value instanceof DbCondition) {
                    woodFill(dbTableQuery, (DbCondition) value);
                }
            }
            // find group by condition
            List<DbCondition> groupByConditions = condition.findConditionByType(QueryType.GROUP_BY);
            if (CollUtil.isNotEmpty(groupByConditions)) {
                String groupBy = groupByConditions.stream().map(DbCondition::getField).collect(Collectors.joining(","));
                dbTableQuery.groupBy(groupBy);
            }


        }

        DbOrderBy order = criteria.getOrder();
        if (order != null) {
            for (DbOrderBy.OrderByItem orderByItem : order.getOrderByItems()) {
                if (StrUtil.equalsIgnoreCase("ASC", orderByItem.getOrderType())) {
                    dbTableQuery.orderByAsc(orderByItem.getColumn());
                }
                if (StrUtil.equalsIgnoreCase("DESC", orderByItem.getOrderType())) {
                    dbTableQuery.orderByDesc(orderByItem.getColumn());
                }
            }
        }

        DbPage page = criteria.getPage();
        if (page != null) {
            dbTableQuery.limit(page.getOffset(), page.getPageSize());
        }
        return dbTableQuery;
    }

    private void woodFill(DbTableQuery dbTableQuery, DbCondition condition) {
        if (condition == null) {
            return;
        }
        switch (condition.getType()) {
            case AND -> {
                dbTableQuery.and().begin();
                for (Object value : condition.getValues()) {
                    if (value instanceof DbCondition) {
                        woodFill(dbTableQuery, (DbCondition) value);
                    }
                }
                dbTableQuery.end();
            }
            case OR -> {
                dbTableQuery.or().begin();
                for (Object value : condition.getValues()) {
                    if (value instanceof DbCondition) {
                        woodFill(dbTableQuery, (DbCondition) value);
                    }
                }
                dbTableQuery.end();
            }
            case EQ -> dbTableQuery.andEq(condition.getField(), condition.getValues().get(0));
            case NE -> dbTableQuery.andNeq(condition.getField(), condition.getValues().get(0));
            case GT -> dbTableQuery.andGt(condition.getField(), condition.getValues().get(0));
            case GE -> dbTableQuery.andGte(condition.getField(), condition.getValues().get(0));
            case LT -> dbTableQuery.andLt(condition.getField(), condition.getValues().get(0));
            case LE -> dbTableQuery.andLte(condition.getField(), condition.getValues().get(0));
            case LIKE -> dbTableQuery.andLk(condition.getField(), (String) condition.getValues().get(0));
            case NOT_LIKE -> dbTableQuery.andNlk(condition.getField(), (String) condition.getValues().get(0));
            case IN -> {
                if (!condition.getValues().isEmpty()) {
                    dbTableQuery.andIn(condition.getField(), condition.getValues());
                }
            }
            case NOT_IN -> {
                if (!condition.getValues().isEmpty()) {
                    dbTableQuery.andNin(condition.getField(), condition.getValues());
                }
            }
            case IS_NULL -> dbTableQuery.andEq(condition.getField(), null);
            case IS_NOT_NULL -> dbTableQuery.andNeq(condition.getField(), null);
            case BETWEEN ->
                dbTableQuery.andBtw(condition.getField(), condition.getValues().get(0), condition.getValues().get(1));
            case NOT_BETWEEN ->
                dbTableQuery.andNbtw(condition.getField(), condition.getValues().get(0), condition.getValues().get(1));
            case EXISTS -> {
                if (!condition.getValues().isEmpty()) {
                    dbTableQuery.and(" EXISTS (?...) ", condition.getValues());
                }
            }
            case NOT_EXISTS -> {
                if (!condition.getValues().isEmpty()) {
                    dbTableQuery.and(" NOT EXISTS (?...) ", condition.getValues());
                }
            }
            case GROUP_BY -> {
            }
            case ORDER_BY -> dbTableQuery.orderBy(condition.getField());
            case ASC -> dbTableQuery.orderByAsc(condition.getField());
            case DESC -> dbTableQuery.orderByDesc(condition.getField());
            case CUSTOM -> {
                if (!condition.getValues().isEmpty()) {
                    dbTableQuery.and(condition.getField(), condition.getValues());
                } else {
                    dbTableQuery.and(condition.getField());
                }
            }
            default -> throw new IllegalArgumentException("不支持的查询类型");
        }

    }

    private <T> DbTableQuery toTbQuery(TableParam<T> tableParam) {
        if (tableParam.getJoinTables().isEmpty()) {
            return dbContext.table(tableParam.getTableName());
        } else {
            DbTableQuery dbTableQuery = dbContext.table(tableParam.getTableName());
            for (TableParam.JoinTable joinTable : tableParam.getJoinTables()) {
                String tbName = joinTable.getTableName();
                if (StrUtil.isNotBlank(joinTable.getAlias())) {
                    tbName = tbName + " as " + joinTable.getAlias();
                }
                switch (joinTable.getJoinType()) {
                    case 1 -> dbTableQuery.leftJoin(tbName).on(joinTable.getJoinCondition());
                    case 2 -> dbTableQuery.rightJoin(tbName).on(joinTable.getJoinCondition());
                    case 3 -> dbTableQuery.innerJoin(tbName).on(joinTable.getJoinCondition());
                    default -> throw new IllegalArgumentException("不支持的连接类型");
                }
            }
            return dbTableQuery;
        }
    }

    private void preProcess(Object data, AnEntity entity, boolean insert) {
        List<AnField> anFields = entity.getFields();
        for (AnField field : anFields) {
            Class<? extends FieldBaseSupplier> fieldWhenNullSet = insert ? field.getInsertWhenNullSet() : field.getUpdateWhenNullSet();
            if (fieldWhenNullSet != EmptyFieldBaseSupplier.class) {
                FieldBaseSupplier<?> fieldBaseSupplier = AnnoBeanUtils.getBean(fieldWhenNullSet);
                if (fieldBaseSupplier != null && InvokeUtil.invokeGetter(data, field.getReflectField()) == null) {
                    InvokeUtil.invokeSetter(data, field.getReflectField(), fieldBaseSupplier.get());
                }
            }
        }
    }
}
