package site.sorghum.anno.anno.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.enums.AnnoChartType;

/**
 * 图表响应
 *
 * @author qjw
 * @since 2024/02/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnChartResponse<T> {
    /**
     * 唯一编码
     */
    String id;

    /**
     * 名称
     */
    String name;

    /**
     * 类型
     */
    AnnoChartType type;

    /**
     * 值
     */
    T value;

    /**
     * 顺序
     */
    int order;
}
