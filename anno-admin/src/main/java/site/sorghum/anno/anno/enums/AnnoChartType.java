package site.sorghum.anno.anno.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图表类型
 *
 * @author Qianjiawei
 * @since 2024/01/25
 */
@AllArgsConstructor
@Getter
public enum AnnoChartType {
    NUMBER("number", "数值"),
    LINE("line", "折线图"),
    BAR("bar", "柱状图"),
    RADAR("radar", "雷达图"),
    RING("ring", "环图"),
    PIE("pie", "饼图");

    private final String code;
    private final String name;
}
