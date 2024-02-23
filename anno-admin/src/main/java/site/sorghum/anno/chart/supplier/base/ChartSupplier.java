package site.sorghum.anno.chart.supplier.base;


import java.util.Map;

/**
 * 图表供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@FunctionalInterface
public interface ChartSupplier<T> {


    /**
     * 收到
     *
     * @param param 参数
     * @return {@link T}
     */
    T get(Map<String, Object> param);
}
