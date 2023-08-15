package site.sorghum.anno._metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class AnJoinTable {
    /**
     * 主表名
     */
    String mainTable;

    /**
     * 主表别名
     */
    String mainAlias;

    /**
     * 连表信息
     */
    List<JoinTable> joinTables;


    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class JoinTable {
        /**
         * 连表名
         */
        String table;

        /**
         * 连表别名
         */
        String alias;

        /**
         * 连表条件
         */
        String joinCondition;

        /**
         * 1 left join 2 right join 3 inner join
         */
        int joinType = 0;
    }
}
