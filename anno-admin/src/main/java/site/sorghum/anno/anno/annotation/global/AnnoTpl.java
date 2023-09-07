package site.sorghum.anno.anno.annotation.global;


import java.lang.annotation.*;

/**
 * 外置页面
 *
 * @author Sorghum
 * @since 2023/09/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface AnnoTpl {
    /**
     *  页面名称
     *  如：helloWord.html
     *  会在resources/WEB-INF/anno-admin-ui/page/下寻找
     */
    String tplName() default "";

    /**
     * 解析的AnnoMain类
     */
    Class<?> tplClazz() default Object.class;

    /**
     * 是否启用
     */
    boolean enable() default true;
}
