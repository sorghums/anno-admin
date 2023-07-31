package site.sorghum.anno.anno.annotation.field.type;

import java.lang.annotation.*;

/**
 * Anno 选择类型
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoTreeType {
    /**
     * SQL语句, 优先级高于value
     * 必须返回两列，列名分别为 id,label,pid
     * 比如 select id,label,pid from table where del_flag = 0 order by id desc
     *
     * @return {@link String}
     */
    String sql() default "";

    /**
     * 选择数据
     *
     * @return {@link TreeData[]}
     */
    TreeData[] value() default {};

    @interface TreeData {
        /**
         * 主键
         */
        String id();
        /**
         * 显示的标签
         */
        String label();

        /**
         * 显示的值
         */
        String value();

        /**
         * 父主键
         */
        String pid();
    }
}
