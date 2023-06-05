package site.sorghum.anno.modular.anno.annotation.field.type;

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
public @interface AnnoOptionType {
    /**
     * SQL语句, 优先级高于value
     * 必须返回两列，列名分别为 label 和 value
     * 比如 select id, name from table where del_flag = 0 order by id desc
     *
     * @return {@link String}
     */
    String sql() default "";

    /**
     * 选择数据
     *
     * @return {@link OptionData[]}
     */
    OptionData[] value() default {};

    @interface OptionData {
        /**
         * 显示的标签
         */
        String label();

        /**
         * 显示的值
         */
        String value();
    }
}
