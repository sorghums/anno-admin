package site.sorghum.anno.plugin.chart;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.map.MapUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.chart.supplier.base.CommonChartSupplier;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 流量趋势供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@Named
@Slf4j
public class TrafficTrendSupplier implements CommonChartSupplier {

    @Db
    DbContext dbContext;

    @Override
    public List<PieChartResponse> get(Map<String, Object> param) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<PieChartResponse> list = new ArrayList<>();
        Map<String, Object> returnMapData = new HashMap<>();
        DateTime now = DateUtil.date();
        int nowHour = DateUtil.hour(now, true) + 1;
        try {
            Date date = DateUtil.beginOfDay(now);
            List<Map<String, Object>> selectMap = dbContext.table("an_login_log").where("1=1").andGte("latest_time", date).selectMapList("id,latest_time");
            // 计算最近24小时，每个小时的登陆次数
            for (Map<String, Object> stringObjectMap : selectMap) {
                Date latestTime = MapUtil.getDate(stringObjectMap, "latest_time");
                int hour = DateUtil.hour(latestTime, true);
                String hourStr = String.valueOf(hour);
                // 有就+1
                if (returnMapData.containsKey(hourStr)) {
                    returnMapData.put(hourStr, MapUtil.getInt(returnMapData, hourStr) + 1);
                } else {
                    returnMapData.put(hourStr, 1);
                }
            }
        } catch (Exception e) {
            log.error("图表trafficTrend数据查询异常！" + e.getMessage());
            return null;
        }
        for (int i = 0; i < nowHour; i++) {
            PieChartResponse response = new PieChartResponse();
            response.setItem(i + "时");
            response.setItemCount(
                MapUtil.getInt(returnMapData, String.valueOf(i),0)
            );
            list.add(response);
        }
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return list;
    }
}
