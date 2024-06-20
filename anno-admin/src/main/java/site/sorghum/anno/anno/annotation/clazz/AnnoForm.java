package site.sorghum.anno.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * AnnoForm注解
 * 用于标注主要的Form模板类
 *
 * @author sorghum
 * @since 2024/06/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoForm {

    /**
     * 名称
     *
     * @return {@link String}
     */
    String name();
}
