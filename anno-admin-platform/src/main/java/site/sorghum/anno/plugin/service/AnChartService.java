package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.entity.response.AnChartResponse;

import java.util.List;

public interface AnChartService {


    /**
     * 获取图表数据
     *
     * @param type 类型
     * @return 对象
     */
    List<AnChartResponse<Object>> getChart(String clazz);
}
