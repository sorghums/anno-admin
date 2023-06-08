package site.sorghum.anno.modular.anno.annotation.field;

import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;

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
     * 关联按钮
     *
     * @return {@link JoinButton}
     */
    JoinButton joinButton() default @JoinButton(enable = false);

    /**
     * java命令行，【暂不支持】
     *
     * @return {@link String}
     */
    JavaCmd JavaCmd() default @JavaCmd(enable = false, beanClass = Object.class, methodName = "");

    @interface JoinButton {

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
        String joinAnnoMainClazzField() default "";

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
