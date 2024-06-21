package site.sorghum.anno.solon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Post;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.BaseDictController;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;

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
    @ApiOperation("加载字典")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "sqlKey", value = "[模式一]字典的查询sql的key", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "annoClazz", value = "[模式二]字典值的加载类", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "idKey", value = "[模式二]值的key", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "labelKey", value = "[模式二]标签的key", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "optionAnnoClazz", value = "[模式三]字典的查询提供类", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "treeAnnoClazz", value = "[模式四]字典树的查询提供类", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "_extra", value = "额外参数", dataType = "Map<String,Object>", paramType = "body"),
        }
    )
    public AnnoResult<List<AnnoTreeDTO<String>>> loadDict(@Param String sqlKey,
                                                          @Param String annoClazz,
                                                          @Param String idKey,
                                                          @Param String labelKey,
                                                          @Param String optionAnnoClazz,
                                                          @Param String treeAnnoClazz,
                                                          @Param Map<String,Object> _extra) {
        if (_extra == null) {
            _extra = Collections.emptyMap();
        }
        return super.loadDict(sqlKey, annoClazz, idKey, labelKey, optionAnnoClazz, treeAnnoClazz, _extra);
    }


    @Mapping("/transOne")
    @Post
    @ApiOperation("翻译单个")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "idValue", value = "[模式二]查询值", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "annoClazz", value = "[模式二]字典值的加载类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "idKey", value = "[模式二]值的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "labelKey", value = "[模式二]标签的key", dataType = "String", paramType = "query")
        }
    )
    @Override
    public AnnoResult<String> transOne(@Param String annoClazz, @Param String idKey, @Param String labelKey, @Param String idValue) {
        return super.transOne(annoClazz, idKey, labelKey, idValue);
    }



}
