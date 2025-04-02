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
import site.sorghum.anno.anno.annotation.clazz.AnnoPermissionImpl;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
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
import site.sorghum.anno.db.service.context.AnnoDbContext;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础数据库操作控制器
 * 提供通用的CRUD、分页查询、树形结构查询、多对多关系操作等功能
 */
@Slf4j
public class BaseDbController {

    @Inject
    private AnnoBaseService baseService;          // 基础服务
    @Inject
    private MetadataManager metadataManager;      // 元数据管理
    @Inject
    private PermissionProxy permissionProxy;      // 权限代理
    @Inject
    private DbTableContext dbTableContext;        // 数据库表上下文
    @Inject
    private AnChartService anChartService;        // 图表服务

    /**
     * 执行SQL查询并返回树形结构数据
     *
     * @param sql SQL语句(格式: dbName:entityName)
     * @return 树形结构数据结果
     */
    public AnnoResult<List<AnnoTreeDTO<String>>> querySqlTree(String sql) {
        String actualSql = QuerySqlCache.get(sql);
        String[] split = sql.split(":");
        String dbName = split[0];
        String entityName = split[1];

        AnEntity anEntity = metadataManager.getEntity(entityName);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);

        return AnnoDbContext.dynamicDbContext(dbName, () -> {
            if (StrUtil.isEmpty(actualSql)) {
                return AnnoResult.failure("SQL不存在，请检查配置");
            }
            List<Map<String, Object>> mapList = baseService.sql2MapList(actualSql);
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(mapList, "label", "id", "pid");
            return AnnoResult.succeed(trees);
        });
    }

    /**
     * 分页查询
     *
     * @param clazz 实体类名
     * @param pageRequest 分页请求参数
     * @param param 查询条件
     * @return 分页结果
     */
    public AnnoResult<AnnoPage<Object>> page(String clazz, AnnoPageRequestAnno pageRequest, Map<String, Object> param) {
        AnEntity entity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(entity, PermissionProxy.VIEW);

        // 处理查询条件和排序
        DbCriteria criteria = buildPageCriteria(entity, pageRequest, param);

        // 执行分页查询
        AnnoPage<Object> pageRes = baseService.page(criteria);
        return AnnoResult.succeed(pageRes);
    }

    /**
     * 新增数据
     *
     * @param clazz 实体类名
     * @param param 数据参数
     * @return 操作结果
     */
    public AnnoResult<Object> save(String clazz, Map<String, Object> param) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.ADD);

        TableParam<Object> tableParam = dbTableContext.getTableParam(clazz);
        Object entity = JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), tableParam.getClazz());
        baseService.insert(entity);
        return AnnoResult.succeed();
    }

    /**
     * 根据ID查询单条数据
     *
     * @param clazz 实体类名
     * @param pkValue 主键值
     * @param _cat 备用主键值
     * @return 查询结果
     */
    public AnnoResult<Object> queryById(String clazz, String pkValue, String _cat) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);

        String id = Optional.ofNullable(pkValue).orElse(_cat);
        AnField pkField = anEntity.getPkField();
        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }

        Object result = baseService.queryOne(DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), id));
        return AnnoResult.succeed(result);
    }

    /**
     * 根据ID删除数据
     *
     * @param clazz 实体类名
     * @param id 主键值
     * @return 操作结果
     */
    public AnnoResult<String> removeById(String clazz, String id) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.DELETE);

        AnField pkField = anEntity.getPkField();
        baseService.delete(DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), id));
        return AnnoResult.succeed("删除成功");
    }

    /**
     * 根据ID更新数据
     *
     * @param clazz 实体类名
     * @param param 更新数据
     * @return 操作结果
     */
    public AnnoResult<Object> updateById(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);

        AnField pkField = anEntity.getPkField();
        Object entity = JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), anEntity.getThisClass());
        baseService.update(entity, DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), param.get(pkField.getJavaName())));
        return AnnoResult.succeed();
    }

    /**
     * 保存或更新数据(根据主键是否存在判断)
     *
     * @param clazz 实体类名
     * @param param 数据参数
     * @return 操作结果
     */
    public AnnoResult<Object> saveOrUpdate(String clazz, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Object entity = JSONUtil.toBean(AnnoUtil.emptyStringIgnore(param), anEntity.getThisClass());
        AnField pkField = anEntity.getPkField();

        if (pkField == null) {
            return AnnoResult.failure("未找到主键");
        }

        if (ReflectUtil.getFieldValue(entity, pkField.getJavaName()) == null) {
            permissionProxy.checkPermission(anEntity, PermissionProxy.ADD);
            baseService.insert(entity);
        } else {
            permissionProxy.checkPermission(anEntity, PermissionProxy.UPDATE);
            baseService.update(entity, DbCriteria.from(anEntity).eq(pkField.getTableFieldName(), param.get(pkField.getJavaName())));
        }
        return AnnoResult.succeed(entity);
    }

    /**
     * 删除多对多关系
     *
     * @param clazz 实体类名
     * @param param 参数(包含annoM2mId、targetJoinValue、thisJoinValue)
     * @return 操作结果
     */
    public AnnoResult<Object> removeRelation(String clazz, Map<String, Object> param) throws SQLException {
        AnEntity entity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(entity, PermissionProxy.DELETE);

        return AnnoDbContext.dynamicDbContext(entity.getDbName(), () -> {
            String annoM2mId = MapUtil.getStr(param, "annoM2mId");
            AnnoButtonImpl.M2MJoinButtonImpl annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
            AnEntity mediumEntity = metadataManager.getEntity(annoMtm.mediumTableClazz());

            String mediumOtherFieldSql = AnnoMtm.getM2mMediumTargetFieldSql(annoMtm);
            List<String> targetValue = MapUtil.get(param, "targetJoinValue", List.class);
            String thisValue = MapUtil.getStr(param, "thisJoinValue");
            String mediumThisField = AnnoMtm.getM2mMediumThisFieldSql(annoMtm);

            DbCriteria criteria = DbCriteria.from(mediumEntity)
                .in(mediumOtherFieldSql, targetValue.toArray())
                .eq(mediumThisField, thisValue);

            baseService.delete(criteria);
            return AnnoResult.succeed();
        });
    }

    /**
     * 获取树形数据
     *
     * @param clazz 实体类名
     * @param annoTreesRequest 树形请求参数
     * @param treeListRequestAnno 列表请求参数
     * @param param 查询条件
     * @return 树形数据结果
     */
    public AnnoResult<List<AnnoTreeDTO<String>>> annoTrees(String clazz,
                                                           AnnoTreesRequestAnno annoTreesRequest,
                                                           AnnoTreeListRequestAnno treeListRequestAnno,
                                                           Map<String, Object> param) {
        List<Object> list = queryTreeList(clazz, treeListRequestAnno, param);
        List<AnnoTreeDTO<String>> annoTreeDTOList = annoTreesRequest.hasFrontSetKey()
            ? Utils.toTrees(list, annoTreesRequest.getIdKey(), annoTreesRequest.getLabelKey())
            : Utils.toTrees(list);

        // 添加默认"无选择"选项
        annoTreeDTOList.add(0, AnnoTreeDTO.<String>builder()
            .id("").label("无选择").title("无选择").value("").key("").build());

        return AnnoResult.succeed(annoTreeDTOList);
    }

    /**
     * 获取树形选择数据
     *
     * @param clazz 实体类名
     * @param annoTreesRequest 树形请求参数
     * @param treeListRequestAnno 列表请求参数
     * @param param 查询条件
     * @return 选择数据结果
     */
    public AnnoResult<List<Object>> annoTreeSelectData(String clazz,
                                                       AnnoTreesRequestAnno annoTreesRequest,
                                                       AnnoTreeListRequestAnno treeListRequestAnno,
                                                       Map<String, Object> param) {
        List<Object> list = queryTreeList(clazz, treeListRequestAnno, param);
        if (CollUtil.isEmpty(list)) {
            return AnnoResult.succeed(Collections.emptyList());
        }

        AnnoButtonImpl.M2MJoinButtonImpl annoMtm = annoTreesRequest.getAnnoMtm();
        List<Object> data = list.stream()
            .map(item -> ReflectUtil.getFieldValue(item, annoMtm.joinTargetClazzField()))
            .collect(Collectors.toList());

        return AnnoResult.succeed(data);
    }

    /**
     * 添加多对多关系
     *
     * @param clazz 实体类名
     * @param param 参数(包含annoM2mId、targetJoinValue、thisJoinValue)
     * @param clearAll 是否清除原有关系
     * @return 操作结果
     */
    public AnnoResult<String> addM2m(String clazz, Map param, boolean clearAll) {
        permissionProxy.checkPermission(metadataManager.getEntity(clazz), PermissionProxy.ADD);

        String annoM2mId = MapUtil.getStr(param, "annoM2mId");
        AnnoButtonImpl.M2MJoinButtonImpl annoMtm = AnnoMtm.annoMtmMap.get(annoM2mId);
        if (annoMtm == null) {
            throw new BizException("未找到对应的多对多数据");
        }

        // 准备中间表数据
        AnEntity entity = metadataManager.getEntity(annoMtm.mediumTableClazz());
        String mediumThisFieldSql = AnnoMtm.getM2mMediumThisFieldSql(annoMtm);
        String mediumTargetFieldSql = AnnoMtm.getM2mMediumTargetFieldSql(annoMtm);
        String mediumThisValue = param.get("thisJoinValue").toString();

        // 处理目标ID集合
        String[] targetIds = param.get("targetJoinValue") instanceof List
            ? ((List<String>) param.get("targetJoinValue")).toArray(new String[0])
            : param.get("targetJoinValue").toString().split(",");

        // 清除原有关系(如果需要)
        if (clearAll) {
            baseService.delete(DbCriteria.from(entity).eq(mediumThisFieldSql, mediumThisValue));
        }

        // 批量添加新关系
        for (String mediumTargetValue : targetIds) {
            Map<String, Object> addValue = new HashMap<>();
            addValue.put(annoMtm.mediumThisField(), mediumThisValue);
            addValue.put(annoMtm.mediumTargetField(), mediumTargetValue);
            baseService.insert(JSONUtil.toBean(addValue, entity.getThisClass()));
        }

        return AnnoResult.succeed();
    }

    /**
     * 执行Java命令
     *
     * @param clazz 实体类名
     * @param map 命令参数
     * @return 执行结果
     */
    public AnnoResult<String> runJavaCmd(String clazz, CommonParam map) throws ClassNotFoundException {
        permissionProxy.checkLogin();

        AnEntity entity = metadataManager.getEntity(clazz);
        String annoJavaCmdId = MapUtil.getStr(map, "annoJavaCmdId");
        AnnoButtonImpl.JavaCmdImpl annoJavaCmd = AnnoJavaCmd.annoJavCmdMap.get(annoJavaCmdId);

        if (annoJavaCmd == null) {
            return AnnoResult.failure("未找到对应的JavaCmd数据");
        }

        // 检查按钮权限
        checkJavaCmdPermission(entity, annoJavaCmdId);

        // 执行命令
        if (!Objects.equals(annoJavaCmd.getRunSupplier(), JavaCmdSupplier.class)) {
            JavaCmdSupplier cmdSupplier = AnnoBeanUtils.getBean(annoJavaCmd.getRunSupplier());
            return AnnoResult.succeed(cmdSupplier.run(map));
        }

        return AnnoResult.succeed("未找到具体执行器");
    }

    /**
     * 获取图表数据
     *
     * @param clazz 实体类名
     * @param fieldId 字段ID
     * @param params 参数
     * @return 图表数据结果
     */
    public AnnoResult<List<AnChartResponse<Object>>> getChart(String clazz, String fieldId, CommonParam params) {
        List<AnChartResponse<Object>> chart = chartData(clazz, fieldId, params);
        return AnnoResult.succeed(chart);
    }

    /**
     * 获取单个图表数据
     *
     * @param clazz 实体类名
     * @param fieldId 字段ID
     * @param params 参数
     * @return 图表数据结果
     */
    public AnnoResult<AnChartResponse<Object>> getOneChart(String clazz, String fieldId, CommonParam params) {
        List<AnChartResponse<Object>> chart = chartData(clazz, fieldId, params);
        return AnnoResult.succeed(CollUtil.isEmpty(chart) ? null : chart.get(0));
    }

    // ============== 私有方法 ==============

    /**
     * 构建分页查询条件
     */
    private DbCriteria buildPageCriteria(AnEntity entity, AnnoPageRequestAnno pageRequest, Map<String, Object> param) {
        param = AnnoUtil.emptyStringIgnore(param);
        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(entity, param);

        // 处理多对多关联条件
        AnnoButtonImpl.M2MJoinButtonImpl annoMtm = pageRequest.getAnnoMtm();
        String m2mSql = Utils.m2mSql(annoMtm, pageRequest.getJoinValue());

        if (StrUtil.isNotEmpty(m2mSql) && !pageRequest.isIgnoreM2m()) {
            String inPrefix = pageRequest.isReverseM2m() ? " not in (" : " in (";
            String joinThisClazzFieldSql = AnnoMtm.getM2mJoinThisClazzFieldSql(entity.getThisClass(), annoMtm);
            criteria.condition().create(joinThisClazzFieldSql + inPrefix + m2mSql + ")", QueryType.CUSTOM);
        }

        // 处理排序
        for (AnOrder anOrder : pageRequest.getAnOrderList()) {
            criteria.order().orderBy(anOrder.getOrderType(),
                entity.getField(anOrder.getOrderValue()).getTableFieldName());
        }

        // 处理NULL值条件
        for (String nullKey : pageRequest.getNullKeys()) {
            criteria.condition().create(entity.getField(nullKey).getTableFieldName(), QueryType.IS_NULL);
        }

        // 设置分页
        criteria.page(pageRequest.getPage(), pageRequest.getPageSize());
        return criteria;
    }

    /**
     * 查询树形列表数据
     */
    private List<Object> queryTreeList(String clazz, AnnoTreeListRequestAnno request, Map<String, Object> param) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);

        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(anEntity, param);

        // 处理多对多关联条件
        AnnoButtonImpl.M2MJoinButtonImpl annoMtm = request.getAnnoMtm();
        String m2mSql = Utils.m2mSql(annoMtm, request.getJoinValue());

        if (StrUtil.isNotEmpty(m2mSql) && !request.isIgnoreM2m()) {
            String inPrefix = request.isReverseM2m() ? " not in (" : " in (";
            String joinThisClazzField = AnnoMtm.getM2mJoinThisClazzFieldSql(anEntity.getThisClass(), annoMtm);
            criteria.addCondition(joinThisClazzField + inPrefix + m2mSql + ")", QueryType.CUSTOM);
        }

        return baseService.list(criteria);
    }

    /**
     * 获取图表数据(内部方法)
     */
    private List<AnChartResponse<Object>> chartData(String clazz, String fieldId, CommonParam params) {
        permissionProxy.checkLogin();
        AnEntity entity = metadataManager.getEntity(clazz);

        // 检查权限
        AnnoPermissionImpl annoPermission = entity.getAnnoPermission();
        if (StrUtil.isNotBlank(annoPermission.baseCode())) {
            permissionProxy.checkPermission(entity, null);
        }

        // 检查是否支持图表
        if (!entity.getAnnoChart().enable()) {
            throw new BizException("实体类非图表类型或未加载");
        }

        return anChartService.getChart(clazz, fieldId, params);
    }

    /**
     * 检查Java命令权限
     */
    private void checkJavaCmdPermission(AnEntity entity, String annoJavaCmdId) {
        AnnoButtonImpl annoButton = AnnoJavaCmd.annoJavaCmd2ButtonMap.get(annoJavaCmdId);
        if (annoButton != null && StrUtil.isNotBlank(annoButton.getPermissionCode())) {
            permissionProxy.checkPermission(entity, annoButton.getPermissionCode());
        }

        AnnoTableButtonImpl annoTableButton = AnnoJavaCmd.annoJavaCmd2TableButtonMap.get(annoJavaCmdId);
        if (annoTableButton != null && StrUtil.isNotBlank(annoTableButton.getPermissionCode())) {
            permissionProxy.checkPermission(entity, annoTableButton.getPermissionCode());
        }
    }
}