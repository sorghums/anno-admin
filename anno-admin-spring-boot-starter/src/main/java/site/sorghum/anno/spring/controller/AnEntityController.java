package site.sorghum.anno.spring.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.AnEntityBaseController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RestController
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AnEntityController extends AnEntityBaseController {

    @Override
    @RequestMapping(value = "/anEntity/{clazz}")
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object,Object>> anEntity(@PathVariable String clazz){
        return super.anEntity(clazz);
    }

    @RequestMapping(value = "/defaultAddData/{clazz}")
    @ApiOperation(value = "获取默认【添加】数据", notes = "获取默认【添加】数据")
    public AnnoResult<Map<String, Object>> defaultAddData(
        @PathVariable String clazz, @RequestBody Map<String,Object> nvMap
    ) {
        List<String> columnDataIdList = new ArrayList<>();
        String columnDataIds = MapUtil.getStr(nvMap, "columnDataIds");
        if (StrUtil.isNotBlank(columnDataIds)) {
            columnDataIdList = StrUtil.split(columnDataIds, ",");
        }
        return AnnoResult.succeed(
            super.defaultAddData(clazz, columnDataIdList)
        );
    }
}
