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

    /**
     * annoMain注释的类，比如 SysPermission.class
     */
    TreeAnnoClass treeAnno() default @TreeAnnoClass(annoClass = Object.class);

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    boolean isMultiple() default false;

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

    @interface TreeAnnoClass {

        /**
         * 并且会自动解析类信息
         * 且走代理操作
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

        /**
         * 父主键 key
         */
        String pidKey() default "pid";
    }
}
