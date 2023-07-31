package site.sorghum.anno._ddl.entity2db;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno._ddl.DdlException;
import site.sorghum.anno._ddl.DdlGenerator;
import site.sorghum.anno._ddl.Platform;
import site.sorghum.anno._ddl.PlatformFactory;

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

    protected PlatformFactory platformFactory;

    protected EntityToTableGetter<T> entityToTableGetter;

    public EntityToDdlGenerator(DbContext dbContext, EntityToTableGetter<T> entityToTableGetter) {
        this.dbContext = dbContext;
        this.platformFactory = new PlatformFactory();
        this.entityToTableGetter = entityToTableGetter;
    }

    public EntityToDdlGenerator(DbContext dbContext, PlatformFactory platformFactory, EntityToTableGetter<T> entityToTableGetter) {
        this.dbContext = dbContext;
        this.platformFactory = platformFactory;
        this.entityToTableGetter = entityToTableGetter;
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
        Platform platform = platformFactory.getPlatformInstance(dbContext.getMetaData());
        DdlGenerator ddlGenerator = platform.getDdlGenerator();

        TableWrap table = entityToTableGetter.getTable(entity);
        return ddlGenerator.getTableDDL(table);
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

        Platform platform = platformFactory.getPlatformInstance(dbContext.getMetaData());
        DdlGenerator ddlGenerator = platform.getDdlGenerator();
        return ddlGenerator.getAddColumnDDL(table, existsTable);
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
