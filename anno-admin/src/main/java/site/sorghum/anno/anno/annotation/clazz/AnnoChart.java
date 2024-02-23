package site.sorghum.anno.anno.annotation.clazz;

import site.sorghum.anno.anno.annotation.field.AnnoChartField;

import java.lang.annotation.*;

/**
 * anno图表
 *
 * @author Qianjiawei
 * @since 2024/01/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface AnnoChart {
    /**
     * 启用
     *
     * @return boolean
     */
    boolean enable() default true;

    /**
     * 布局
     *
     * @return {@link int[]}
     */
    int[] layout() default {1,2,3};

    /**
     * 图表字段
     *
     * @return {@link AnnoChartField[]}
     */
    AnnoChartField[] chartFields() default {};
}
