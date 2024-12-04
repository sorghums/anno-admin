package site.sorghum.anno._ddl.entity2db;

import cn.hutool.core.util.ArrayUtil;
import com.github.drinkjava2.jdialects.Dialect;
import com.github.drinkjava2.jdialects.model.ColumnModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno._ddl.DdlException;
import site.sorghum.anno._ddl.DialectUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Java 实体类转换为 DDL 语句
 *
 * @author songyinyin
 * @since 2023/7/3 22:31
 */
@Slf4j
@Data
public class EntityToDdlGenerator<T> {

    protected DbContext dbContext;

    protected EntityToTableGetter<T> entityToTableGetter;

    protected Dialect dialect;

    private final static Map<String, EntityToDdlGenerator> map = new HashMap<>();

    /**
     * 创建一个EntityToDdlGenerator实例
     *
     * @param dbContext           数据库上下文对象
     * @param entityToTableGetter 实体到表的映射器
     * @param <T>                 实体类型
     * @return 返回创建的EntityToDdlGenerator实例
     */
    public static <T> EntityToDdlGenerator<T> of(DbContext dbContext, EntityToTableGetter<T> entityToTableGetter) {
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

    public EntityToDdlGenerator(DbContext dbContext, EntityToTableGetter<T> entityToTableGetter) {
        this.dbContext = dbContext;
        this.entityToTableGetter = entityToTableGetter;
        try (Connection connection = dbContext.getConnection()) {
            this.dialect = Dialect.guessDialect(connection);
        } catch (SQLException e) {
            log.error("annoAdmin guess Dialect error, default is mysql.");
            this.dialect = Dialect.MySQLDialect;
        }
        Dialect.setGlobalAllowReservedWords(true);
    }

    /**
     * 自动维护 entity 的表结构
     *
     * @param entity 实体类
     */
    public void autoMaintainTable(T entity) {
        TableWrap table = entityToTableGetter.getTable(entity);
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
        TableWrap table = entityToTableGetter.getTable(entity);
        return dialect.toCreateDDL(DialectUtil.tableWrap2TableModel(table));
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
        TableWrap table = entityToTableGetter.getTable(entity);
        TableWrap existsTable = dbContext.getMetaData().getTable(table.getName());

        if (existsTable == null) {
            throw new DdlException("table not exists: " + table.getName());
        }
        Collection<ColumnWrap> columns = table.getColumns();
        List<String> existsTableColumnNames = existsTable.getColumns().stream().map(ColumnWrap::getName).map(String::toLowerCase).toList();
        List<ColumnWrap> addColumnWrap = columns.stream().filter(columnWrap -> !existsTableColumnNames.contains(columnWrap.getName().toLowerCase())).toList();
        if (addColumnWrap.isEmpty()) {
            return Collections.emptyList();
        }
        List<ColumnModel> columnModels = DialectUtil.columnWrap2ColumnModel(addColumnWrap, table);
        return List.of(dialect.toAddColumnDDL(columnModels.toArray(new ColumnModel[0])));
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
