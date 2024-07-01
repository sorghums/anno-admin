package site.sorghum.anno.spring.controller;


import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import org.springframework.web.bind.annotation.*;
import site.sorghum.anno._annotations.AnnoSerialization;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.controller.BaseDbController;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.entity.req.AnnoPageRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreeListRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreesRequestAnno;
import site.sorghum.anno.anno.entity.response.AnChartResponse;
import site.sorghum.anno._common.entity.CommonParam;

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
@AnnoSerialization
@SuppressWarnings("unchecked")
public class DbController extends BaseDbController {

    /**
     * 分页查询
     *
     * @return {@link AnnoResult}<{@link IPage}<{@link T}>>
     */
    @PostMapping("/{clazz}/page")
    public <T> AnnoResult<AnnoPage<T>> page(@PathVariable String clazz,
                                            @RequestBody Map<String, Object> body) {

        return super.page(clazz, JSONUtil.toBean(body, AnnoPageRequestAnno.class), body);
    }

    @PostMapping("/{clazz}/save")
    public <T> AnnoResult<T> save(@PathVariable String clazz, @RequestBody HashMap param) {
        return super.save(clazz, param);
    }

    @PostMapping("/{clazz}/queryById")
    public <T> AnnoResult<T> queryById(@PathVariable String clazz,
                                       @RequestBody Map<String, Object> params) {
        String pkValue = MapUtil.getStr(params, "pkValue");
        String _cat = MapUtil.getStr(params, "_cat");
        return super.queryById(clazz, pkValue, _cat);
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return {@link AnnoResult}
     */
    @PostMapping("/{clazz}/removeById")
    public AnnoResult<String> removeById(@PathVariable String clazz, @RequestBody Map<String, Object> params) {
        return super.removeById(clazz, MapUtil.getStr(params, "id"));
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
                                                               @RequestBody(required = false) HashMap param) {
        if (param == null) {
            param = new HashMap<>();
        }
        return super.annoTrees(clazz, JSONUtil.toBean(param, AnnoTreesRequestAnno.class), JSONUtil.toBean(param, AnnoTreeListRequestAnno.class), param);
    }

    @RequestMapping("/{clazz}/annoTreeSelectData")
    public <T> AnnoResult<List<Object>> annoTreeSelectData(@PathVariable String clazz,
                                                           @RequestBody(required = false) Map<String, Object> param) {
        if (param == null) {
            param = new HashMap<>(0);
        }
        return super.annoTreeSelectData(clazz, JSONUtil.toBean(param, AnnoTreesRequestAnno.class), JSONUtil.toBean(param, AnnoTreeListRequestAnno.class), param);
    }

    @PostMapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@PathVariable String clazz, @RequestBody Map param) {
        if (param == null) {
            param = new HashMap<>();
        }
        boolean clearAll = MapUtil.getBool(param, "clearAll", false);
        return super.addM2m(clazz, param, clearAll);
    }

    @PostMapping(value = "/{clazz}/runJavaCmd", consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@PathVariable String clazz, @RequestBody CommonParam map) throws ClassNotFoundException {
        if (map == null) {
            map = new CommonParam();
        }
        return super.runJavaCmd(clazz, map);
    }

    @PostMapping(value = "/{clazz}/chartData", consumes = "application/json")
    public AnnoResult<List<AnChartResponse<Object>>> chartData(@PathVariable String clazz, @RequestBody CommonParam map) throws ClassNotFoundException {
        if (map == null) {
            throw new BizException("body参数不能为空");
        }
        return super.getChart(clazz, MapUtil.getStr(map,"annoChartFieldId"),map);
    }

    @PostMapping(value = "/{clazz}/oneChartData", consumes = "application/json")
    public AnnoResult<AnChartResponse<Object>> oneChartData(@PathVariable String clazz, @RequestBody CommonParam map) throws ClassNotFoundException {
        if (map == null) {
            throw new BizException("body参数不能为空");
        }
        return super.getOneChart(clazz, MapUtil.getStr(map,"annoChartFieldId"),map);
    }

}
