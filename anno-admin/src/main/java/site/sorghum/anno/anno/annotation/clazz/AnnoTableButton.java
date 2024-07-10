package site.sorghum.anno.anno.annotation.clazz;


import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.lang.annotation.*;

/**
 * Anno Table Button 注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoTableButton {
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
     * 按钮大小 	'default' | 'middle' | 'small' | 'large'
     *
     * @return {@link String}
     */
    String size() default "default";


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
     * java命令行
     *
     * @return {@link String}
     */
    AnnoButton.JavaCmd javaCmd() default @AnnoButton.JavaCmd(enable = false);

    /**
     * 权限码
     * 默认为空，不进行权限控制
     */
    String permissionCode() default "";

}
