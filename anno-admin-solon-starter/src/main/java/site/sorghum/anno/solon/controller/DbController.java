package site.sorghum.anno.solon.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.Result;
import org.noear.wood.IPage;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.BaseDbController;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;

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
public class DbController extends BaseDbController {

    /**
     * 分页查询
     *
     * @return {@link Result}<{@link IPage}<{@link T}>>
     */
    @Mapping("/{clazz}/page")
    @Post
    public <T> AnnoResult<IPage<T>> page(@Path String clazz,
                                         @Param int page,
                                         @Param int perPage,
                                         @Param String orderBy,
                                         @Param String orderDir,
                                         @Param(defaultValue = "false") boolean ignoreM2m,
                                         @Param(defaultValue = "false") boolean reverseM2m,
                                         @Body Map<String, Object> param) {

        return super.page(clazz, page, perPage, orderBy, orderDir, ignoreM2m, reverseM2m, param);
    }

    @Mapping("/{clazz}/save")
    @Post
    public <T> AnnoResult<T> save(@Path String clazz, @Body Map<String, Object> param) {
        return super.save(clazz, param);
    }

    @Mapping("/{clazz}/queryById")
    @Post
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
    public AnnoResult<String> removeById(@Path String clazz, @Param("id") String id) {
        return super.removeById(clazz, id);
    }

    /**
     * 通过ID 更新
     */
    @Mapping("/{clazz}/updateById")
    public <T> AnnoResult<T> updateById(@Path String clazz, @Body Map<String, Object> param) {
        return super.updateById(clazz, param);
    }

    @Mapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@Path String clazz, @Body Map<String, Object> param) {
        return super.saveOrUpdate(clazz, param);
    }

    @Mapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@Path String clazz, @Body Map<String, String> param) throws SQLException {
        return super.removeRelation(clazz, param);
    }

    @Mapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(@Path String clazz,
                                                               @Param(defaultValue = "false") boolean ignoreM2m,
                                                               @Param(defaultValue = "false") boolean reverseM2m,
                                                               @Body Map<String, String> param) {
        return super.annoTrees(clazz, ignoreM2m, reverseM2m, param);
    }

    @Mapping("/{clazz}/annoTreeSelectData")
    public <T> AnnoResult<Map<?, ?>> annoTreeSelectData(@Path String clazz,
                                                        @Param(defaultValue = "false") boolean ignoreM2m,
                                                        @Param(defaultValue = "false") boolean reverseM2m,
                                                        @Body Map<String, String> param) {
        return super.annoTreeSelectData(clazz, ignoreM2m, reverseM2m, param);
    }

    @Mapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@Path String clazz, @Body Map param, @Param boolean clearAll) {
        return super.addM2m(clazz, param, clearAll);
    }

    @Mapping(value = "runJavaCmd", method = MethodType.POST, consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@Body Map<String, String> map) throws ClassNotFoundException {
        return super.runJavaCmd(map);
    }

}
