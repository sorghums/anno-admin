package site.sorghum.anno.anno.annotation.field.type;

import site.sorghum.anno.anno.option.OptionDataSupplier;

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
     * 必须返回两列，列名分别为 label 和 id
     * 比如 select id, label from table where del_flag = 0 order by id desc
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
     * 自定义数据提供者
     *
     * @return 自定义数据提供者，返回值为OptionDataSupplier的子类类型
     */

    Class<? extends OptionDataSupplier> supplier() default OptionDataSupplier.class;

    /**
     * 返回一个枚举类型的Class对象，该枚举类型用于表示选项。
     *
     * @return 枚举类型的Class对象，如果未指定则返回Enum.class。
     */
    Class<? extends Enum> optionEnum() default Enum.class;

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

    /**
     * 在线字典的key，如果不为空，则会自动走在线字典的代理操作
     * @return {@link String}
     */
    String onlineDictKey() default "";
}
