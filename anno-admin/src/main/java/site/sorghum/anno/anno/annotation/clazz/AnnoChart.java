package site.sorghum.anno.anno.annotation.clazz;

import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;

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

    /**
     * 查询表单提供类
     * @return @{@link Class}
     */
    Class<? extends BaseForm> searchForm() default DefaultBaseForm.class;
}
