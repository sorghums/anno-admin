package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.endpoint.EndPointBody;
import site.sorghum.anno.endpoint.EndPointFilter;

@RestController
@RequestMapping(AnnoConstants.BASE_URL + "/endPoint")
@Api(tags = "endPoint")
public class EndPointController {

    @PostMapping
    @SaIgnore
    @ApiOperation(value = "endPoint")
    public AnnoResult<?> index(@RequestBody EndPointBody body) {
        return AnnoResult.succeed(EndPointFilter.filter(body.getEndPointName(), body.getCommonParam()));
    }
}
