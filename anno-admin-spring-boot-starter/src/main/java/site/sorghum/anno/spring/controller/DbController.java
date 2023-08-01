package site.sorghum.anno.spring.controller;


import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import org.springframework.web.bind.annotation.*;
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
@RestController
@RequestMapping("/system/anno")
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
                                         @RequestParam(required = false) int page,
                                         @RequestParam(required = false) int perPage,
                                         @RequestParam(required = false) String orderBy,
                                         @RequestParam(required = false) String orderDir,
                                         @RequestParam(required = false) boolean ignoreM2m,
                                         @RequestParam(required = false) boolean reverseM2m,
                                         @RequestParam(required = false) Map<String, Object> param) {

        return super.page(clazz, page, perPage, orderBy, orderDir, ignoreM2m, reverseM2m, param);
    }

    @PostMapping("/{clazz}/save")
    public <T> AnnoResult<T> save(@PathVariable String clazz, @RequestBody Map<String, Object> param) {
        return super.save(clazz, param);
    }

    @PostMapping("/{clazz}/queryById")
    public <T> AnnoResult<T> queryById(@PathVariable String clazz, @RequestParam String pkValue, @RequestParam String _cat) {
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
    public <T> AnnoResult<T> updateById(@PathVariable String clazz, @RequestBody Map<String, Object> param) {
        return super.updateById(clazz, param);
    }

    @PostMapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@PathVariable String clazz, @RequestBody Map<String, Object> param) {
        return super.saveOrUpdate(clazz, param);
    }

    @PostMapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@PathVariable String clazz, @RequestBody Map<String, String> param) throws SQLException {
        return super.removeRelation(clazz, param);
    }

    @PostMapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(@PathVariable String clazz,
                                                               @RequestParam boolean ignoreM2m,
                                                               @RequestParam boolean reverseM2m,
                                                               @RequestBody Map<String, String> param) {
        return super.annoTrees(clazz, ignoreM2m, reverseM2m, param);
    }

    @PostMapping("/{clazz}/annoTreeSelectData")
    public <T> AnnoResult<Map<?, ?>> annoTreeSelectData(@PathVariable String clazz,
                                                        @RequestParam boolean ignoreM2m,
                                                        @RequestParam boolean reverseM2m,
                                                        @RequestBody Map<String, String> param) {
        return super.annoTreeSelectData(clazz, ignoreM2m, reverseM2m, param);
    }

    @PostMapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@PathVariable String clazz, @RequestBody Map param, @RequestParam boolean clearAll) {
        return super.addM2m(clazz, param, clearAll);
    }

    @PostMapping(value = "runJavaCmd",consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@RequestBody Map<String, String> map) throws ClassNotFoundException {
        return super.runJavaCmd(map);
    }

}
