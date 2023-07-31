package site.sorghum.anno.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 是否是主类
 *
 * @author Sorghum
 * @since 2023/07/31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Primary {
    boolean value() default true;
}
