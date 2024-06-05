package site.sorghum.anno.anno.annotation.clazz;


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
    JavaCmd javaCmd() default @JavaCmd(enable = false);

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
     * 权限码
     * 默认为空，不进行权限控制
     */
    String permissionCode() default "";

}
