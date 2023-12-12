package site.sorghum.anno.spring.controller;


import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import org.springframework.web.bind.annotation.*;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.BaseDbController;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Anno控制器
 *
 * @author sorghum
 * @since 2023/05/20
 */
@RestController
@RequestMapping(AnnoConstants.BASE_URL + "/amis/system/anno")
@Slf4j
@SuppressWarnings("unchecked")
public class DbController extends BaseDbController {

    /**
     * 分页查询
     *
     * @return {@link AnnoResult}<{@link IPage}<{@link T}>>
     */
    @PostMapping("/{clazz}/page")
    public <T> AnnoResult<IPage<T>> page(@PathVariable String clazz,
                                         @RequestParam int page,
                                         @RequestParam int current,
                                         @RequestParam int perPage,
                                         @RequestParam int pageSize,
                                         @RequestParam(required = false) String orderBy,
                                         @RequestParam(required = false) String orderDir,
                                         @RequestParam(defaultValue = "false") boolean ignoreM2m,
                                         @RequestParam(defaultValue = "false") boolean reverseM2m,
                                         @RequestParam(required = false) String annoM2mId,
                                         @RequestParam(required = false) Map<String, Object> param) {

        return super.page(clazz, Math.max(page,current), Math.max(perPage,pageSize), orderBy, orderDir, ignoreM2m, reverseM2m, annoM2mId, param);
    }

    @PostMapping("/{clazz}/save")
    public <T> AnnoResult<T> save(@PathVariable String clazz, @RequestBody HashMap param) {
        return super.save(clazz, param);
    }

    @PostMapping("/{clazz}/queryById")
    public <T> AnnoResult<T> queryById(@PathVariable String clazz, @RequestParam(required = false) String pkValue, @RequestParam(required = false) String _cat) {
        return super.queryById(clazz, pkValue, _cat);
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return {@link AnnoResult}
     */
    @PostMapping("/{clazz}/removeById")
    public AnnoResult<String> removeById(@PathVariable String clazz, @RequestParam("id") String id) {
        return super.removeById(clazz, id);
    }

    /**
     * 通过ID 更新
     */
    @PostMapping("/{clazz}/updateById")
    public <T> AnnoResult<T> updateById(@PathVariable String clazz, @RequestBody HashMap param) {
        return super.updateById(clazz, param);
    }

    @PostMapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@PathVariable String clazz, @RequestBody HashMap param) {
        return super.saveOrUpdate(clazz, param);
    }

    @PostMapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@PathVariable String clazz, @RequestBody HashMap param) throws SQLException {
        return super.removeRelation(clazz, param);
    }

    @RequestMapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(@PathVariable String clazz,
                                                               @RequestParam(defaultValue = "false") boolean ignoreM2m,
                                                               @RequestParam(defaultValue = "false") boolean reverseM2m,
                                                               @RequestBody(required = false) HashMap param) {
        if (param == null) {
            param = new HashMap<>();
        }
        return super.annoTrees(clazz, ignoreM2m, reverseM2m, param);
    }

    @RequestMapping("/{clazz}/annoTreeSelectData")
    public <T> AnnoResult<List<Object>> annoTreeSelectData(@PathVariable String clazz,
                                                        @RequestParam(defaultValue = "false") boolean ignoreM2m,
                                                        @RequestParam(defaultValue = "false") boolean reverseM2m,
                                                        @RequestBody(required = false) Map<String, String> param) {
        if (param == null) {
            param = new HashMap<>();
        }
        return super.annoTreeSelectData(clazz, ignoreM2m, reverseM2m, param);
    }

    @PostMapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@PathVariable String clazz, @RequestBody Map param, @RequestParam(defaultValue = "false") boolean clearAll) {
        if (param == null) {
            param = new HashMap<>();
        }
        return super.addM2m(clazz, param, clearAll);
    }

    @PostMapping(value = "/${clazz}/runJavaCmd",consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@PathVariable String clazz,@RequestBody HashMap map) throws ClassNotFoundException {
        if (map == null) {
            map = new HashMap<>();
        }
        return super.runJavaCmd(clazz, map);
    }

}
