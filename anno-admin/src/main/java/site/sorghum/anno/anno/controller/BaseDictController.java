package site.sorghum.anno.anno.controller;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeTypeImpl;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.proxy.AnnoBaseService;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.tree.TreeDataSupplier;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.QueryType;
import site.sorghum.anno.db.service.context.AnnoDbContext;
import site.sorghum.anno.trans.OnlineDictCache;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 字典数据控制器
 * 提供字典数据加载、翻译等功能，支持多种数据源：
 * <br>1. 在线字典
 * <br>2. SQL查询
 * <br>3. 实体类查询
 * <br>4. 下拉框选项提供器
 * <br>5. 树形数据提供器
 */
@Slf4j
public class BaseDictController {

    @Inject
    private AnnoBaseService baseService;          // 基础服务
    @Inject
    private MetadataManager metadataManager;      // 元数据管理
    @Inject
    private PermissionProxy permissionProxy;      // 权限代理
    @Inject
    private OnlineDictCache onlineDictCache;      // 在线字典缓存

    /**
     * 加载字典数据
     *
     * @param sqlKey SQL键值(格式: dbName:entityName)
     * @param annoClazz 实体类名
     * @param idKey ID字段名
     * @param labelKey 标签字段名
     * @param onlineDictKey 在线字典键值
     * @param optionAnnoClazz 下拉框选项提供器类名
     * @param treeAnnoClazz 树形数据提供器类名
     * @param _extra 额外参数
     * @return 字典数据结果(树形结构)
     */
    public AnnoResult<List<AnnoTreeDTO<String>>> loadDict(
        String sqlKey,
        String annoClazz,
        String idKey,
        String labelKey,
        String onlineDictKey,
        String optionAnnoClazz,
        String treeAnnoClazz,
        Map<String, Object> _extra
    ) {
        // 1. 优先检查在线字典
        if (StrUtil.isNotBlank(onlineDictKey)) {
            return loadOnlineDict(onlineDictKey);
        }

        // 2. 检查SQL查询
        if (StrUtil.isNotBlank(sqlKey)) {
            return loadDictFromSql(sqlKey);
        }

        // 3. 检查实体类查询
        if (StrUtil.isNotBlank(annoClazz)) {
            return loadDictFromEntity(annoClazz, idKey, labelKey);
        }

        // 4. 检查下拉框选项提供器
        if (StrUtil.isNotBlank(optionAnnoClazz)) {
            return loadDictFromOptionProvider(optionAnnoClazz);
        }

        // 5. 检查树形数据提供器
        if (StrUtil.isNotBlank(treeAnnoClazz)) {
            return loadDictFromTreeProvider(treeAnnoClazz);
        }

        // 默认返回空列表
        return AnnoResult.succeed(Collections.emptyList());
    }

    /**
     * 翻译单个值
     *
     * @param annoClazz 实体类名
     * @param idKey ID字段名
     * @param labelKey 标签字段名
     * @param idValue 要翻译的ID值
     * @return 翻译结果
     */
    public AnnoResult<String> transOne(String annoClazz, String idKey, String labelKey, String idValue) {
        AnEntity managerEntity = metadataManager.getEntity(annoClazz);

        // 构建查询条件
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(annoClazz);
        String sqlIdKey = AnnoFieldCache.getSqlColumnByJavaName(managerEntity.getThisClass(), idKey);
        criteria.addCondition(sqlIdKey, QueryType.EQ, idValue);

        // 执行查询
        List<Object> list = queryTreeList(criteria);
        if (list.isEmpty()) {
            return AnnoResult.succeed(idValue);
        }

        // 获取第一个结果的标签值
        Object fieldValue = ReflectUtil.getFieldValue(list.get(0), labelKey);
        return AnnoResult.succeed(fieldValue == null ? idValue : fieldValue.toString());
    }

