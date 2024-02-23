package site.sorghum.anno.chart;


import site.sorghum.anno.anno.entity.response.AnChartResponse;

import java.util.List;
import java.util.Map;

/**
 * 图表服务
 *
 * @author Sorghum Qjw
 * @since 2024/02/23
 */
public interface AnChartService {


    /**
     * 获取图表
     * 获取图表数据
     *
     * @param clazz   实体类
     * @param fieldId 字段id
     * @param params  params
     * @return 对象
     */
    List<AnChartResponse<Object>> getChart(String clazz, String fieldId, Map<String, Object> params);
}
