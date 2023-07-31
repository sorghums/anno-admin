package site.sorghum.anno.anno.annotation.global;

import java.lang.annotation.*;

/**
 * AnnoMain注解
 * 用于标注主要的模板类
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoScan {
   String[] scanPackage() default {};
}
