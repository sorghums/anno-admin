package site.sorghum.anno.annotation;


import java.lang.annotation.*;

/**
 * Anno 忽略bytebuddy注入
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoIgnore {

}
