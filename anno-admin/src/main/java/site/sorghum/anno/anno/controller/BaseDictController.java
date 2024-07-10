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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 基本dict控制器
 *
 * @author Sorghum
 * @since 2024/01/11
 */
@Slf4j
public class BaseDictController {

    @Inject
    AnnoBaseService baseService;

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;


    public AnnoResult<List<AnnoTreeDTO<String>>> loadDict(
        String sqlKey,
        String annoClazz,
        String idKey,
        String labelKey,
        String optionAnnoClazz,
        String treeAnnoClazz,
        Map<String, Object> _extra
    ) {
        if (StrUtil.isNotBlank(sqlKey)) {
            String actualSql = QuerySqlCache.get(sqlKey);
            if (StrUtil.isEmpty(actualSql)) {
                return AnnoResult.failure("sql 不存在,请检查相关配置项");
            }
            List<Map<String, Object>> mapList = baseService.sql2MapList(actualSql);
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                mapList, "label", "id", "pid"
            );
            return AnnoResult.succeed(trees);
        }
        if (StrUtil.isNotBlank(annoClazz)) {
            DbCriteria criteria = new DbCriteria();
            criteria.setEntityName(annoClazz);
            List<Object> list = queryTreeList(criteria);
            List<AnnoTreeDTO<String>> annoTreeDTOs;
            if (StrUtil.isNotBlank(idKey) && StrUtil.isNotBlank(labelKey)) {
                annoTreeDTOs = Utils.toTrees(list, idKey, labelKey);
            } else {
                annoTreeDTOs = Utils.toTrees(list);
            }
            annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder().id("").label("无选择").title("无选择").value("").key("").build());
            return AnnoResult.succeed(annoTreeDTOs);
        }
        if (StrUtil.isNotBlank(optionAnnoClazz)) {
            Class<? extends OptionDataSupplier> aClass = OptionDataSupplier.CLASS_MAP.get(optionAnnoClazz);
            if (aClass == null) {
                return AnnoResult.failure("未找到对应的下拉框提供器:" + optionAnnoClazz);
            }
            List<AnnoOptionTypeImpl.OptionDataImpl> optionDataList = AnnoBeanUtils.getBean(aClass).getOptionDataList();
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                optionDataList, "label", "value", "pid"
            );
            return AnnoResult.succeed(trees);
        }
        if (StrUtil.isNotBlank(treeAnnoClazz)) {
            Class<? extends TreeDataSupplier> aClass = TreeDataSupplier.CLASS_MAP.get(treeAnnoClazz);
            if (aClass == null) {
                return AnnoResult.failure("未找到对应的树下拉框提供器:" + treeAnnoClazz);
            }
            List<AnnoTreeTypeImpl.TreeDataImpl> optionDataList = AnnoBeanUtils.getBean(aClass).getTreeDataList();
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                optionDataList, "label", "id", "pid"
            );
            return AnnoResult.succeed(trees);
        }
        return AnnoResult.succeed(Collections.emptyList());
    }

    public AnnoResult<String> transOne(String annoClazz, String idKey, String labelKey, String idValue) {
        AnEntity managerEntity = metadataManager.getEntity(annoClazz);
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(annoClazz);
        String sqlIdKey = AnnoFieldCache.getSqlColumnByJavaName(managerEntity.getThisClass(), idKey);
        criteria.addCondition(sqlIdKey, QueryType.EQ, idValue);

        List<Object> list = queryTreeList(criteria);
        if (list.isEmpty()) {
            return AnnoResult.succeed(idValue);
        }
        for (Object o : list) {
            Object fieldValue = ReflectUtil.getFieldValue(
                o, labelKey
            );
            return AnnoResult.succeed(fieldValue == null ? idValue : fieldValue.toString());
        }
        return AnnoResult.succeed(idValue);
    }

    private <T> List<T> queryTreeList(DbCriteria criteria) {
        permissionProxy.checkPermission(metadataManager.getEntity(criteria.getEntityName()), PermissionProxy.VIEW);
        return baseService.list(criteria);
    }
}
