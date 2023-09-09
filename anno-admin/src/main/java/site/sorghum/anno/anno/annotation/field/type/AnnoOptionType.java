package site.sorghum.anno.anno.annotation.field.type;

import java.lang.annotation.*;

/**
 * Anno 选择类型
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoOptionType {
    /**
     * SQL语句, 优先级高于value
     * 必须返回两列，列名分别为 label 和 value
     * 比如 select value, label from table where del_flag = 0 order by id desc
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

    /**
     * annoMain注释的类，比如 SysOrg.class
     * 最后会执行类似的：select value, label from sys_org where del_flag = 0 order by id desc
     * 并且会自动走SysOrg的代理操作
     */
    OptionAnnoClass optionAnno() default @OptionAnnoClass(annoClass = Object.class);

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    boolean isMultiple() default false;


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


    @interface OptionAnnoClass {

        /**
         * annoMain注释的类，比如 SysOrg.class
         * 最后会执行类似的：select value, label from sys_org where del_flag = 0 order by id desc
         * 并且会自动走SysOrg的代理操作
         */
        Class<?> annoClass();

        /**
         * 显示的值 key
         */
        String idKey() default "id";


        /**
         * 显示的标签 key
         */
        String labelKey() default "name";
    }
}
