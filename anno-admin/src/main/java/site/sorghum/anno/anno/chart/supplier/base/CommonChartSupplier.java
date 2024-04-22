package site.sorghum.anno.anno.chart.supplier.base;

import lombok.Data;

import java.util.List;

/**
 * 供应商
 * 饼图/柱状图/折线图等
 *
 * @author Sorghum
 * @since 2024/04/22
 */
public interface CommonChartSupplier extends ChartSupplier<List<CommonChartSupplier.PieChartResponse>>{

    @Data
    public class PieChartResponse {
        private Number itemCount;
        private String item;
    }


}
