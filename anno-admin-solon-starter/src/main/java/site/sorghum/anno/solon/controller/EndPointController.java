package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.endpoint.EndPointBody;
import site.sorghum.anno.endpoint.EndPointFilter;

@Controller
@Mapping(AnnoConstants.BASE_URL + "/endPoint")
@Api(tags = "endPoint")
public class EndPointController {

    @Mapping
    @SaIgnore
    @ApiOperation(value = "endPoint")
    public AnnoResult<?> index(@Body EndPointBody body) {
        return AnnoResult.succeed(EndPointFilter.filter(body.getEndPointName(), body.getCommonParam()));
    }
}
