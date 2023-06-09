package site.sorghum.anno.modular.anno.controller;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.core.handle.Context;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Db
    DbContext dbContext;

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
        String m2mSql = annoService.m2mSql(param.toJavaObject(Map.class));
        if (StrUtil.isNotEmpty(m2mSql)) {
            String joinThisClazzField = param.getString("joinThisClazzField");
            queryRequest.setAndSql(joinThisClazzField + " in (" + m2mSql + ")");
        }
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
    public <T> AnnoResult<T> queryById(@Path String clazz, @Param String pkValue, @Param String _cat) {
        String id = null;
        if (id == null) {
            id = pkValue;
        }
        if (id == null) {
            id = _cat;
        }
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        return AnnoResult.from(Result.succeed(annoService.queryById(aClass, id)));
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

    @Mapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@Path String clazz, @Body JSONObject param) throws SQLException {
        String mediumOtherField = param.getString("mediumOtherField");
        String otherValue = param.getString("joinValue");
        String thisValue = param.getString(param.getString("joinThisClazzField"));
        String mediumThisField = param.getString("mediumThisField");
        Class<?> nowClass = AnnoClazzCache.get(clazz);
        Class<?> mediumCLass = AnnoClazzCache.get(param.getString("mediumTableClass"));
        String pkField = AnnoUtil.getPkField(nowClass);
        Map<String, Object> mediumMap =
                dbContext.table(AnnoUtil.getTableName(mediumCLass)).whereEq(mediumOtherField, otherValue).andEq(mediumThisField, thisValue).orderByDesc(pkField).selectMap(pkField);
        if (mediumMap.get(pkField) != null) {
            log.info("删除关联表数据：{}", mediumMap.get(pkField));
            annoService.removeById(mediumCLass, mediumMap.get(pkField).toString());
        }
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDto<String>>> annoTrees(@Path String clazz) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        List<AnnoTreeDto<String>> annoTreeDtos = annoService.annoTrees(aClass);
        return AnnoResult.from(Result.succeed(annoTreeDtos));
    }

    @Mapping("/{clazz}/m2mSelect")
    public <T> AnnoResult<JSONArray> m2mSelect(@Path String clazz, @Body Map<?, String> param) {
        // 主表
        Class<?> aClass = AnnoClazzCache.get(clazz);
        String m2mSql = annoService.m2mSql(param);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        queryRequest.setClazz((Class<T>) aClass);
        String joinThisClazzField = param.get("joinThisClazzField");
        queryRequest.setAndSql(joinThisClazzField + " not in ( " + m2mSql + " )");
        List<T> list = annoService.list(queryRequest);
        JSONArray listMap =JSON.parseArray(JSON.toJSONString(list));
        listMap.forEach(item -> {
            JSONObject jsonObject = (JSONObject) item;
            jsonObject.put("label", jsonObject.get(joinThisClazzField));
            jsonObject.put("value", jsonObject.get(joinThisClazzField));
        });
        return AnnoResult.succeed(listMap);
    }

    @Mapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@Path String clazz, @Body Map param) {
        // 主表
        Class<?> aClass = AnnoClazzCache.get(clazz);
        // 中间表
        String mediumTableClass = param.get("mediumTableClass").toString();
        Class<?> mediumClass = AnnoClazzCache.get(mediumTableClass);
        String mediumThisValues = param.get("selectTable").toString();
        String[] split = mediumThisValues.split(",");
        for (String mediumThisValue : split) {
            // 字段一
            String mediumThisField = param.get("mediumThisField").toString();
            // 字段二
            String mediumOtherField = param.get("mediumOtherField").toString();
            String mediumOtherValue = param.get("joinValue").toString();
            JSONObject addValue = new JSONObject() {{
                put(mediumThisField, mediumThisValue);
                put(mediumOtherField, mediumOtherValue);
            }};
            annoService.save(addValue.toJavaObject(mediumClass));
        }
        return AnnoResult.succeed();
    }

    private JSONObject emptyStringIgnore(JSONObject param) {
        JSONObject nParam = new JSONObject();
        for (String key : param.keySet()) {
            Object item = param.get(key);
            if (item instanceof String) {
                String sItem = (String) item;
                if (StrUtil.isNotBlank(sItem)) {
                    nParam.put(key, sItem);
                }
            } else {
                nParam.put(key, param.get(key));
            }
        }
        return nParam;
    }
}
