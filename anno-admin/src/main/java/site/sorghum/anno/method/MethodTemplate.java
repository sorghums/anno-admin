package site.sorghum.anno.method;

import site.sorghum.anno.method.route.DefaultMethodRoute;
import site.sorghum.anno.method.route.MethodRoute;

import java.lang.annotation.*;

/**
 * @author songyinyin
 * @since 2024/1/16 20:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface MethodTemplate {

    /**
     * 定义组件执行的子文件夹，在 resources:method/ 目录下
     */
    String ruleDir() default "";


    /**
     * 需要加载多个文件夹时，可以使用此属性。与 ruleDir 互斥
     */
    Class<? extends MethodRoute> route() default DefaultMethodRoute.class;
}
