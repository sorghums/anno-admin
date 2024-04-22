package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.enums.AnnoChartType;
import site.sorghum.anno.anno.chart.supplier.ChartSupplier;

import java.lang.annotation.*;

/**
 * 年表字段
 *
 * @author Sorghum Qjw
 * @since 2024/02/23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface AnnoChartField {

    /**
     * 名称
     *
     * @return {@link String}
     */
    String name() default "";

    /**
     * 类型
     *
     * @return {@link AnnoChartType}
     */
    AnnoChartType type() default AnnoChartType.NUMBER;

    /**
     * 数据供应商
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link ChartSupplier}<{@link ?}>>
     */
    Class<? extends ChartSupplier<?>> runSupplier();

    /**
     * 顺序
     *
     * @return int
     */
    int order() default 0;

    /**
     * 权限代码
     *
     * @return {@link String}
     */
    String permissionCode() default "";

    /**
     * 颜色
     *
     * @return {@link String}
     */
    String actionColor() default "blue";

    /**
     * 角标
     *
     * @return {@link String}
     */
    String action() default "";
}
