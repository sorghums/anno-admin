package site.sorghum.anno.plugin.chart;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.entity.CommenParam;
import site.sorghum.anno._common.entity.IgnoreCaseHashMap;
import site.sorghum.anno.anno.chart.supplier.CommonChartSupplier;

import java.util.List;

/**
 * 访问操作系统供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@Named
@Slf4j
public class AccessOsSupplier implements CommonChartSupplier {

    @Db
    DbContext dbContext;

    @Override
    public List<PieChartResponse> get(CommenParam param) {
        try {
            List<IgnoreCaseHashMap> mapList = dbContext.sql("select os as item,count(*) as itemCount from an_login_log group by os")
                .getMapList()
                .stream()
                .map(IgnoreCaseHashMap::new)
                .toList();
            return mapList.stream().map(m -> {
                PieChartResponse pie = new PieChartResponse();
                pie.setItem(m.getString("item"));
                pie.setItemCount(m.getInteger("itemCount"));
                return pie;
            }).toList();
        } catch (Exception e) {
            log.error("图表accessOs数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
