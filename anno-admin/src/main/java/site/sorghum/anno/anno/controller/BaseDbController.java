package site.sorghum.anno.anno.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.util.AnnoTableParamCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.OrderByParam;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;

import java.sql.SQLException;
import java.util.ArrayList;
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
@Slf4j
public class BaseDbController {

    @Inject
    @Named("dbServiceWithProxy")
    DbService dbService;

    @Inject
    MetadataManager metadataManager;

    /**
     * 分页查询
     *
     * @return {@link AnnoResult}<{@link IPage}<{@link T}>>
     */
    public <T> AnnoResult<IPage<T>> page(String clazz,
                                         int page,
                                         int perPage,
                                         String orderBy,
                                         String orderDir,
                                         boolean ignoreM2m,
                                         boolean reverseM2m,
                                         Map<String, Object> param) {
        AnEntity entity = metadataManager.getEntity(clazz);
        param = emptyStringIgnore(param);
        String m2mSql = Utils.m2mSql(param);
        String andSql = null;
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = MapUtil.getStr(param, "joinThisClazzField");
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, entity.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        TableParam tableParam = AnnoTableParamCache.get(clazz);
        if (StrUtil.isNotEmpty(orderBy)) {
            tableParam.getOrderByParam().addOrderByItem(new OrderByParam.OrderByItem(entity.getField(orderBy).getTableFieldName(), "asc".equals(orderDir)));
        }
        IPage<T> pageRes = dbService.page(tableParam, dbConditions, new PageParam(page, perPage));
        return AnnoResult.succeed(pageRes);
    }

    public <T> AnnoResult<T> save(String clazz, Map<String, Object> param) {
        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);
        T t = JSONUtil.toBean(emptyStringIgnore(param), tableParam.getClazz());
        dbService.insert(tableParam, t);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<T> queryById(String clazz, String pkValue, String _cat) {
        String id = pkValue;
        if (id == null) {
            id = _cat;
        }
        AnEntity anEntity = metadataManager.getEntity(clazz);
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }
        T queryOne = (T) dbService.queryOne(metadataManager.getTableParam(clazz), CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(id).build()));
        return AnnoResult.succeed(queryOne);
    }

    /**
     * 通过id删除
     * <p>
     * id id
     *
     * @return {@link Result}
     */
    public AnnoResult<String> removeById(String clazz, String id) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        AnField pkField = anEntity.getPkField();
        dbService.delete(metadataManager.getTableParam(clazz), CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(id).build()));
        return AnnoResult.succeed();
    }

    /**
     * 通过ID 更新
     */
    public <T> AnnoResult<T> updateById(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        AnField pkField = anEntity.getPkField();
        TableParam<T> tableParam = metadataManager.getTableParam(clazz);
        T bean = (T) JSONUtil.toBean(emptyStringIgnore(param), anEntity.getClazz());
        dbService.update(tableParam,
            CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(param.get(pkField.getFieldName())).build()),
            bean);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<T> saveOrUpdate(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        T data = (T) JSONUtil.toBean(emptyStringIgnore(param), anEntity.getClazz());
        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(clazz);
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }
        if (ReflectUtil.getFieldValue(data, pkField.getFieldName()) == null) {
            dbService.insert(tableParam, data);
        } else {
            dbService.update(tableParam,
                CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(param.get(pkField.getFieldName())).build()),
                data);
        }
        return AnnoResult.succeed(data);
    }

    public <T> AnnoResult<T> removeRelation(String clazz, Map<String, String> param) throws SQLException {
        String mediumOtherField = param.get("mediumOtherField");
        String otherValue = param.get("joinValue");
        String thisValue = param.get(param.get("joinThisClazzField"));
        String mediumThisField = param.get("mediumThisField");
        TableParam<?> tableParam = metadataManager.getTableParam(param.get("mediumTableClass"));
        ArrayList<DbCondition> dbConditions = CollUtil.newArrayList(
            DbCondition.builder().field(mediumOtherField).value(otherValue).build(),
            DbCondition.builder().field(mediumThisField).value(thisValue).build()
        );
        dbService.delete(tableParam, dbConditions);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(String clazz,
                                                               boolean ignoreM2m,
                                                               boolean reverseM2m,
                                                               Map<String, String> param) {
        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(clazz);
        String m2mSql = Utils.m2mSql(param);
        String andSql = null;
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField");
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, tableParam.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        List<T> list = dbService.list(tableParam, dbConditions);
        List<AnnoTreeDTO<String>> annoTreeDTOs = Utils.toTrees(list);
        annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder().id("0").label("无选择").value("").build());
        return AnnoResult.succeed(annoTreeDTOs);
    }

    public <T> AnnoResult<Map<?, ?>> annoTreeSelectData(String clazz,
                                                        boolean ignoreM2m,
                                                        boolean reverseM2m,
                                                        Map<String, String> param) {
        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(clazz);
        String m2mSql = Utils.m2mSql(param);
        String andSql = null;
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = param.get("joinThisClazzField");
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, tableParam.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        List<T> list = dbService.list(tableParam, dbConditions);
        if (list == null || list.isEmpty()) {
            return AnnoResult.succeed(Collections.emptyMap());
        }
        List<Object> data = list.stream().map(item -> ReflectUtil.getFieldValue(item, AnnoUtil.getPkField(tableParam.getClazz()))).collect(Collectors.toList());
        return AnnoResult.succeed(MapUtil.of("m2mTree", data));
    }

    public <T> AnnoResult<String> addM2m(String clazz, Map param, boolean clearAll) {
        // 中间表
        String mediumTableClass = param.get("mediumTableClass").toString();
        TableParam<Object> tableParam = (TableParam<Object>) metadataManager.getTableParam(mediumTableClass);
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
            dbService.insert(tableParam, JSONUtil.toBean(addValue, tableParam.getClazz()));
        }
        return AnnoResult.succeed();
    }

    public AnnoResult<String> runJavaCmd(Map<String, String> map) throws ClassNotFoundException {
        map.put("clazz", CryptoUtil.decrypt(map.get("clazz")));
        map.put("method", CryptoUtil.decrypt(map.get("method")));
        map.put("expireTime", CryptoUtil.decrypt(map.get("expireTime")));
        // 判断是否过期
        if (Long.parseLong(map.get("expireTime")) < System.currentTimeMillis()) {
            return AnnoResult.failure("页面已过期，请刷新页面重试。");
        }
        Object bean = AnnoBeanUtils.getBean(
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