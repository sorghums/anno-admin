package site.sorghum.anno.anno.annotation.field.type;

import java.lang.annotation.*;

/**
 * 代码类型
 *
 * @author Sorghum
 * @since 2024/07/08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoCodeType {

    String MODE =  "text/x-yaml";
    /**
     * 返回一个表示模式的字符串，默认为 "text/x-yaml"。
     *
     * @return 模式的字符串表示，用于指示配置文件的类型或格式。
     *
     */
    String mode() default MODE;

    String THEME = "dracula";
    /**
     * 获取主题名称的注解元素
     *
     */
    String theme() default THEME;
}
