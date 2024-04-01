package site.sorghum.anno.spring.controller;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.BaseDictController;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;

import java.util.List;
import java.util.Map;


/**
 * dict控制器
 *
 * @author Sorghum
 * @since 2023/11/17
 */
@RestController
@RequestMapping(AnnoConstants.BASE_URL + "/amis/system/dict")
@Api(tags = "字典控制器")
public class DictController extends BaseDictController {
    @RequestMapping("/loadDict")
    @ApiOperation("加载字典")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "sqlKey", value = "[模式一]字典的查询sql的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "annoClazz", value = "[模式二]字典值的加载类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "idKey", value = "[模式二]值的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "labelKey", value = "[模式二]标签的key", dataType = "String", paramType = "query")
        }
    )
    public AnnoResult<List<AnnoTreeDTO<String>>> loadDict(@RequestBody Map<String, Object> dictParam) {
        return super.loadDict(MapUtil.getStr(dictParam, "sqlKey"), MapUtil.getStr(dictParam, "annoClazz"), MapUtil.getStr(dictParam, "idKey"), MapUtil.getStr(dictParam, "labelKey"));
    }




    @RequestMapping("/transOne")
    @ApiOperation("翻译单个")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "idValue", value = "[模式二]值", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "annoClazz", value = "[模式二]字典值的加载类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "idKey", value = "[模式二]值的key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "labelKey", value = "[模式二]标签的key", dataType = "String", paramType = "query")
        }
    )
    public AnnoResult<String> transOne(@RequestBody Map<String, Object> dictParam) {
        return super.transOne(MapUtil.getStr(dictParam, "annoClazz"), MapUtil.getStr(dictParam, "idKey"), MapUtil.getStr(dictParam, "labelKey")
            , MapUtil.getStr(dictParam, "idValue"));
    }
}
