package site.sorghum.anno._common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno._metadata.AnOrder;

import java.util.List;

@Data
public class PageRequest {

    @ApiModelProperty(value = "当前页[通1]",example = "1")
    Integer current = 1;

    @ApiModelProperty(value = "当前页[通1]",example = "1")
    Integer page = 1;

    @ApiModelProperty(value = "每页显示条数[通2]",example = "10")
    Integer perPage = 10;

    @ApiModelProperty(value = "每页显示条数[通2]",example = "10")
    Integer pageSize;

    @ApiModelProperty(value = "排序")
    List<AnOrder> anOrderList;

    @ApiModelProperty(value = "是否忽略多对多")
    Boolean ignoreM2m = false;

    @ApiModelProperty(value = "是否反转多对多")
    Boolean reverseM2m = false;

    @ApiModelProperty(value = "查询null值字段")
    List<String> nullKeys;

    @ApiModelProperty(value = "多对多中间表")
    String m2mMediumTableClass;

    @ApiModelProperty(value = "多对多中间表目标字段")
    String m2mMediumTargetField;

    @ApiModelProperty(value = "多对多中间表本表字段")
    String m2mMediumThisField;

    @ApiModelProperty(value = "多对多本表字段")
    String m2mJoinThisClazzField;

    @ApiModelProperty(value = "多对多本表字段值")
    String joinValue;
}
