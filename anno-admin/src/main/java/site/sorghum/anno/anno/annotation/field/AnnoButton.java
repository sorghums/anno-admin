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

    /**
     * 行数据回显：点击行按钮打开 baseForm 表单时，将当前行数据按映射回显到表单字段
     */
    RowDataEcho rowDataEcho() default @RowDataEcho(enable = false);

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
    M2MJoinButton m2mJoinButton() default @M2MJoinButton(enable = false, mediumTableClazz = Object.class);

    /**
     * java命令行
     *
     * @return {@link String}
     */
    JavaCmd javaCmd() default @JavaCmd(enable = false);


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
        Class<?> mediumTableClazz();


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

    /**
     * 行数据展示依赖条件
     * 表达式中的 annoDataForm 代表当前行数据
     */
    ShowBy showBy() default @ShowBy(enable = false);

    /**
     * 权限码
     * 默认为空，不进行权限控制
     */
    String permissionCode() default "";

    @interface ShowBy {
        /**
         * 是否启用展示条件
         */
        boolean enable() default true;

        /**
         * 展示条件表达式
         * 必须以 annoDataForm.xx 为变量的取值
         */
        String expr() default "true";
    }

    /**
     * 行数据回显配置
     */
    @interface RowDataEcho {
        /**
         * 是否启用回显
         */
        boolean enable() default true;

        /**
         * 回显字段映射
         *
         * @return {@link EchoField}[]
         */
        EchoField[] fields() default {};

        /**
         * 映射字段默认只读（可在 {@link EchoField#readonly()} 上按字段覆盖）
         */
        boolean readonly() default false;
    }

    /**
     * 回显字段映射：将当前行【父表】字段值回显到 baseForm 表单字段
     */
    @interface EchoField {
        /**
         * 父表（当前行）字段名
         *
         * @return {@link String}
         */
        String thisField();

        /**
         * 表单字段名，默认与 thisField 同名
         *
         * @return {@link String}
         */
        String formField() default "";

        /**
         * 该字段是否只读展示，未配置时取 {@link RowDataEcho#readonly()}
         */
        boolean readonly() default false;
    }

}
