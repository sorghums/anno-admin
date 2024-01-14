package site.sorghum.anno.anno.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.proxy.AnnoBaseService;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.db.DbCriteria;

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
        String labelKey
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
        return AnnoResult.succeed(Collections.emptyList());
    }

    private <T> List<T> queryTreeList(DbCriteria criteria) {
        permissionProxy.checkPermission(metadataManager.getEntity(criteria.getEntityName()), PermissionProxy.VIEW);
        return baseService.list(criteria);
    }
}
