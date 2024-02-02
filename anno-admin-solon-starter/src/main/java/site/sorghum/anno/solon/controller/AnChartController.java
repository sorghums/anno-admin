package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.AnChartBaseController;

import java.util.Map;

@Controller
@Api(tags = "图表控制器")
@Mapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AnChartController extends AnChartBaseController {

    @Override
    @Mapping(value = "/anChart/{clazz}")
    @SaIgnore
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object,Object>> anChart(String clazz) {
        return super.anChart(clazz);
    }
}
