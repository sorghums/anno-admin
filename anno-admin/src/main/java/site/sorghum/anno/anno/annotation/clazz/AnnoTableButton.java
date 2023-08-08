package site.sorghum.anno.anno.annotation.clazz;


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
     * java命令行
     *
     * @return {@link String}
     */
    JavaCmd javaCmd() default @JavaCmd(enable = false, beanClass = Object.class, methodName = "");

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

    /**
     * 权限码
     * 默认为空，不进行权限控制
     */
    String permissionCode() default "";

}
