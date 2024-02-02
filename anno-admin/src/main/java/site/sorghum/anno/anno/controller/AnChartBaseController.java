package site.sorghum.anno.anno.controller;

import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnChart;

import java.util.Map;

/**
 * 图表控制器
 *
 * @author Qianjiawei
 * @since 2024/02/01
 */
public class AnChartBaseController {

    public AnnoResult<Map<Object,Object>> anChart(String clazz){
        AnChart anChart = AnChart.chartMap.get(clazz);
        if (anChart != null){
            return AnnoResult.succeed(JSONUtil.toBean(anChart,Map.class));
        }
        return AnnoResult.succeed();
    }

}
