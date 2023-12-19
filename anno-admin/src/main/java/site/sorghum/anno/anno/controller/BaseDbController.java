package site.sorghum.anno.anno.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.IPage;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.*;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.entity.req.AnnoTreeListRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreesRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoPageRequestAnno;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.*;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;

import java.sql.SQLException;
import java.util.*;
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
    @Inject
    PermissionContext permissionContext;
    @Inject
    PermissionProxy permissionProxy;

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> querySqlTree(String sql){
        String actualSql = QuerySqlCache.get(sql);
        if (StrUtil.isEmpty(actualSql)) {
            return AnnoResult.failure("sql 不存在,请检查相关配置项");
        }
        List<Map<String, Object>> mapList = dbService.sql2MapList(actualSql);
        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            mapList, "label", "id", "pid"
        );
        return AnnoResult.succeed(trees);
    }


    /**
     * 分页查询
     *
     * @return {@link AnnoResult}<{@link IPage}<{@link T}>>
     */
    public <T> AnnoResult<IPage<T>> page(String clazz,
                                         AnnoPageRequestAnno pageRequest,
                                         Map<String, Object> param) {
        List<String> nullKeys = pageRequest.getNullKeys();
        List<AnOrder> anOrderList = pageRequest.getAnOrderList();
        AnEntity entity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(entity, PermissionProxy.VIEW);
        param = emptyStringIgnore(param);
        AnnoMtm annoMtm = pageRequest.getAnnoMtm();
        String m2mSql = Utils.m2mSql(annoMtm, pageRequest.getJoinValue());
        String andSql = null;
        String inPrefix = " in (";
        if (pageRequest.isReverseM2m()) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !pageRequest.isIgnoreM2m()) {
            String joinThisClazzFieldSql = annoMtm.getM2mJoinThisClazzFieldSql();
            andSql = joinThisClazzFieldSql + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, entity.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        for (AnOrder anOrder : anOrderList) {
            dbConditions.add(new DbCondition(DbCondition.QueryType.ORDER_BY,null,entity.getField(anOrder.getOrderValue()).getTableFieldName(),anOrder.getOrderType()));
        }
        for (String nullKey : nullKeys) {
            dbConditions.add(
                DbCondition.builder().
                    field(AnnoFieldCache.getSqlColumnByJavaName(entity.getClazz(),nullKey)).
                    andOr(DbCondition.AndOr.AND).build());
        }
        IPage<T> pageRes = (IPage<T>) dbService.page(entity.getClazz(), dbConditions, new PageParam(pageRequest.getPage(), pageRequest.getPageSize()));
        return AnnoResult.succeed(pageRes);
    }

    public <T> AnnoResult<T> save(String clazz, Map<String, Object> param) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.ADD);

        TableParam<T> tableParam = (TableParam<T>) AnnoTableParamCache.get(clazz);

        T t = JSONUtil.toBean(emptyStringIgnore(param), tableParam.getClazz());
        dbService.insert(t);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<T> queryById(String clazz, String pkValue, String _cat) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);

        String id = pkValue;
        if (id == null) {
            id = _cat;
        }
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }
        T queryOne = (T) dbService.queryOne(anEntity.getClazz(), CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(id).build()));
        return AnnoResult.succeed(queryOne);
    }

    /**
     * 通过id删除
     * <p>
     * id id
     *
     * @return {@link AnnoResult}
     */
    public AnnoResult<String> removeById(String clazz, String id) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.DELETE);

        AnField pkField = anEntity.getPkField();
        dbService.delete(anEntity.getClazz(), CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(id).build()));
        return AnnoResult.succeed("删除成功");
    }

    /**
     * 通过ID 更新
     */
    public <T> AnnoResult<T> updateById(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);

        AnField pkField = anEntity.getPkField();
        T bean = (T) JSONUtil.toBean(emptyStringIgnore(param), anEntity.getClazz());
        dbService.update(CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(param.get(pkField.getFieldName())).build()), bean);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<T> saveOrUpdate(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        T data = (T) JSONUtil.toBean(emptyStringIgnore(param), anEntity.getClazz());
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }
        if (ReflectUtil.getFieldValue(data, pkField.getFieldName()) == null) {
            permissionProxy.checkPermission(anEntity, PermissionProxy.ADD);
            dbService.insert(data);
        } else {
            permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);
            dbService.update(CollUtil.newArrayList(DbCondition.builder().field(pkField.getTableFieldName()).value(param.get(pkField.getFieldName())).build()), data);
        }
        return AnnoResult.succeed(data);
    }

    public <T> AnnoResult<T> removeRelation(String clazz, Map<String, Object> param) throws SQLException {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.DELETE);
        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoMtm annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        AnEntity mediumEntity = metadataManager.getEntity(annoMtm.getM2mMediumTableClass());
        String mediumOtherFieldSql = annoMtm.getM2mMediumTargetFieldSql();
        List<String> targetValue = MapUtil.get(param,"targetJoinValue",List.class);
        String thisValue = MapUtil.getStr(param,"thisJoinValue");
        String mediumThisField = annoMtm.getM2mMediumThisFieldSql();
        ArrayList<DbCondition> dbConditions = CollUtil.newArrayList(
            DbCondition.builder().field(mediumOtherFieldSql).value(targetValue).type(DbCondition.QueryType.IN).build(),
            DbCondition.builder().field(mediumThisField).value(thisValue).build()
        );
        dbService.delete(mediumEntity.getClazz(), dbConditions);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(String clazz,
                                                               boolean ignoreM2m,
                                                               boolean reverseM2m,
                                                               Map<String, String> param) {
        List<T> list = queryTreeList(clazz, ignoreM2m, reverseM2m, param);
        List<AnnoTreeDTO<String>> annoTreeDTOs = null;
        if (param.containsKey("idKey") && param.containsKey("labelKey")) {
            annoTreeDTOs = Utils.toTrees(list, param.get("idKey"), param.get("labelKey"));
        }else {
            annoTreeDTOs = Utils.toTrees(list);
        }
        annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder().id("").label("无选择").title("无选择").value("").key("").build());
        return AnnoResult.succeed(annoTreeDTOs);
    }

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(String clazz,
                                                               AnnoTreesRequestAnno annoTreesRequest,
                                                               AnnoTreeListRequestAnno treeListRequestAnno,
                                                               Map<String, String> param) {
        List<T> list = queryTreeList(clazz, treeListRequestAnno, param);
        List<AnnoTreeDTO<String>> annoTreeDTOList = null;
        if (annoTreesRequest.hasFrontSetKey()) {
            annoTreeDTOList = Utils.toTrees(list, annoTreesRequest.getIdKey(), annoTreesRequest.getLabelKey());
        }else {
            annoTreeDTOList = Utils.toTrees(list);
        }
        annoTreeDTOList.add(0, AnnoTreeDTO.<String>builder().id("").label("无选择").title("无选择").value("").key("").build());
        return AnnoResult.succeed(annoTreeDTOList);
    }

    private <T> List<T> queryTreeList(String clazz, boolean ignoreM2m, boolean reverseM2m, Map<String, String> param) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.VIEW);

        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(clazz);
        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoMtm annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        String m2mSql = Utils.m2mSql(annoMtm, param.get("joinValue"));
        String andSql = null;
        String inPrefix = " in (";
        if (reverseM2m) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !ignoreM2m) {
            String joinThisClazzField = annoMtm.getM2mJoinThisClazzFieldSql();
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, tableParam.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        return dbService.list(tableParam.getClazz(), dbConditions);
    }

    private <T> List<T> queryTreeList(String clazz, AnnoTreeListRequestAnno annoTreeListRequestAnno, Map<String, String> param) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.VIEW);
        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(clazz);
        AnnoMtm annoMtm = annoTreeListRequestAnno.getAnnoMtm();
        String m2mSql = Utils.m2mSql(annoMtm, annoTreeListRequestAnno.getJoinValue());
        String andSql = null;
        String inPrefix = " in (";
        if (annoTreeListRequestAnno.isReverseM2m()) {
            inPrefix = " not in (";
        }
        if (StrUtil.isNotEmpty(m2mSql) && !annoTreeListRequestAnno.isIgnoreM2m()) {
            String joinThisClazzField = annoMtm.getM2mJoinThisClazzFieldSql();
            andSql = joinThisClazzField + inPrefix + m2mSql + ")";
        }
        List<DbCondition> dbConditions = AnnoUtil.simpleEntity2conditions(param, tableParam.getClazz());
        if (andSql != null) {
            dbConditions.add(DbCondition.builder().type(DbCondition.QueryType.CUSTOM).field(andSql).build());
        }
        return dbService.list(tableParam.getClazz(), dbConditions);
    }


    public <T> AnnoResult<List<Object>> annoTreeSelectData(String clazz,
                                                        boolean ignoreM2m,
                                                        boolean reverseM2m,
                                                        Map<String, String> param) {
        List<Object> list = queryTreeList(clazz, ignoreM2m, reverseM2m, param);
        if (list == null || list.isEmpty()) {
            return AnnoResult.succeed(Collections.emptyList());
        }
        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoMtm annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        List<Object> data = list.stream().map(item -> ReflectUtil.getFieldValue(item, annoMtm.getM2mJoinTargetClazzField())).collect(Collectors.toList());
        return AnnoResult.succeed(data);
    }

    public <T> AnnoResult<List<Object>> annoTreeSelectData(String clazz,
                                                           AnnoTreesRequestAnno annoTreesRequest,
                                                           AnnoTreeListRequestAnno treeListRequestAnno,
                                                           Map<String, String> param) {
        List<Object> list = queryTreeList(clazz, treeListRequestAnno, param);
        if (list == null || list.isEmpty()) {
            return AnnoResult.succeed(Collections.emptyList());
        }
        AnnoMtm annoMtm = annoTreesRequest.getAnnoMtm();
        List<Object> data = list.stream().map(item -> ReflectUtil.getFieldValue(item, annoMtm.getM2mJoinTargetClazzField())).collect(Collectors.toList());
        return AnnoResult.succeed(data);
    }

    public <T> AnnoResult<String> addM2m(String clazz, Map param, boolean clearAll) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.ADD);
        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoMtm annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        if(Objects.isNull(annoMtm)){
            throw new BizException("未找到对应的多对多数据!");
        }
        // 中间表
        Class<?> mediumTableClazz = metadataManager.getEntity(annoMtm.getM2mMediumTableClass()).getClazz();
        String[] split;
        Object ids = param.get("targetJoinValue");
        // 字段一
        String mediumThisFieldSql = annoMtm.getM2mMediumThisFieldSql();
        // 字段二
        String mediumTargetFieldSql = annoMtm.getM2mMediumTargetFieldSql();

        String mediumThisValue = param.get("thisJoinValue").toString();

        if (ids instanceof List) {
            List<String> idList = (List) ids;
            split = idList.toArray(new String[0]);
        } else {
            String mediumThisValues = ids.toString();
            split = mediumThisValues.split(",");
        }
        if (clearAll) {
            // 物理删除
            dbService.delete(mediumTableClazz, CollUtil.newArrayList(
                DbCondition.builder().field(mediumThisFieldSql).value(mediumThisValue).build()
            ));
        }
        for (String mediumTargetValue : split) {
            Map<String, Object> addValue = new HashMap<>() {{
                put(annoMtm.getM2mMediumThisField(), mediumThisValue);
                put(annoMtm.getM2mMediumTargetField(), mediumTargetValue);
            }};
            dbService.insert(JSONUtil.toBean(addValue, mediumTableClazz));
        }
        return AnnoResult.succeed();
    }

    public AnnoResult<String> runJavaCmd(String clazz, Map<String, String> map) throws ClassNotFoundException {
        CheckPermissionFunction.loginCheckFunction.run();
        AnEntity entity = metadataManager.getEntity(clazz);
        String annoJavaCmdId = MapUtil.getStr(map, "annoJavaCmdId");
        AnnoJavaCmd annoJavaCmd = AnnoJavaCmd.annoJavCmdMap.get(annoJavaCmdId);
        if (annoJavaCmd == null){
            return AnnoResult.failure("未找到对应的JavaCmd数据!");
        }
        if (StrUtil.isNotBlank(annoJavaCmd.getPermissionCode())){
            permissionProxy.checkPermission(entity, annoJavaCmd.getPermissionCode());
        }

        Object bean = AnnoBeanUtils.getBean(annoJavaCmd.getJavaCmdBeanClass());
        ReflectUtil.invoke(bean, annoJavaCmd.getJavaCmdMethodName(), map);
        return AnnoResult.succeed("执行成功");
    }

    private Map<String, Object> emptyStringIgnore(Map<String, ?> param) {
        Map<String, Object> nParam = new HashMap<>();
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

    private Map<String, Object> emptyStringIgnore(Map<String, ?> param,AnEntity anEntity) {
        Map<String, Object> nParam = new HashMap<>();
        for (AnField dbAnField : anEntity.getDbAnFields()) {
            String key = dbAnField.getFieldName();
            if (param.containsKey(key)){
                Object item = param.get(key);
                if (item instanceof String sItem) {
                    if (StrUtil.isNotBlank(sItem) || dbAnField.isEditCanClear()) {
                        nParam.put(key, sItem);
                    }
                } else {
                    nParam.put(key, param.get(key));
                }
            }
        }
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
