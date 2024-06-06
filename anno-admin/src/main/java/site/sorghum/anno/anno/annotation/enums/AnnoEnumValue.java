package site.sorghum.anno.anno.annotation.enums;

import java.lang.annotation.*;

/**
 * 枚举值
 *
 * @author Sorghum
 * @since 2024/06/05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoEnumValue {

}
