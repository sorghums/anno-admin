package site.sorghum.anno.db.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.util.TypeUtils;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;
import org.noear.wood.wrap.SqlTypeUtil;
import org.noear.wood.wrap.TypeConverter;
import site.sorghum.anno.db.exception.AnnoDbException;
import site.sorghum.anno.db.interfaces.AnnoAdminCoreFunctions;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.RemoveParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.i18n.I18nUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库服务
 *
 * @author sorghum
 * @since 2023/07/07
 */
@Named("dbServiceWood")
public class DbServiceWood implements DbService {

    /**
     * 通用条件
     */
    private static final String COMMON_CONDITION = "1=1";

    /**
     * Wood数据库上下文
     */
    @Db
    DbContext dbContext;

    @SneakyThrows
    @Override
    public <T> IPage<T> page(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam) {
        TableParam<T> tableParam = AnnoAdminCoreFunctions.tableParamFetchFunction.apply(tClass);
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(dbConditions, tableParam);
        return dbTableQuery
            .limit((pageParam.getPage() - 1) * pageParam.getPageSize(), pageParam.getPageSize())
            .selectPage(tableParam.getColumnStr(), tableParam.getClazz());
    }

    @SneakyThrows
    @Override
    public <T> List<T> list(Class<T> tClass, List<DbCondition> dbConditions) {
        TableParam<T> tableParam = AnnoAdminCoreFunctions.tableParamFetchFunction.apply(tClass);
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(dbConditions, tableParam);
        return dbTableQuery.selectList(tableParam.getColumnStr(), tableParam.getClazz());
    }

    @Override
    public <T> T queryOne(Class<T> tClass, List<DbCondition> dbConditions) {
        List<T> ts = list(tClass, dbConditions);
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
    public <T> int update(List<DbCondition> dbConditions, T t) {
        TableParam<T> tableParam = AnnoAdminCoreFunctions.tableParamFetchFunction.apply(t.getClass());
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(dbConditions, tableParam);
        // 执行值
        dbTableQuery.setEntityIf(t, (k, v) -> filterField(tableParam, k, v));
        return dbTableQuery.update();
    }

    @SneakyThrows
    @Override
    public <T> long insert(T t) {
        TableParam<T> tableParam = AnnoAdminCoreFunctions.tableParamFetchFunction.apply(t.getClass());
        RemoveParam removeParam = tableParam.getRemoveParam();
        DbTableQuery dbTableQuery = dbContext.
            table(tableParam.getTableName()).
            setEntityIf(t, (k, v) -> filterField(tableParam, k, v));
        if (removeParam.getLogic()) {
            // 查询当前字段的类型
            Field field = AnnoAdminCoreFunctions.sqlFieldToJavaFieldFunction.apply(tableParam.getClazz(), removeParam.getRemoveColumn());
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(removeParam.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), removeParam.getNotRemoveValue());
            dbTableQuery.set(removeParam.getRemoveColumn(), converted);
        }
        return dbTableQuery.insert();
    }

    @SneakyThrows
    @Override
    public <T> int delete(Class<T> tClass, List<DbCondition> dbConditions) {
        TableParam<T> tableParam = AnnoAdminCoreFunctions.tableParamFetchFunction.apply(tClass);
        RemoveParam removeParam = tableParam.getRemoveParam();
        DbTableQuery dbTableQuery = buildCommonDbTableQuery(dbConditions, tableParam);
        if (removeParam.getLogic()) {
            // 查询当前字段的类型
            Field field = AnnoAdminCoreFunctions.sqlFieldToJavaFieldFunction.apply(tableParam.getClazz(), removeParam.getRemoveColumn());
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(removeParam.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), removeParam.getNotRemoveValue());
            return dbTableQuery.set(removeParam.getRemoveColumn(), converted).update();
        } else {
            return dbTableQuery.delete();
        }
    }

    public <T> DbTableQuery toTbQuery(Class<T> entityClass, List<DbCondition> dbConditions) {
        return buildCommonDbTableQuery(dbConditions, AnnoAdminCoreFunctions.tableParamFetchFunction.apply(entityClass));
    }

    @Override
    public List<Map<String, Object>> sql2MapList(String actualSql) {
        try {
            return dbContext.sql(actualSql).getDataList().getMapList();
        } catch (SQLException e) {
            throw new AnnoDbException(e.getMessage());
        }
    }

    /**
     * entity 转数据库字段时，过滤掉不需要的字段
     */
    private boolean filterField(TableParam<?> tableParam, String tableFieldName, Object fieldValue) {
        if (tableFieldName == null){
            return false;
        }
        if (fieldValue == null) {
            return AnnoAdminCoreFunctions.sqlFieldCanClearFunction.apply(tableParam.getClazz(), tableFieldName);
        }
        return tableParam.getColumns().contains(tableFieldName);
    }


    private <T> DbTableQuery buildCommonDbTableQuery(List<DbCondition> dbConditions, TableParam<T> tableParam) {
        RemoveParam removeParam = tableParam.getRemoveParam();
        DbTableQuery dbTableQuery = tableParam.toTbQuery(dbContext).where(COMMON_CONDITION);
        if (removeParam.getLogic()) {
            // 查询当前字段的类型
            Field field = AnnoAdminCoreFunctions.sqlFieldToJavaFieldFunction.apply(tableParam.getClazz(), removeParam.getRemoveColumn());
            if (field == null) {
                throw new AnnoDbException("未找在实体中找到对应的逻辑删除字段,请检查:%s".formatted(removeParam.getRemoveColumn()));
            }
            Object converted = Convert.convert(field.getType(), removeParam.getNotRemoveValue());
            dbConditions.add(DbCondition.builder().field(removeParam.getRemoveColumn()).value(converted).build());
        }
        tableParam.getOrderByParam().fillSql(dbTableQuery);
        for (DbCondition dbCondition : dbConditions) {
            dbCondition.woodFill(dbTableQuery);
        }
        return dbTableQuery;
    }
}
