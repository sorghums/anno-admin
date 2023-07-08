package site.sorghum.anno.modular.anno.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.Result;
import org.noear.wood.DbContext;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.RemoveParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.response.AnnoResult;
import site.sorghum.anno.util.CryptoUtil;
import site.sorghum.anno.util.JSONUtil;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
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

    @Inject
    DbService dbService;

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
                                         @Param boolean ignoreM2m,
                                         @Param boolean reverseM2m,
                                         @Body Map<String, String> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        param = emptyStringIgnore(param);
        queryRequest.setClazz(aClass);
        queryRequest.setPage(page - 1);
        queryRequest.setPerPage(perPage);
        queryRequest.setParam(JSONUtil.parseObject(param, aClass));
        queryRequest.setOrderBy(orderBy);
        queryRequest.setOrderDir(orderDir);
        String m2mSql = annoService.m2mSql(param);
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField").toString();
            queryRequest.setAndSql(joinThisClazzField + inPrefix + m2mSql + ")");
        }
        List<DbCondition> dbConditions = DbCondition.simpleEntity2conditions(param, aClass);
        if (queryRequest.getAndSql() != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(queryRequest.getAndSql()).build());
        }
        IPage<T> pageRes = dbService.page(new TableParam<T>() {{
            setClazz(aClass);
            setTableName(AnnoUtil.getTableName(aClass));
            setRemoveParam(new RemoveParam(){{setLogic(true);}});
        }}, dbConditions, new PageParam(page, perPage));
        return AnnoResult.succeed(pageRes);
    }

    @Mapping("/{clazz}/save")
    @Post
    public <T> AnnoResult<T> save(@Path String clazz, @Body Map<String,Object> param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        annoService.save(JSONUtil.parseObject(emptyStringIgnore(param),aClass));
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
    public <T> AnnoResult<T> updateById(@Path String clazz, @Body Map<String,Object> param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        annoService.updateById(JSONUtil.parseObject(emptyStringIgnore(param),aClass));
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@Path String clazz, @Body Map<String,Object> param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        T javaObject = (T) JSONUtil.parseObject(emptyStringIgnore(param),aClass);
        if (ReflectUtil.getFieldValue(javaObject, AnnoUtil.getPkField(aClass)) == null) {
            annoService.save(javaObject);
        } else {
            annoService.updateById(javaObject);
        }
        return AnnoResult.from(Result.succeed(javaObject));
    }

    @Mapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@Path String clazz, @Body Map<String,String> param) throws SQLException {
        String mediumOtherField = param.get("mediumOtherField");
        String otherValue = param.get("joinValue");
        String thisValue = param.get(param.get("joinThisClazzField"));
        String mediumThisField = param.get("mediumThisField");
        Class<?> mediumCLass = AnnoClazzCache.get(param.get("mediumTableClass"));
        annoService.removeByKvs(mediumCLass, CollUtil.newArrayList(
                new Tuple(mediumOtherField, otherValue),
                new Tuple(mediumThisField, thisValue)
        ));
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/annoTrees")
    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(@Path String clazz,
                                                               @Param boolean ignoreM2m,
                                                               @Param boolean reverseM2m,
                                                               @Body Map<String, String> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        queryRequest.setClazz(aClass);
        String m2mSql = annoService.m2mSql(param);
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField");
            queryRequest.setAndSql(joinThisClazzField + inPrefix + m2mSql + ")");
        }
        List<AnnoTreeDTO<String>> annoTreeDTOS = annoService.annoTrees(queryRequest);
        return AnnoResult.from(Result.succeed(annoTreeDTOS));
    }

    @Mapping("/{clazz}/annoTreeSelectDatas")
    public <T> AnnoResult<Map<?, ?>> annoTreeSelectDatas(@Path String clazz,
                                                         @Param boolean ignoreM2m,
                                                         @Param boolean reverseM2m,
                                                         @Body Map<String, String> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        queryRequest.setClazz(aClass);
        String m2mSql = annoService.m2mSql(param);
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField");
            queryRequest.setAndSql(joinThisClazzField + inPrefix + m2mSql + ")");
        }
        List<T> list = annoService.list(queryRequest);
        if (list == null || list.isEmpty()) {
            return AnnoResult.from(Result.succeed(Collections.emptyMap()));
        }
        List<Object> datas = list.stream().map(item -> ReflectUtil.getFieldValue(item, AnnoUtil.getPkField(aClass))).collect(Collectors.toList());
        return AnnoResult.from(Result.succeed(MapUtil.of("m2mTree", datas)));
    }

    @Mapping("/{clazz}/m2mSelect")
    public <T> AnnoResult<List<Map<String,Object>>> m2mSelect(@Path String clazz, @Body Map<?, String> param) {
        // 主表
        Class<?> aClass = AnnoClazzCache.get(clazz);
        String m2mSql = annoService.m2mSql(param);
        QueryRequest<T> queryRequest = new QueryRequest<>();
        queryRequest.setClazz((Class<T>) aClass);
        String joinThisClazzField = param.get("joinThisClazzField");
        queryRequest.setAndSql(joinThisClazzField + " not in ( " + m2mSql + " )");
        List<T> list = annoService.list(queryRequest);
        List<Map<String,Object>> maps = CollUtil.newArrayList();
        list.forEach(item -> {
            Map<String,Object> map;
            if (item instanceof Map){
                map = (Map<String, Object>) item;
            }else {
                map = JSONUtil.parseObject(item, Map.class);
            }
            map.put("label", map.get(joinThisClazzField));
            map.put("value", map.get(joinThisClazzField));
            maps.add(map);
        });
        return AnnoResult.succeed(maps);
    }

    @Mapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@Path String clazz, @Body Map param,@Param boolean clearAll) {
        // 主表
        Class<?> aClass = AnnoClazzCache.get(clazz);
        // 中间表
        String mediumTableClass = param.get("mediumTableClass").toString();
        Class<?> mediumClass = AnnoClazzCache.get(mediumTableClass);
        String[] split;
        Object ids = param.get("ids");
        // 字段一
        String mediumThisField = param.get("mediumThisField").toString();
        // 字段二
        String mediumOtherField = param.get("mediumOtherField").toString();
        String mediumOtherValue = param.get("joinValue").toString();
        if (ids instanceof List){
            List<String> idList =  (List)ids;
            split = idList.toArray(new String[idList.size()]);
        }else {
            String mediumThisValues = ids.toString();
            split = mediumThisValues.split(",");
        }
        if (clearAll) {
            annoService.removeByKvs(mediumClass, ListUtil.of(new Tuple(mediumOtherField, mediumOtherValue)));
        }
        for (String mediumThisValue : split) {
            Map<String,Object> addValue = new HashMap<String,Object>() {{
                put(mediumThisField, mediumThisValue);
                put(mediumOtherField, mediumOtherValue);
            }};

            annoService.save(JSONUtil.parseObject(addValue,mediumClass));
        }
        return AnnoResult.succeed();
    }

    @Mapping(value = "runJavaCmd",method = MethodType.POST,consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@Body Map<String ,String> map) throws ClassNotFoundException {
        map.put("clazz", CryptoUtil.decrypt(map.get("clazz")));
        map.put("method", CryptoUtil.decrypt(map.get("method")));
        Object bean = Solon.context().getBean(
                Class.forName(map.get("clazz"))
        );
        ReflectUtil.invoke(bean, map.get("method"),map);
        return AnnoResult.succeed("执行成功");
    }

    private Map emptyStringIgnore(Map<String,?> param) {
        Map nParam = new HashMap();
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
