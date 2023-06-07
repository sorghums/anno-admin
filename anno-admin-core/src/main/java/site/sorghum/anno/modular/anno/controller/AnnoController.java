package site.sorghum.anno.modular.anno.controller;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDto;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.response.AnnoResult;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Result;
import org.noear.wood.IPage;

import java.util.List;

/**
 * Anno控制器
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Controller
@Mapping("/system/anno")
@Slf4j
public class AnnoController {

    @Inject
    AnnoService annoService;

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
                                         @Body JSONObject param,
                                         @Param String orderBy,
                                         @Param String orderDir,
                                         @Param String _cat) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        queryRequest.setClazz(aClass);
        queryRequest.setCat(_cat);
        queryRequest.setPage(page - 1);
        queryRequest.setPerPage(perPage);
        queryRequest.setParam(param.toJavaObject(aClass));
        queryRequest.setOrderBy(orderBy);
        queryRequest.setOrderDir(orderDir);
        return AnnoResult.from(Result.succeed(annoService.page(queryRequest)));
    }

    @Mapping("/{clazz}/save")
    @Post
    public <T> AnnoResult<T> save(@Path String clazz, @Body JSONObject param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        annoService.save(emptyStringIgnore(param).toJavaObject(aClass));
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/queryById")
    @Post
    public <T> AnnoResult<T> queryById(@Path String clazz,@Param String pkValue,@Param String _cat){
        String id = null;
        if (id == null){
            id = pkValue;
        }
        if (id == null){
            id = _cat;
        }
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        return AnnoResult.from(Result.succeed(annoService.queryById(aClass,id)));
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
        Class<?> aClass = AnnoClazzCache.get(clazz);
        annoService.removeById(aClass, id);
        return AnnoResult.from(Result.succeed());
    }

    /**
     * 通过ID 更新
     */
    @Mapping("/{clazz}/updateById")
    public <T> AnnoResult<T> updateById(@Path String clazz, @Body JSONObject param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        annoService.updateById(emptyStringIgnore(param).toJavaObject(aClass));
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@Path String clazz, @Body JSONObject param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        T javaObject = (T) emptyStringIgnore(param).toJavaObject(aClass);
        if (ReflectUtil.getFieldValue(javaObject, AnnoUtil.getPkField(aClass)) == null) {
            annoService.save(javaObject);
        } else {
            annoService.updateById(javaObject);
        }
        return AnnoResult.from(Result.succeed(javaObject));
    }

    @Mapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDto<String>>> annoTrees(@Path String clazz) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        List<AnnoTreeDto<String>> annoTreeDtos = annoService.annoTrees(aClass);
        return AnnoResult.from(Result.succeed(annoTreeDtos));
    }

    private JSONObject emptyStringIgnore(JSONObject param){
        JSONObject nParam = new JSONObject();
        for (String key : param.keySet()) {
            Object item = param.get(key);
            if (item instanceof String){
                String sItem = (String) item;
                if (StrUtil.isNotBlank(sItem)){
                    nParam.put(key,sItem);
                }
            }else {
                nParam.put(key, param.get(key));
            }
        }
        return nParam;
    }
}
