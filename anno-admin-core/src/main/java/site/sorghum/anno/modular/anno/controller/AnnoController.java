package site.sorghum.anno.modular.anno.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.Result;
import org.noear.wood.IPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.OrderByParam;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoFieldCache;
import site.sorghum.anno.modular.anno.util.AnnoTableParamCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.common.response.AnnoResult;
import site.sorghum.anno.common.util.CryptoUtil;
import site.sorghum.anno.common.util.JSONUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Anno控制器
 *
 * @author sorghum
 * @since 2023/05/20
 */
@SuppressWarnings("unchecked")
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
                                         @Body Map<String, Object> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        param = emptyStringIgnore(param);
        String m2mSql = annoService.m2mSql(param);
        String andSql = null;
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField").toString();
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, aClass);
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);
        if (StrUtil.isNotEmpty(orderBy)) {
            tableParam.getOrderByParam().addOrderByItem(new OrderByParam.OrderByItem(AnnoFieldCache.getSqlColumnByJavaName(aClass, orderBy), "asc".equals(orderDir)));
        }
        IPage<T> pageRes = dbService.page(tableParam, dbConditions, new PageParam(page, perPage));
        return AnnoResult.succeed(pageRes);
    }

    @Mapping("/{clazz}/save")
    @Post
    public <T> AnnoResult<T> save(@Path String clazz, @Body Map<String, Object> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);
        T t = JSONUtil.parseObject(emptyStringIgnore(param), aClass);
        dbService.insert(tableParam, t);
        return AnnoResult.from(Result.succeed());
    }

    @Mapping("/{clazz}/queryById")
    @Post
    public <T> AnnoResult<T> queryById(@Path String clazz, @Param String pkValue, @Param String _cat) {
        String id = pkValue;
        if (id == null) {
            id = _cat;
        }
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        Field pkField = AnnoUtil.getPkFieldItem(aClass);
        if (pkField == null) {
            return AnnoResult.from(Result.failure("未找到主键"));
        }
        T queryOne = (T) dbService.queryOne(AnnoTableParamCache.get(clazz), CollUtil.newArrayList(DbCondition.builder().field(AnnoUtil.getColumnName(pkField)).value(id).build()));
        return AnnoResult.succeed(queryOne);
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
        Field pkField = AnnoUtil.getPkFieldItem(aClass);
        dbService.delete(AnnoTableParamCache.get(clazz), Collections.singletonList(DbCondition.builder().field(AnnoUtil.getColumnName(pkField)).value(id).build()));
        return AnnoResult.succeed();
    }

    /**
     * 通过ID 更新
     */
    @Mapping("/{clazz}/updateById")
    public <T> AnnoResult<T> updateById(@Path String clazz, @Body Map<String, Object> param) {
        Class<T> aClass = (Class<T>) AnnoClazzCache.get(clazz);
        Field pkField = AnnoUtil.getPkFieldItem(aClass);
        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);
        dbService.update(tableParam,
                CollUtil.newArrayList(DbCondition.builder().field(AnnoUtil.getColumnName(pkField)).value(param.get(pkField.getName())).build()),
                JSONUtil.parseObject(emptyStringIgnore(param), aClass));
        return AnnoResult.succeed();
    }

    @Mapping("/{clazz}/saveOrUpdate")
    public <T> AnnoResult<T> saveOrUpdate(@Path String clazz, @Body Map<String, Object> param) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        T data = (T) JSONUtil.parseObject(emptyStringIgnore(param), aClass);
        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);
        if (ReflectUtil.getFieldValue(data, AnnoUtil.getPkField(aClass)) == null) {
            dbService.insert(tableParam, data);
        } else {
            Field pkField = AnnoUtil.getPkFieldItem(aClass);
            if (pkField == null) {
                return AnnoResult.from(Result.failure("未找到主键"));
            }
            dbService.update(tableParam,
                    CollUtil.newArrayList(DbCondition.builder().field(AnnoFieldCache.getSqlColumnByFiled(aClass,pkField)).value(param.get(pkField.getName())).build()),
                    data);
        }
        return AnnoResult.from(Result.succeed(data));
    }

    @Mapping("/{clazz}/remove-relation")
    public <T> AnnoResult<T> removeRelation(@Path String clazz, @Body Map<String, String> param) throws SQLException {
        String mediumOtherField = param.get("mediumOtherField");
        String otherValue = param.get("joinValue");
        String thisValue = param.get(param.get("joinThisClazzField"));
        String mediumThisField = param.get("mediumThisField");
        TableParam<?> tableParam = AnnoTableParamCache.get(param.get("mediumTableClass"));
        ArrayList<DbCondition> dbConditions = CollUtil.newArrayList(
                DbCondition.builder().field(mediumOtherField).value(otherValue).build(),
                DbCondition.builder().field(mediumThisField).value(thisValue).build()
        );
        dbService.delete(tableParam, dbConditions);
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

    @Mapping("/{clazz}/annoTreeSelectData")
    public <T> AnnoResult<Map<?, ?>> annoTreeSelectData(@Path String clazz,
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
        List<Object> data = list.stream().map(item -> ReflectUtil.getFieldValue(item, AnnoUtil.getPkField(aClass))).collect(Collectors.toList());
        return AnnoResult.from(Result.succeed(MapUtil.of("m2mTree", data)));
    }

    @Mapping("/{clazz}/addM2m")
    public <T> AnnoResult<String> addM2m(@Path String clazz, @Body Map param, @Param boolean clearAll) {
        // 主表
        Class<?> aClass = AnnoClazzCache.get(clazz);
        // 中间表
        String mediumTableClass = param.get("mediumTableClass").toString();
        Class<Object> mediumClass = (Class<Object>) AnnoClazzCache.get(mediumTableClass);
        TableParam<Object> tableParam = (TableParam<Object>) AnnoTableParamCache.get(mediumTableClass);
        String[] split;
        Object ids = param.get("ids");
        // 字段一
        String mediumThisField = param.get("mediumThisField").toString();
        // 字段二
        String mediumOtherField = param.get("mediumOtherField").toString();
        String mediumOtherValue = param.get("joinValue").toString();
        if (ids instanceof List) {
            List<String> idList = (List) ids;
            split = idList.toArray(new String[0]);
        } else {
            String mediumThisValues = ids.toString();
            split = mediumThisValues.split(",");
        }
        if (clearAll) {
            // 物理删除
            tableParam.getRemoveParam().setLogic(false);
            dbService.delete(tableParam, CollUtil.newArrayList(
                    DbCondition.builder().field(mediumOtherField).value(mediumOtherValue).build()
            ));
            tableParam.getRemoveParam().setLogic(true);
        }
        for (String mediumThisValue : split) {
            Map<String, Object> addValue = new HashMap<String, Object>() {{
                put(mediumThisField, mediumThisValue);
                put(mediumOtherField, mediumOtherValue);
            }};

            dbService.insert(tableParam,JSONUtil.parseObject(addValue, mediumClass));
        }
        return AnnoResult.succeed();
    }

    @Mapping(value = "runJavaCmd", method = MethodType.POST, consumes = "application/json")
    public AnnoResult<String> runJavaCmd(@Body Map<String, String> map) throws ClassNotFoundException {
        map.put("clazz", CryptoUtil.decrypt(map.get("clazz")));
        map.put("method", CryptoUtil.decrypt(map.get("method")));
        Object bean = Solon.context().getBean(
                Class.forName(map.get("clazz"))
        );
        ReflectUtil.invoke(bean, map.get("method"), map);
        return AnnoResult.succeed("执行成功");
    }

    private Map<String, Object> emptyStringIgnore(Map<String, ?> param) {
        Map<String, Object> nParam = new HashMap<String, Object>();
        for (String key : param.keySet()) {
            Object item = param.get(key);
            if (item instanceof String sItem) {
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
