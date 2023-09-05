package site.sorghum.anno.anno.annotation.field;


import java.lang.annotation.*;

/**
 * Anno Button 注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoButton {
    /**
     * 按钮名称
     *
     * @return {@link String}
     */
    String name();


    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     *
     * @return {@link String}
     */
    String size() default "sm";

    //----------------------- 以下为按钮事件 -----------------------

    /**
     * 按下按钮后的js命令
     *
     * @return {@link String}
     */
    String jsCmd() default "";

    /**
     * 跳转的url
     *
     * @return {@link String}
     */
    String jumpUrl() default "";

    /**
     * 一对多关联按钮
     *
     * @return {@link O2MJoinButton}
     */
    O2MJoinButton o2mJoinButton() default @O2MJoinButton(enable = false);

    /**
     * 多对多关联设置【新】
     */
    M2MRelation m2mRelation() default @M2MRelation(enable = false);

    /**
     * 多对多关联按钮
     *
     * @return {@link O2MJoinButton}
     */
    M2MJoinButton m2mJoinButton() default @M2MJoinButton(enable = false, mediumTableClass = Object.class);

    /**
     * java命令行
     *
     * @return {@link String}
     */
    JavaCmd javaCmd() default @JavaCmd(enable = false, beanClass = Object.class, methodName = "");

    /**
     * 下钻按钮【未启用】
     *
     * @return {@link String}
     */
    DrillDownButton drillDownButton() default @DrillDownButton(enable = false, fetchSql = "",
        thisField = "tid", targetField = "rid",
        mediumTableClass = Object.class, mediumThisField = "tid", mediumOtherField = "rid");


    @interface O2MJoinButton {

        /**
         * 连表查询
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> joinAnnoMainClazz() default Object.class;

        /**
         * 以哪个字段为条件【this】
         *
         * @return {@link String}
         */
        String joinThisClazzField() default "";


        /**
         * 以哪个字段为条件【target】
         *
         * @return {@link String}
         */
        String joinOtherClazzField() default "";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         *
         * @return {@link String}
         */
        String windowSize() default "xl";

        /**
         * 弹出窗口高度
         *
         * @return {@link String}
         */
        String windowHeight() default "700px";

        /**
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;

    }

    @interface M2MJoinButton {

        /**
         * 目标表
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> joinAnnoMainClazz() default Object.class;

        /**
         * SQL语句：? 为 joinThisClazzField的值
         * demo1: select user_id from sys_user_role where role_id = ?
         * demo2: select role_id from sys_user_role where user_id = ?
         *
         * @return {@link String}
         */
        String joinSql() default "";

        /**
         * 以哪个字段为条件【this】
         *
         * @return {@link String}
         */
        String joinThisClazzField() default "";


        /**
         * 中间表的类
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> mediumTableClass();


        /**
         * 中间表的字段【目标表】
         *
         * @return {@link String}
         */
        String mediumOtherField() default "";

        /**
         * 中间表的字段【本表】
         *
         * @return {@link String}
         */
        String mediumThisField() default "";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         *
         * @return {@link String}
         */
        String windowSize() default "xl";

        /**
         * 弹出窗口高度
         *
         * @return {@link String}
         */
        String windowHeight() default "700px";

        /**
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;
    }

    @interface JavaCmd {
        /**
         * bean类
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> beanClass();

        /**
         * 方法名称 参数必须是: Map<String,Object> props
         *
         * @return {@link String}
         */
        String methodName();

        /**
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;

    }

    @interface DrillDownButton {
        /**
         * 用于数据展示的查询
         * select * from sys_user t1 left join sys_org t2 where user_id = ?
         */
        String fetchSql();

        /**
         * 以哪个字段为条件【本表】
         */
        String thisField();

        /**
         * 以哪个字段为条件【目标表】
         */
        String targetField();

        /**
         * 中间表
         */
        Class<?> mediumTableClass();

        /**
         * 中间表 的字段【目标表】
         */
        String mediumOtherField();

        /**
         * 中间表 的字段【本表】
         */
        String mediumThisField();

        /**
         * 是否是树形结构
         */
        boolean isTree() default false;

        /**
         * 树形结构 的id字段
         */
        String treeIdField() default "id";

        /**
         * 树形结构 的父id字段
         */
        String treeParentIdField() default "pid";

        /**
         * 树形结构 的label字段
         */
        String treeLabelField() default "name";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         *
         * @return {@link String}
         */
        String windowSize() default "xl";

        /**
         * 弹出窗口高度
         *
         * @return {@link String}
         */
        String windowHeight() default "700px";

        /**
         * 是否启用
         */
        boolean enable() default true;
    }

    @interface M2MRelation {

        /**
         * 对象目标类
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> joinTargetClass() default Object.class;

        /**
         * 以哪个字段为条件【目标表】
         *
         * @return {@link String}
         */
        String joinTargetField() default "id";

        /**
         * SQL语句：? 为 joinThisClazzField的值
         * demo1: select user_id from sys_user_role where role_id = ?
         * demo2: select role_id from sys_user_role where user_id = ?
         *
         * @return {@link String}
         */
        String joinSql() default "";

        /**
         * 以哪个字段为条件【本】
         *
         * @return {@link String}
         */
        String joinThisField() default "id";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         *
         * @return {@link String}
         */
        String windowSize() default "xl";

        /**
         * 弹出窗口高度
         *
         * @return {@link String}
         */
        String windowHeight() default "700px";

        /**
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;
    }

    /**
     * 权限码
     * 默认为空，不进行权限控制
     */
    String permissionCode() default "";

}
