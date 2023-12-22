package site.sorghum.anno.anno.annotation.common;


import site.sorghum.anno.anno.tpl.BaseTplRender;

import java.lang.annotation.*;

/**
 * 外置页面
 *
 * @author Sorghum
 * @since 2023/09/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface AnnoTpl {
    /**
     * 解析的Action类
     */
    Class<? extends BaseTplRender> tplClazz() default BaseTplRender.class;

    /**
     * 弹出窗口宽度
     *
     * @return {@link String}
     */
    String windowWidth() default "960px";

    /**
     * 弹出窗口高度
     *
     * @return {@link String}
     */
    String windowHeight() default "800px";

    /**
     * 是否启用
     */
    boolean enable() default true;
}
