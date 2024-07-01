package site.sorghum.anno.anno.chart.supplier;


import site.sorghum.anno._common.entity.CommonParam;

/**
 * 图表供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@FunctionalInterface
public interface ChartSupplier<T> {


    /**
     * 提供器
     *
     * @param param 参数
     * @return {@link T}
     */
    T get(CommonParam param);
}
