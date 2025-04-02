package site.sorghum.anno.solon.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.NvMap;
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
@Controller
@Api(tags = "功能控制器")
@Mapping(value = AnnoConstants.BASE_URL + "/system/config")
@Slf4j
public class AnEntityController extends AnEntityBaseController {

    @Mapping(value = "/anEntity/{clazz}")
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object, Object>> anEntity(@Path String clazz) {
        return super.getEntityMetadata(clazz);
    }

    @Mapping(value = "/defaultAddData/{clazz}")
    @ApiOperation(value = "获取默认【添加】数据", notes = "获取默认【添加】数据")
    public AnnoResult<Map<String, Object>> defaultAddData(
        @Path String clazz, @Body NvMap nvMap
    ) {
        List<String> columnDataIdList = new ArrayList<>();
        String columnDataIds = nvMap.get("columnDataIds");
        if (StrUtil.isNotBlank(columnDataIds)) {
            columnDataIdList = StrUtil.split(columnDataIds, ",");
        }
        return AnnoResult.succeed(
            super.getDefaultAddData(clazz, columnDataIdList)
        );
    }

}
