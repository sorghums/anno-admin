package site.sorghum.anno.plugin.chart;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.chart.supplier.base.CommonChartSupplier;

import java.util.List;
import java.util.Map;

/**
 * 接入设备供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@Named
@Slf4j
public class AccessDeviceSupplier implements CommonChartSupplier {

    @Db
    DbContext dbContext;

    @Override
    public List<PieChartResponse> get(Map<String, Object> param) {
        try {
            List<Map<String, Object>> mapList = dbContext.sql("select device as item,count(*) as itemCount from an_login_log group by device").getMapList();
            return JSONUtil.toBeanList(mapList,PieChartResponse.class);
        } catch (Exception e) {
            log.error("图表accessDevice数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
