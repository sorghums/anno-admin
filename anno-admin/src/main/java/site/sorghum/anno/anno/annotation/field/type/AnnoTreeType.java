package site.sorghum.anno.anno.annotation.field.type;

import site.sorghum.anno.anno.tree.TreeDataSupplier;

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
     * 自定义数据提供者
     *
     * @return 自定义数据提供者，返回值为TreeDataSupplier的类型
     */

    Class<? extends TreeDataSupplier> supplier() default TreeDataSupplier.class;

    /**
     * annoMain注释的类，比如 SysPermission.class
     */
    TreeAnnoClass treeAnno() default @TreeAnnoClass(annoClass = Object.class);

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    boolean isMultiple() default false;


    /**
     * 树形数据
     */
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

    /**
     * 在线字典的key，如果不为空，则会自动走在线字典的代理操作
     * @return {@link String}
     */
    String onlineDictKey() default "";
}
