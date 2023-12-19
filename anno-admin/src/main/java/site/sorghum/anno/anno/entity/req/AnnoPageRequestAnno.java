package site.sorghum.anno.anno.entity.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno._metadata.AnOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面请求
 *
 * @author Sorghum
 * @since 2023/12/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnoPageRequestAnno extends AnnoM2mRequest {
    @ApiModelProperty(value = "页码", example = "1")
    int page;
    @ApiModelProperty(value = "每页数量", example = "10")
    int pageSize;
    @ApiModelProperty(value = "链接值")
    String joinValue;
    @ApiModelProperty(value = "查询Null值字段")
    List<String> nullKeys = new ArrayList<>();
    @ApiModelProperty(value = "排序")
    List<AnOrder> anOrderList =  new ArrayList<>();
}
