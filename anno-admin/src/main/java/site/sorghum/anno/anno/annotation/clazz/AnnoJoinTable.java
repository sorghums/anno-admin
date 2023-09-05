package site.sorghum.anno.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * 增加连表相关参数
 * 仅虚拟表时可用
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoJoinTable {
    /**
     * 主表名
     */
    String mainTable();

    /**
     * 主表别名
     */
    String mainAlias();

    /**
     * 连表信息
     */
    JoinTable[] joinTables();

    @interface JoinTable {
        /**
         * 连表名
         */
        String table();

        /**
         * 连表别名
         */
        String alias();

        /**
         * 连表条件
         */
        String joinCondition();

        /**
         * 1 left join 2 right join 3 inner join
         */
        int joinType() default 1;
    }

    /**
     * 是否启用
     */
    boolean enable() default true;
}
