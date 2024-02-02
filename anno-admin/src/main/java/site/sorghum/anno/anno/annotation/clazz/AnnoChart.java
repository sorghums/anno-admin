package site.sorghum.anno.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * anno图表
 *
 * @author Qianjiawei
 * @since 2024/01/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface AnnoChart {

    String name();

    boolean enable() default true;

    String permissionCode();

    int[] layout() default {1,2,3};
}
