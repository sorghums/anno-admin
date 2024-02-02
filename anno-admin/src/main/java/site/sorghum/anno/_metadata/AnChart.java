package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnChart {

    public static final Map<String, AnChart> chartMap = new HashMap<>();

    @ApiModelProperty(value = "菜单标题", example = "登录日志")
    private String name;

    @ApiModelProperty(value = "是否启用", example = "true")
    private boolean enable;

    @ApiModelProperty(value = "权限码", example = "AnLoginChart")
    private String permissionCode;

    @ApiModelProperty(value = "前端布局", example = "1,2,3")
    private int[] layout;

    @ApiModelProperty(value = "字段注解列表")
    private List<AnChartField> fields;

    public AnChart(AnnoChart annoChart) {
        this.name = annoChart.name();
        this.enable = annoChart.enable();
        this.permissionCode = annoChart.permissionCode();
        this.layout = annoChart.layout();
    }

}
