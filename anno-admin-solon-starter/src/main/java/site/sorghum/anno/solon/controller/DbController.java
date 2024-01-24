package site.sorghum.anno.solon.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Consumes;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.Result;
import org.noear.wood.IPage;
import site.sorghum.anno._annotations.AnnoSerialization;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.BaseDbController;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.entity.req.AnnoPageRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreeListRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreesRequestAnno;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Anno控制器
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Controller
@Mapping(AnnoConstants.BASE_URL + "/amis/system/anno")
@Slf4j
@SaIgnore
@Api(tags = "数据控制器")
@AnnoSerialization
public class DbController extends BaseDbController {

    @Mapping("/sql2tree")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "sql转树", notes = "sql转树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sqlKey", value = "sqlKey", example = "a12345", required = true, dataType = "String", paramType = "query"),
    })
    public AnnoResult<List<AnnoTreeDTO<String>>> sql2tree(@Body Map<String, String> param) {
        return super.querySqlTree(param.get("sqlKey"));
    }

    /**
     * 分页查询
     *
     * @return {@link Result}<{@link IPage}<{@link T}>>
     */
    @Override
    @Mapping("/{clazz}/page")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "[[entity]]", value = "其他查询的表的字段", paramType = "query"),}
    )
    public <T> AnnoResult<AnnoPage<T>> page(@Path String clazz,
                                            @Body AnnoPageRequestAnno pageRequest,
                                            @Body Map<String, Object> param) {
        return super.page(clazz, pageRequest, param);
    }

    @Mapping("/{clazz}/save")
    @Consumes("application/json")
    @Post
    @ApiOperation(value = "保存", notes = "保存")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "[[entity]]", value = "实体类的值", required = true, dataType = "Object", paramType = "query"),}
    )
    public <T> AnnoResult<T> save(@Path String clazz, @Body Map<String, Object> param) {
        return super.save(clazz, param);
    }

    @Mapping("/{clazz}/queryById")
    @Post
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @Consumes("application/json")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "pkValue", value = "主键值", required = true, dataType = "String", paramType = "query")}
    )
    public <T> AnnoResult<T> queryById(@Path String clazz, @Param String pkValue, @Param String _cat) {
        return super.queryById(clazz, pkValue, _cat);
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return {@link Result}
     */
    @Mapping("/{clazz}/removeById")
    @Post
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @Consumes("application/json")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "id", value = "主键值", required = true, dataType = "String", paramType = "query")}
    )
    public AnnoResult<String> removeById(@Path String clazz, @Param("id") String id) {
        return super.removeById(clazz, id);
    }

    /**
     * 通过ID 更新
     */
    @Mapping("/{clazz}/updateById")
    @Post
    @ApiOperation(value = "通过ID 更新", notes = "通过ID 更新")
    @Consumes("application/json")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "[[entity]]", value = "实体类的值", required = true, dataType = "Object", paramType = "query"),}
    )
    public <T> AnnoResult<T> updateById(@Path String clazz, @Body Map<String, Object> param) {
        return super.updateById(clazz, param);
    }

    @Mapping("/{clazz}/saveOrUpdate")
    @Post
    @ApiOperation(value = "保存或更新", notes = "保存或更新")
    @Consumes("application/json")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "[[entity]]", value = "实体类的值", required = true, dataType = "Object", paramType = "query"),}
    )
    public <T> AnnoResult<T> saveOrUpdate(@Path String clazz, @Body Map<String, Object> param) {
        return super.saveOrUpdate(clazz, param);
    }

    @Mapping("/{clazz}/remove-relation")
    @Post
    @ApiOperation(value = "删除多对多关系", notes = "删除多对多关系")
    @Consumes("application/json")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "annoM2mId", value = "多对多数据ID"),
            @ApiImplicitParam(name = "targetJoinValue", value = "目标值", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thisJoinValue", value = "本表值,是个数组 ['1',['2']]", required = true, dataType = "List", paramType = "query"),}
    )
    public <T> AnnoResult<T> removeRelation(@Path String clazz, @Body Map<String, Object> param) throws SQLException {
        return super.removeRelation(clazz, param);
    }

    @Override
    @Mapping("/{clazz}/annoTrees")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "获取树形结构", notes = "获取树形结构")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "ignoreM2m", value = "是否忽略多对多", example = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "reverseM2m", value = "是否反转多对多", example = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "annoM2mId", value = "多对多数据ID"),
            @ApiImplicitParam(name = "joinValue", value = "多对多本表字段值"),
            @ApiImplicitParam(name = "idKey", value = "树的value字段"),
            @ApiImplicitParam(name = "labelKey", value = "树的label字段"),}
    )
    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(@Path String clazz,
                                                               @Body AnnoTreesRequestAnno annoTreesRequest,
                                                               @Body AnnoTreeListRequestAnno annoTreeListRequestAnno,
                                                               @Body Map<String, Object> param) {
        return super.annoTrees(clazz, annoTreesRequest, annoTreeListRequestAnno, param);
    }

    @Override
    @Mapping("/{clazz}/annoTreeSelectData")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "获取树形已选择的收据", notes = "获取树形结构")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "ignoreM2m", value = "是否忽略多对多", example = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "reverseM2m", value = "是否反转多对多", example = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "annoM2mId", value = "多对多数据ID"),
            @ApiImplicitParam(name = "joinValue", value = "多对多本表字段值"),
            @ApiImplicitParam(name = "idKey", value = "树的value字段"),
            @ApiImplicitParam(name = "labelKey", value = "树的label字段"),}
    )
    public <T> AnnoResult<List<Object>> annoTreeSelectData(@Path String clazz,
                                                           @Body AnnoTreesRequestAnno annoTreesRequest,
                                                           @Body AnnoTreeListRequestAnno treeListRequestAnno,
                                                           @Body Map<String, Object> param) {
        return super.annoTreeSelectData(clazz, annoTreesRequest, treeListRequestAnno, param);
    }

    @Mapping("/{clazz}/addM2m")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "添加多对多关系", notes = "添加多对多关系")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "clearAll", value = "是否清空所有关系", example = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "m2mMediumTableClass", value = "多对多中间表", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "m2mMediumTargetField", value = "多对多中间表目标字段", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "m2mMediumThisField", value = "多对多中间表本表字段", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "targetJoinValue", value = "目标值", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thisJoinValue", value = "本表值", required = true, dataType = "String", paramType = "query"),}
    )
    public <T> AnnoResult<String> addM2m(@Path String clazz, @Body Map param, @Param boolean clearAll) {
        return super.addM2m(clazz, param, clearAll);
    }

    @Mapping(value = "/{clazz}/runJavaCmd", method = MethodType.POST, consumes = "application/json")
    @Post
    @Consumes("application/json")
    @ApiOperation(value = "执行java代码", notes = "执行java代码")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(name = "annoJavaCmdId", value = "anno运行java的命令ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "[[param]]", value = "参数", required = true, dataType = "String", paramType = "query"),
        }
    )
    public AnnoResult<String> runJavaCmd(@Path String clazz, @Body Map<String, String> map) throws ClassNotFoundException {
        return super.runJavaCmd(clazz, map);
    }

}
