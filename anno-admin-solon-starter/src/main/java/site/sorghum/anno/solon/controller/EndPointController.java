package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.endpoint.EndPointFilter;

@Controller
@Mapping(AnnoConstants.BASE_URL + "/endPoint")
@Api(tags = "endPoint")
public class EndPointController {

    @Mapping("/{endPointName}")
    @SaIgnore
    @ApiOperation(value = "endPoint")
    public AnnoResult<?> index(@Path String endPointName, Context context) {
        CommonParam commonParam = new CommonParam();
        commonParam.putAll(context.paramMap().toValueMap());
        AnnoResult<Object> succeed = AnnoResult.succeed(EndPointFilter.filter(endPointName, commonParam));
        if (succeed.getData() instanceof byte[]) {
            Context.current().output((byte[]) succeed.getData());
            return null;
        }
        return succeed;
    }
}
