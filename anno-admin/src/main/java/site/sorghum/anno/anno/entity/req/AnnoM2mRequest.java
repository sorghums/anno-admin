package site.sorghum.anno.anno.entity.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno._metadata.AnnoMtm;

/**
 * m2m请求
 *
 * @author Sorghum
 * @since 2023/12/19
 */
@Data
public class AnnoM2mRequest {
    /**
     * 忽略m2m
     */
    @ApiModelProperty(value = "忽略m2m")
    boolean ignoreM2m;
    /**
     * 反向m2m
     */
    @ApiModelProperty(value = "反向m2m")
    boolean reverseM2m;
    /**
     * m2m的id
     */
    @ApiModelProperty(value = "m2m的id")
    String annoM2mId;

    public AnnoMtm getAnnoMtm() {
        return AnnoMtm.annoMtmMap.get(annoM2mId);
    }
}
