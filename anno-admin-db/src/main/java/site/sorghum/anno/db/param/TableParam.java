package site.sorghum.anno.db.param;

import lombok.*;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 表参数
 *
 * @author sorghum
 * @since 2023/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableParam<T> {
    /**
     * 实体类
     */
    Class<T> clazz;
    /**
     * 表名
     */
    String tableName;

    /**
     * 列名
     */
    List<String> columns = new ArrayList<>();

    /**
     * 删除参数
     */
    RemoveParam removeParam;

    /**
     * 排序参数
     */
    OrderByParam orderByParam = new OrderByParam();


    /**
     * 虚拟表
     */
    boolean virtualTable = false;


    /**
     * 连表信息
     */
    List<JoinTable> joinTables = new ArrayList<>();

    public void addColumn(String column) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

    public String getColumnStr() {
        String column = String.join(",", this.getColumns());
        if (column.isEmpty()) {
            column = "*";
        }
        return column;
    }

    /**
     * 设置实体类
     *
     * @param clazz 实体类
     */
    public void setClazz(Class<?> clazz) {
        this.clazz = (Class<T>) clazz;
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class JoinTable {

        /**
         * 表名
         */
        String tableName;

        /**
         * 别名
         */
        String alias;

        /**
         * 1 left join 2 right join 3 inner join
         */
        int joinType = 1;

        /**
         * 连接条件
         */
        String joinCondition;
    }


    public DbTableQuery toTbQuery(DbContext dbContext) {
        if (joinTables.isEmpty()) {
            return dbContext.table(tableName);
        } else {
            DbTableQuery dbTableQuery = dbContext.table(tableName);
            for (JoinTable joinTable : joinTables) {
                String tbName = joinTable.tableName;
                if (!joinTable.alias.isEmpty()) {
                    tbName = joinTable.tableName + " as " + joinTable.alias;
                }
                switch (joinTable.joinType) {
                    case 1 -> dbTableQuery.leftJoin(tbName).on(joinTable.joinCondition);
                    case 2 -> dbTableQuery.rightJoin(tbName).on(joinTable.joinCondition);
                    case 3 -> dbTableQuery.innerJoin(tbName).on(joinTable.joinCondition);
                    default -> throw new IllegalArgumentException("不支持的连接类型");
                }
            }
            return dbTableQuery;
        }
    }

}
