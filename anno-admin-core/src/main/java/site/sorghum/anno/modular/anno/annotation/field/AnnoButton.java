package site.sorghum.anno.modular.anno.annotation.field;

import java.lang.annotation.*;

/**
 * Anno Button 注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoButton {
    /**
     * 按钮名称
     *
     * @return {@link String}
     */
    String name() default "";


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
     * 多对多关联按钮
     *
     * @return {@link O2MJoinButton}
     */
    M2MJoinButton m2mJoinButton() default @M2MJoinButton(enable = false,mediumTableClass = Object.class);

    /**
     * java命令行，【暂不支持】
     *
     * @return {@link String}
     */
    JavaCmd JavaCmd() default @JavaCmd(enable = false, beanClass = Object.class, methodName = "");

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
         * 启用
         *
         * @return boolean
         */
        boolean enable() default true;

        /**
         * 中间表
         *
         * @return {@link String}
         */
        String mediumTable() default "";

        /**
         * 中间表的类
         * @return {@link Class}<{@link ?}>
         */
        Class<?> mediumTableClass();

        /**
         * 中间表的字段【本表】
         *
         * @return {@link String}
         */
        String mediumThisFiled() default "";

        /**
         * 中间表的字段【目标表】
         *
         * @return {@link String}
         */
        String mediumOtherField() default "";
    }

    @interface JavaCmd {
        /**
         * bean类
         *
         * @return {@link Class}<{@link ?}>
         */
        Class<?> beanClass();

        /**
         * 方法名称，必须是无参方法
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

}
