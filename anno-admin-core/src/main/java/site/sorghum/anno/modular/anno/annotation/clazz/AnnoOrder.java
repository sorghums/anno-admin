package site.sorghum.anno.modular.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * Anno 排序配置
 * <p>
 * 可配置默认排序
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoOrder {


    /**
     * 排序类型 0 asc,1 desc
     *
     * @return int
     */
    String orderType() default "asc";

    /**
     * 排序值，默认无
     *
     * @return {@link String}
     */
    String orderValue() default "";
}
