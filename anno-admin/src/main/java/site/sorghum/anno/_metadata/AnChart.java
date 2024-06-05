package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.anno.annotation.clazz.AnnoChart;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 图表
 *
 * @author Sorghum && Qjw
 * @since 2024/02/22
 */
@Data
@AllArgsConstructor
public class AnChart {

    /**
     * 布局
     */
    @ApiModelProperty(value = "前端布局", example = "1,2,3")
    private int[] layout;

    /**
     * 字段注解列表
     */
    @ApiModelProperty(value = "字段注解列表")
    private List<AnChartField> fields;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enable;

    public AnChart(AnnoChart annoChart) {
        this.enable = annoChart.enable();
        this.layout = annoChart.layout();
        this.fields = Arrays.stream(annoChart.chartFields()).map(AnChartField::new).peek(
            field -> {
                field.setId(MD5Util.digestHex(
                    field.getName() + field.getType() + field.getOrder()
                ));
            }
        ).sorted(Comparator.comparing(AnChartField::getOrder)).toList();
    }

}
