package site.sorghum.anno.solon.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.controller.BaseDictController;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * dict控制器
 *
 * @author Sorghum
 * @since 2023/11/17
 */
@Controller
@Mapping(AnnoConstants.BASE_URL + "/amis/system/dict")
@Api(tags = "字典控制器")
public class DictController extends BaseDictController {
    @Override
    @Mapping("/loadDict")
    @Post
    @Consumes("application/json")
    @ApiOperation("加载字典")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "sqlKey", value = "[模式一]字典的查询sql的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "annoClazz", value = "[模式二]字典值的加载类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "idKey", value = "[模式二]值的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "labelKey", value = "[模式二]标签的key", dataType = "String", paramType = "query")
        }
    )
    public AnnoResult<List<AnnoTreeDTO<String>>> loadDict(@Param String sqlKey, @Param String annoClazz, @Param String idKey, @Param String labelKey) {
        return super.loadDict(sqlKey, annoClazz, idKey, labelKey);
    }
}
