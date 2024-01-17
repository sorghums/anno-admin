package site.sorghum.anno.method;

import site.sorghum.anno.method.route.DefaultMethodRoute;
import site.sorghum.anno.method.route.MethodRoute;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author songyinyin
 * @since 2024/1/16 20:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface MethodTemplate {

    /**
     * 定义组件执行的文件名，在 resources:method/ 目录下，需要唯一
     */
    String fileNamePrefix() default "";


    /**
     * 定义组件执行的路由
     */
    Class<? extends MethodRoute> route() default DefaultMethodRoute.class;
}
