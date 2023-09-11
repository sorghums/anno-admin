package site.sorghum.anno.anno.annotation.common;


import site.sorghum.anno.anno.tpl.DefaultAnTplAction;

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
     *  页面名称
     *  如：helloWord.html
     *  会在resources/WEB-INF/templates下寻找
     */
    String tplName() default "";

    /**
     * 解析的Action类
     */
    Class<? extends DefaultAnTplAction> tplClazz() default DefaultAnTplAction.class;

    /**
     * 弹出窗口大小
     * xs、sm、md、lg、xl、full
     *
     * @return {@link String}
     */
    String windowSize() default "xl";

    /**
     * 弹出窗口高度
     *
     * @return {@link String}
     */
    String windowHeight() default "700px";

    /**
     * 是否启用
     */
    boolean enable() default true;
}
