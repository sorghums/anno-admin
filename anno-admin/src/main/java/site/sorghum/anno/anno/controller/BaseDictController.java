package site.sorghum.anno.anno.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * 基本dict控制器
 *
 * @author Sorghum
 * @since 2024/01/11
 */
@Slf4j
public class BaseDictController {

    @Inject("dbServiceWithProxy")
    @Autowired
    @Qualifier("dbServiceWithProxy")
    DbService dbService;

    @Inject
    @Autowired
    MetadataManager metadataManager;

    @Inject
    @Autowired
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
            annoTreeDTOs.add(0, AnnoTreeDTO.<String>builder().id("").label("无选择").title("无选择").value("").key("").build());
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
