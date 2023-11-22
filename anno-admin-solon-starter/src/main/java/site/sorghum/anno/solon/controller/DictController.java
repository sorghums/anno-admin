package site.sorghum.anno.solon.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.Utils;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * dict控制器
 *
 * @author Sorghum
 * @since 2023/11/17
 */
@Mapping("/amis/system/dict")
public class DictController {

    @Inject("dbServiceWithProxy")
    DbService dbService;

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Mapping("/loadAllDict")
    public AnnoResult<List<AnnoTreeDTO<String>>> loadAllDict(
        @Param("sqlKey") String sqlKey,
        @Param("annoClazz") String annoClazz,
        @Param("idKey") String idKey,
        @Param("labelKey") String labelKey
    ) {
        if (StrUtil.isNotBlank(sqlKey)) {
            String actualSql = QuerySqlCache.get(sqlKey);
            if (StrUtil.isEmpty(actualSql)) {
                return AnnoResult.failure("sql 不存在,请检查相关配置项");
            }
            List<Map<String, Object>> mapList = dbService.sql2MapList(actualSql);
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                mapList, "label", "id", "pid"
            );
            return AnnoResult.succeed(trees);
        }
        if (StrUtil.isNotBlank(annoClazz)) {
            List<Object> list = queryTreeList(annoClazz,new ArrayList<DbCondition>());
            List<AnnoTreeDTO<String>> annoTreeDTOs = null;
            if (StrUtil.isNotBlank(idKey) && StrUtil.isNotBlank(labelKey)) {
                annoTreeDTOs = Utils.toTrees(list,idKey,labelKey);
            }else {
                annoTreeDTOs = Utils.toTrees(list);
            }
            annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder().id("0").label("无选择").title("无选择").value("").key("").build());
            return AnnoResult.succeed(annoTreeDTOs);
        }
        return AnnoResult.succeed(Collections.emptyList());
    }

    private <T> List<T> queryTreeList(String annoClazz,List<DbCondition> dbConditions) {
        permissionProxy.checkPermission(metadataManager.getEntity(annoClazz), PermissionProxy.VIEW);
        TableParam<T> tableParam = (TableParam<T>) metadataManager.getTableParam(annoClazz);
        return dbService.list(tableParam.getClazz(), dbConditions);
    }


}
