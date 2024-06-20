package site.sorghum.anno.anno.annotation.field;


import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

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
     * 图标
     *
     * @return {@link String}
     */
    String icon() default "ant-design:appstore-filled";

    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     *
     * @return {@link String}
     */
    String size() default "sm";

    /**
     * 表单提供类
     * @return @{@link Class}
     */
    Class<? extends BaseForm> baseForm() default DefaultBaseForm.class;

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
    JavaCmd javaCmd() default @JavaCmd(enable = false);

    /**
     * 下钻按钮【未启用】
     *
     * @return {@link String}
     */
    DrillDownButton drillDownButton() default @DrillDownButton(enable = false, fetchSql = "",
        thisField = "tid", targetField = "rid",
        mediumTableClass = Object.class, mediumThisField = "tid", mediumOtherField = "rid");


    /**
     * 模板视图按钮
     */
    AnnoTpl annoTpl() default @AnnoTpl(enable = false);

    @interface O2MJoinButton {

        /**
         * 连表查询
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> targetClass() default Object.class;

        /**
         * 以哪个字段为条件【this】
         *
         * @return {@link String}
         */
        String thisJavaField() default "";


        /**
         * 以哪个字段为条件【target】
         *
         * @return {@link String}
         */
        String targetJavaField() default "";

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
        Class<?> joinTargetClazz() default Object.class;

        /**
         * 以哪个字段为条件【this】
         *
         * @return {@link String}
         */
        String joinThisClazzField() default "id";

        /**
         * 以哪个字段为条件【target】
         */
        String joinTargetClazzField() default "id";

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
        String mediumTargetField() default "";

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
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;
    }

    @interface JavaCmd {

        /**
         * 运行供应商
         *
         * @return {@link Class}<{@link ?extends} {@link JavaCmdSupplier}>
         */
        Class<?extends JavaCmdSupplier> runSupplier() default JavaCmdSupplier.class;

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
         * demo1: select user_id from an_user_role where role_id = ?
         * demo2: select role_id from an_user_role where user_id = ?
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
