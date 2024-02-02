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
    NUMBER("数值"),
    LINE("折线图"),
    BAR("柱状图"),
    RADAR("雷达图"),
    RING("环图"),
    PIE("饼图");

    private final String name;
}
