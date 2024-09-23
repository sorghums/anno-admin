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

    String WINDOW_WIDTH = "960px";
    /**
     * 弹出窗口宽度
     *
     * @return {@link String}
     */
    String windowWidth() default WINDOW_WIDTH;

    String WINDOW_HEIGHT = "500px";
    /**
     * 弹出窗口高度
     *
     * @return {@link String}
     */
    String windowHeight() default WINDOW_HEIGHT;

    /**
     * 是否启用
     */
    boolean enable() default true;
}
