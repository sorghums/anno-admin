package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.AnChartBaseController;

import java.util.Map;

/**
 * 图表控制器
 *
 * @author Qianjiawei
 * @since 2024/02/02
 */
@RestController
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AnChartController extends AnChartBaseController {

    @Override
    @RequestMapping(value = "/anChart/{clazz}")
    @SaIgnore
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object,Object>> anChart(String clazz) {
        return super.anChart(clazz);
    }
}
