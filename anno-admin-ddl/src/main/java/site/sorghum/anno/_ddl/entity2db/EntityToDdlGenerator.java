package site.sorghum.anno._ddl.entity2db;

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
import java.util.Collections;
import java.util.List;

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

    public EntityToDdlGenerator(DbContext dbContext, EntityToTableGetter<T> entityToTableGetter) {
        this.dbContext = dbContext;
        this.entityToTableGetter = entityToTableGetter;
        try (Connection connection = dbContext.getConnection()) {
            this.dialect = Dialect.guessDialect(connection);
        } catch (SQLException e) {
            log.error("annoAdmin guess Dialect error, default is mysql.");
            this.dialect = Dialect.MySQLDialect;
        }
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
    public String getCreateTableDDL(T entity) {
        TableWrap table = entityToTableGetter.getTable(entity);
        return String.join("\n", dialect.toCreateDDL(DialectUtil.tableWrap2TableModel(table)));
    }

    /**
     * 获取某个实体类创建表的 DDL 语句，并执行
     *
     * @param entity 实体类
     */
    public void executeCreateTableDDL(T entity) {
        String tableDDL = getCreateTableDDL(entity);
        try {
            dbContext.exe(tableDDL);
            log.info("exe ddl ==> {}", tableDDL);
        } catch (Exception e) {
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
        List<ColumnWrap> columns = table.getColumns();
        List<String> existsTableColumnNames = existsTable.getColumns().stream().map(ColumnWrap::getName).map(String::toLowerCase).toList();
        List<ColumnWrap> addColumnWrap = columns.stream().filter(columnWrap -> !existsTableColumnNames.contains(columnWrap.getName().toLowerCase())).toList();
        if (addColumnWrap.isEmpty()){
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
                dbContext.exe(ddl);
                log.info("exe column ddl ==> {}", ddl);
            }
        } catch (Exception e) {
            throw new DdlException(e);
        }
    }


}
