package site.sorghum.anno.anno.entity.req;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Anno树请求
 *
 * @author Sorghum
 * @since 2023/12/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnoTreesRequestAnno extends AnnoM2mRequest {
    /**
     * 树的value字段
     */
    @ApiModelProperty(value = "树的value字段")
    String idKey;
    /**
     * 树的label字段
     */
    @ApiModelProperty(value = "树的label字段")
    String labelKey;

    /**
     * 连接的值
     */
    @ApiModelProperty(value = "连接的值")
    String jointValue;

    public boolean hasFrontSetKey(){
        return StrUtil.isNotEmpty(idKey) && StrUtil.isNotEmpty(labelKey);
    }
}
