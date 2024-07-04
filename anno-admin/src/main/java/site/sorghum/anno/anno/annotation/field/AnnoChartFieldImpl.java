package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.chart.supplier.ChartSupplier;
import site.sorghum.anno.anno.enums.AnnoChartType;

import java.lang.annotation.Annotation;

/**
 * 年表字段
 *
 * @author Sorghum Qjw
 * @since 2024/07/04
 */
public class AnnoChartFieldImpl implements AnnoChartField {
    /**
     * 名称
     */
    private String name = "";

    /**
     * 类型
     */
    private AnnoChartType type = AnnoChartType.NUMBER;

    /**
     * 数据供应商
     */
    private Class<? extends ChartSupplier<?>> runSupplier;

    /**
     * 顺序
     */
    private int order = 0;

    /**
     * 权限代码
     */
    private String permissionCode = "";

    /**
     * 颜色
     */
    private String actionColor = "blue";

    /**
     * 角标
     */
    private String action = "";

    @Override
    public String name() {
        return name;
    }

    @Override
    public AnnoChartType type() {
        return type;
    }

    @Override
    public Class<? extends ChartSupplier<?>> runSupplier() {
        return runSupplier;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public String permissionCode() {
        return permissionCode;
    }

    @Override
    public String actionColor() {
        return actionColor;
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoChartField.class;
    }
}