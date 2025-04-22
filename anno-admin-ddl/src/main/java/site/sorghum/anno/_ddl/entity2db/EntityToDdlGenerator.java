package site.sorghum.anno._ddl.entity2db;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno._ddl.DdlException;
import site.sorghum.ddl.entity.DdlColumnWrap;
import site.sorghum.ddl.entity.DdlTableWrap;
import site.sorghum.ddl.wood.WoodDdlWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java 实体类转换为 DDL 语句
 *
 * @author songyinyin
 * @since 2023/7/3 22:31
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Data
public class EntityToDdlGenerator<T> {

    protected DbContext dbContext;

    protected EntityToTableGetter<T> entityToTableGetter;

    protected WoodDdlWrapper woodDdlWrapper;

    private final static Map<String, EntityToDdlGenerator> map = new HashMap<>();

    public EntityToDdlGenerator(DbContext dbContext,
                                EntityToTableGetter<T> entityToTableGetter) {
        this.dbContext = dbContext;
        this.entityToTableGetter = entityToTableGetter;
        woodDdlWrapper  = new WoodDdlWrapper(dbContext);
    }

    /**
     * 创建一个EntityToDdlGenerator实例
     *
     * @param dbContext           数据库上下文对象
     * @param entityToTableGetter 实体到表的映射器
     * @param <T>                 实体类型
     * @return 返回创建的EntityToDdlGenerator实例
     */
    public static <T> EntityToDdlGenerator<T> of(DbContext dbContext,
                                                 EntityToTableGetter<T> entityToTableGetter) {
        int hashCodeOne = dbContext.hashCode();
        int hashCodeTwo = entityToTableGetter.hashCode();
        String key = hashCodeOne + "_" + hashCodeTwo;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        EntityToDdlGenerator entityToDdlGenerator = new EntityToDdlGenerator(dbContext, entityToTableGetter);
        map.put(key, entityToDdlGenerator);
        return entityToDdlGenerator;
    }

    /**
     * 自动维护 entity 的表结构
     *
     * @param entity 实体类
     */
    public void autoMaintainTable(T entity) {
        DdlTableWrap table = entityToTableGetter.getTable(entity);
        TableWrap existsTable = dbContext.getMetaData().getTable(table.getName());

        if (existsTable == null) {
            executeCreateTableDDL(entity);
        } else {
            executeTableAddedColumnDDL(entity);
        }
    }

    /**
     * 获取某个实体类创建表的 DDL 语句
     *
     * @param entity 实体类
     * @return 创建表的 DDL 语句
     */
    public String[] getCreateTableDDL(T entity) {
        DdlTableWrap table = entityToTableGetter.getTable(entity);
        return woodDdlWrapper.dialect().generateCreateTableDDL(table).toArray(new String[0]);
    }

    /**
     * 获取某个实体类创建表的 DDL 语句，并执行
     *
     * @param entity 实体类
     */
    public void executeCreateTableDDL(T entity) {
        String[] tableDDL = getCreateTableDDL(entity);
        try {
            for (String ddl : tableDDL) {
                dbContext.exe(ddl);
                log.info("exe ddl ==> {}", ddl);
            }
        } catch (Exception e) {
            //String[] 转 string
            log.error("exe ddl error ==> {}", ArrayUtil.join(tableDDL, "\n"));
            throw new DdlException(e);
        }
    }

    /**
     * 根据已有的实体类，生成新增字段的 DDL 语句
     *
     * @param entity 实体类
     * @return 新增字段的 DDL 语句
     */
    public List<String> getTableAddedColumnDDL(T entity) {
        DdlTableWrap table = entityToTableGetter.getTable(entity);
        DdlTableWrap ddlTableWrap = woodDdlWrapper.fromDataSource(table.getName());
        if (ddlTableWrap == null) {
            throw new DdlException("table not exists: " + table.getName());
        }
        List<DdlColumnWrap> columns = table.getColumns();
        List<String> existsTableColumnNames = ddlTableWrap.getColumns().stream().map(DdlColumnWrap::getName).map(String::toLowerCase).toList();
        List<DdlColumnWrap> addColumnWrap = columns.stream().filter(columnWrap -> !existsTableColumnNames.contains(columnWrap.getName().toLowerCase())).toList();
        if (addColumnWrap.isEmpty()) {
            return Collections.emptyList();
        }
        return addColumnWrap.stream().map(it -> woodDdlWrapper.dialect().generateAddColumnDDL(it)).toList();
    }

    /**
     * 根据已有的实体类，生成新增字段的 DDL 语句，并执行
     *
     * @param entity 实体类
     */
    public void executeTableAddedColumnDDL(T entity) {
        List<String> tableAddedColumnDDL = getTableAddedColumnDDL(entity);
        try {
            for (String ddl : tableAddedColumnDDL) {
                log.info("exe column ddl ==> {}", ddl);
                dbContext.exe(ddl);
            }
        } catch (Exception e) {
            throw new DdlException(e);
        }
    }


}
