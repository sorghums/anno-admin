package site.sorghum.anno._annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用Anno独有的序列化
 *
 * @author Sorghum
 * @since 2024/01/24
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoSerialization {
}