    /**
     * 查询树形列表数据(私有方法)
     *
     * @param criteria 查询条件
     * @return 查询结果列表
     */
    private <T> List<T> queryTreeList(DbCriteria criteria) {
        permissionProxy.checkPermission(metadataManager.getEntity(criteria.getEntityName()), PermissionProxy.VIEW);
        return baseService.list(criteria);
    }

    // ============== 私有方法 ==============

    /**
     * 从在线字典加载数据
     */
    private AnnoResult<List<AnnoTreeDTO<String>>> loadOnlineDict(String onlineDictKey) {
        List<OnlineDictCache.OnlineDict> onlineDictList = onlineDictCache.getForLoadDict(onlineDictKey);
        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            onlineDictList, "name", "value", "parentValue"
        );
        return AnnoResult.succeed(trees);
    }

    /**
     * 从SQL查询加载字典数据
     */
    private AnnoResult<List<AnnoTreeDTO<String>>> loadDictFromSql(String sqlKey) {
        String[] split = sqlKey.split(":");
        String dbName = split[0];
        String entityName = split[1];

        AnEntity anEntity = metadataManager.getEntity(entityName);
        permissionProxy.checkPermission(anEntity, PermissionProxy.VIEW);

        String actualSql = QuerySqlCache.get(sqlKey);
        if (StrUtil.isEmpty(actualSql)) {
            return AnnoResult.failure("SQL不存在，请检查配置");
        }

        List<Map<String, Object>> mapList = AnnoDbContext.dynamicDbContext(dbName,
            () -> baseService.sql2MapList(actualSql));

        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            mapList, "label", "id", "pid"
        );
        return AnnoResult.succeed(trees);
    }

    /**
     * 从实体类加载字典数据
     */
    private AnnoResult<List<AnnoTreeDTO<String>>> loadDictFromEntity(
        String annoClazz, String idKey, String labelKey
    ) {
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(annoClazz);
        List<Object> list = queryTreeList(criteria);

        List<AnnoTreeDTO<String>> annoTreeDTOs;
        if (StrUtil.isNotBlank(idKey) && StrUtil.isNotBlank(labelKey)) {
            annoTreeDTOs = Utils.toTrees(list, idKey, labelKey);
        } else {
            annoTreeDTOs = Utils.toTrees(list);
        }

        // 添加默认"无选择"选项
        annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder()
            .id("").label("无选择").title("无选择").value("").key("").build());

        return AnnoResult.succeed(annoTreeDTOs);
    }

    /**
     * 从下拉框选项提供器加载数据
     */
    private AnnoResult<List<AnnoTreeDTO<String>>> loadDictFromOptionProvider(String optionAnnoClazz) {
        Class<? extends OptionDataSupplier> providerClass = OptionDataSupplier.CLASS_MAP.get(optionAnnoClazz);
        if (providerClass == null) {
            return AnnoResult.failure("未找到对应的下拉框提供器:" + optionAnnoClazz);
        }

        List<AnnoOptionTypeImpl.OptionDataImpl> optionDataList =
            AnnoBeanUtils.getBean(providerClass).getOptionDataList();

        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            optionDataList, "label", "value", "pid"
        );
        return AnnoResult.succeed(trees);
    }

    /**
     * 从树形数据提供器加载数据
     */
    private AnnoResult<List<AnnoTreeDTO<String>>> loadDictFromTreeProvider(String treeAnnoClazz) {
        Class<? extends TreeDataSupplier> providerClass = TreeDataSupplier.CLASS_MAP.get(treeAnnoClazz);
        if (providerClass == null) {
            return AnnoResult.failure("未找到对应的树下拉框提供器:" + treeAnnoClazz);
        }

        List<AnnoTreeTypeImpl.TreeDataImpl> treeDataList =
            AnnoBeanUtils.getBean(providerClass).getTreeDataList();

        List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
            treeDataList, "label", "id", "pid"
        );
        return AnnoResult.succeed(trees);
    }
}