package site.sorghum.anno.anno.annotation.clazz;

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
public @interface AnnoPermission {
    /**
     * 启用
     *
     * @return boolean
     */
    boolean enable() default true;

    /**
     * 基础权限名称(即查看权限)
     * 如：sys_org
     *
     * @return {@link String}
     */
    String baseCode();

    /**
     * 基础代码翻译
     *
     * @return {@link String}
     */
    String baseCodeTranslate();
}
