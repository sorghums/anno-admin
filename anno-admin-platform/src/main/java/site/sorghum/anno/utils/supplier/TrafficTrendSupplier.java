package site.sorghum.anno.utils.supplier;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.utils.supplier.base.MapListSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Named
@Slf4j
public class TrafficTrendSupplier extends MapListSupplier {

    @Db
    DbContext dbContext;

    @Override
    public List<Map<String, Object>> get() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Map<String, Object>> list;
        try {
            list = dbContext.sql("select hour(latest_time) as item, count(*) as itemCount from an_login_log where date(latest_time) = curdate() group by date_format(latest_time, '%Y-%m-%d'),item").getMapList();
        } catch (Exception e) {
            log.error("图表trafficTrend数据查询异常！" + e.getMessage());
            return null;
        }
        if (CollUtil.isEmpty(list)) {
            return null;
        }

        Map<Integer, Map<String, Object>> resultMap = new HashMap<>();
        for (Map<String, Object> entry : list) {
            int hour = Integer.parseInt(entry.get("item").toString());
            resultMap.put(hour, entry);
        }

        for (int i = 0; i <= 23; i++) {
            if (!resultMap.containsKey(i)) {
                int finalI = i;
                resultMap.put(i, new HashMap<>() {{
                    put("item", finalI);
                    put("itemCount", 0);
                }});
            }
        }

        List<Map<String, Object>> result = resultMap.values().stream().toList();

        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return result;
    }
}
