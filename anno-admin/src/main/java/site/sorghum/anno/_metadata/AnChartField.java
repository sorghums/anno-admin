package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.enums.AnnoChartType;
import site.sorghum.anno.chart.supplier.base.ChartSupplier;

@Data
@AllArgsConstructor
public class AnChartField {

    @ApiModelProperty(value = "图表ID [固定自动生成]", example = "1454d1as2d1as32")
    private String id;

    @ApiModelProperty(value = "图表标题", example = "今日访客")
    private String name;

    @ApiModelProperty(value = "图表类型", example = "AnnoChartType.NUMBER")
    private AnnoChartType type;

    @ApiModelProperty(value = "运行类", example = "TodayLoginSupplier.class")
    private Class<? extends ChartSupplier<?>> runSupplier;

    @ApiModelProperty(value = "排序", example = "1")
    private int order;

    @ApiModelProperty(value = "权限码" , example = "todayLoginChart")
    private String permissionCode;

    @ApiModelProperty(value = "颜色" , example = "blue")
    private String actionColor;

    @ApiModelProperty(value = "角标" , example = "日")
    private String action;

    public AnChartField(AnnoChartField annoChartField) {
        this.name = annoChartField.name();
        this.type = annoChartField.type();
        this.runSupplier = annoChartField.runSupplier();
        this.order = annoChartField.order();
        this.permissionCode = annoChartField.permissionCode();
        this.actionColor = annoChartField.actionColor();
        this.action = annoChartField.action();
    }
}
