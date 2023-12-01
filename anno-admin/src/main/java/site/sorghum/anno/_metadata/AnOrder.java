package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;

@Data
public class AnOrder {

    /**
     * 排序类型 asc, desc
     *
     * @see AnnoOrder#orderType()
     */
    @ApiModelProperty(value = "排序类型 asc, desc",example = "asc")
    private String orderType;

    /**
     * 排序
     *
     * @see AnnoOrder#orderValue()
     */
    @ApiModelProperty(value = "排序",example = "id")
    private String orderValue;
}
