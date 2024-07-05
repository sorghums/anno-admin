package site.sorghum.anno.anno.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.*;
import site.sorghum.anno.anno.chart.AnChartService;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.entity.req.AnnoPageRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreeListRequestAnno;
import site.sorghum.anno.anno.entity.req.AnnoTreesRequestAnno;
import site.sorghum.anno.anno.entity.response.AnChartResponse;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.anno.proxy.AnnoBaseService;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbTableContext;
import site.sorghum.anno.db.QueryType;
import site.sorghum.anno.db.TableParam;

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
    AnnoBaseService baseService;

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    DbTableContext dbTableContext;

    @Inject
    AnChartService anChartService;

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> querySqlTree(String sql) {
        String actualSql = QuerySqlCache.get(sql);
        if (StrUtil.isEmpty(actualSql)) {
            return AnnoResult.failure("sql 不存在,请检查相关配置项");
        }
        List<Map<String, Object>> mapList = baseService.sql2MapList(actualSql);
        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            mapList, "label", "id", "pid"
        );
        return AnnoResult.succeed(trees);
    }


    /**
     * 分页查询
     */
    public <T> AnnoResult<AnnoPage<T>> page(String clazz,
                                            AnnoPageRequestAnno pageRequest,
                                            Map<String, Object> param) {
        List<String> nullKeys = pageRequest.getNullKeys();
        List<AnOrder> anOrderList = pageRequest.getAnOrderList();
        AnEntity entity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(entity, PermissionProxy.VIEW);
        param = AnnoUtil.emptyStringIgnore(param);
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
        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(entity, param);
        if (andSql != null) {
            criteria.condition().create(andSql, QueryType.CUSTOM);
        }
        for (AnOrder anOrder : anOrderList) {
            criteria.order().orderBy(anOrder.getOrderType(), entity.getField(anOrder.getOrderValue()).getTableFieldName());
        }
        for (String nullKey : nullKeys) {
            criteria.condition().create(entity.getField(nullKey).getTableFieldName(), QueryType.IS_NULL);
        }
        criteria.page(pageRequest.getPage(), pageRequest.getPageSize());
        AnnoPage<T> pageRes = baseService.page(criteria);
        return AnnoResult.succeed(pageRes);
    }

    public <T> AnnoResult<T> save(String clazz, Map<String, Object> param) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.ADD);

        TableParam<T> tableParam = dbTableContext.getTableParam(clazz);

        T t = JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), tableParam.getClazz());
        baseService.insert(t);
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
        T queryOne = baseService.queryOne(DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), id));
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
        baseService.delete(DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), id));
        return AnnoResult.succeed("删除成功");
    }

    /**
     * 通过ID 更新
     */
    public <T> AnnoResult<T> updateById(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);

        AnField pkField = anEntity.getPkField();
        T bean = (T) JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), anEntity.getClazz());
        baseService.update(bean, DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), param.get(pkField.getFieldName())));
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<T> saveOrUpdate(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        T data = (T) JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), anEntity.getClazz());
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }
        if (ReflectUtil.getFieldValue(data, pkField.getFieldName()) == null) {
            permissionProxy.checkPermission(anEntity, PermissionProxy.ADD);
            baseService.insert(data);
        } else {
            permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);
            baseService.update(data, DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), param.get(pkField.getFieldName())));
        }
        return AnnoResult.succeed(data);
    }

    public <T> AnnoResult<T> removeRelation(String clazz, Map<String, Object> param) throws SQLException {
        AnEntity entity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(entity, PermissionProxy.DELETE);
        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoMtm annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        AnEntity mediumEntity = metadataManager.getEntity(annoMtm.getM2mMediumTableClazz());
        String mediumOtherFieldSql = annoMtm.getM2mMediumTargetFieldSql();
        List<String> targetValue = MapUtil.get(param, "targetJoinValue", List.class);
        String thisValue = MapUtil.getStr(param, "thisJoinValue");
        String mediumThisField = annoMtm.getM2mMediumThisFieldSql();
        DbCriteria criteria = DbCriteria.from(mediumEntity)
            .in(mediumOtherFieldSql, targetValue.toArray())
            .eq(mediumThisField, thisValue);
        baseService.delete(criteria);
        return AnnoResult.succeed();
    }

    public <T> AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(String clazz,
                                                               AnnoTreesRequestAnno annoTreesRequest,
                                                               AnnoTreeListRequestAnno treeListRequestAnno,
                                                               Map<String, Object> param) {
        List<T> list = queryTreeList(clazz, treeListRequestAnno, param);
        List<AnnoTreeDTO<String>> annoTreeDTOList = null;
        if (annoTreesRequest.hasFrontSetKey()) {
            annoTreeDTOList = Utils.toTrees(list, annoTreesRequest.getIdKey(), annoTreesRequest.getLabelKey());
        } else {
            annoTreeDTOList = Utils.toTrees(list);
        }
        annoTreeDTOList.add(0, AnnoTreeDTO.<String>builder().id("").label("无选择").title("无选择").value("").key("").build());
        return AnnoResult.succeed(annoTreeDTOList);
    }

    private <T> List<T> queryTreeList(String clazz, AnnoTreeListRequestAnno annoTreeListRequestAnno, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);
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
        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(anEntity, param);
        if (StrUtil.isNotBlank(andSql)) {
            criteria.addCondition(andSql, QueryType.CUSTOM);
        }
        return baseService.list(criteria);
    }


    public <T> AnnoResult<List<Object>> annoTreeSelectData(String clazz,
                                                           AnnoTreesRequestAnno annoTreesRequest,
                                                           AnnoTreeListRequestAnno treeListRequestAnno,
                                                           Map<String, Object> param) {
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
        if (Objects.isNull(annoMtm)) {
            throw new BizException("未找到对应的多对多数据!");
        }
        // 中间表
        AnEntity entity = metadataManager.getEntity(annoMtm.getM2mMediumTableClazz());
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
            baseService.delete(DbCriteria.from(entity).eq(mediumThisFieldSql, mediumThisValue));
        }
        for (String mediumTargetValue : split) {
            Map<String, Object> addValue = new HashMap<>() {{
                put(annoMtm.getM2mMediumThisField(), mediumThisValue);
                put(annoMtm.getM2mMediumTargetField(), mediumTargetValue);
            }};
            baseService.insert(JSONUtil.toBean(addValue, entity.getClazz()));
        }
        return AnnoResult.succeed();
    }

    public AnnoResult<String> runJavaCmd(String clazz, CommonParam map) throws ClassNotFoundException {
        permissionProxy.checkLogin();
        AnEntity entity = metadataManager.getEntity(clazz);
        String annoJavaCmdId = MapUtil.getStr(map, "annoJavaCmdId");
        AnnoJavaCmd annoJavaCmd = AnnoJavaCmd.annoJavCmdMap.get(annoJavaCmdId);
        if (annoJavaCmd == null) {
            return AnnoResult.failure("未找到对应的JavaCmd数据!");
        }
        if (StrUtil.isNotBlank(annoJavaCmd.getPermissionCode())) {
            permissionProxy.checkPermission(entity, annoJavaCmd.getPermissionCode());
        }
        if (!Objects.equals(annoJavaCmd.getRunSupplier(), JavaCmdSupplier.class)){
            JavaCmdSupplier cmdSupplier = AnnoBeanUtils.getBean(annoJavaCmd.getRunSupplier());
            return AnnoResult.succeed(cmdSupplier.run(map));
        }
        return AnnoResult.succeed("未找到具体执行器");
    }


    private List<AnChartResponse<Object>> chartData(String clazz, String fieldId, CommonParam params) {
        permissionProxy.checkLogin();
        AnEntity entity = metadataManager.getEntity(clazz);
        if (StrUtil.isNotBlank(entity.getPermissionCode())) {
            permissionProxy.checkPermission(entity, null);
        }
        if (!entity.getAnChart().getEnable()) {
            throw new BizException("实体类非图表类型或未加载!");
        }
        return anChartService.getChart(clazz, fieldId, params);
    }

    public AnnoResult<List<AnChartResponse<Object>>> getChart(String clazz, String fieldId, CommonParam params) {
        List<AnChartResponse<Object>> chart =chartData(clazz, fieldId, params);
        return AnnoResult.succeed(chart);
    }

    public AnnoResult<AnChartResponse<Object>> getOneChart(String clazz, String fieldId, CommonParam params) {
        List<AnChartResponse<Object>> chart = chartData(clazz, fieldId, params);
        if (CollUtil.isEmpty(chart)) {
            return AnnoResult.succeed();
        }
        return AnnoResult.succeed(chart.get(0));
    }
}
