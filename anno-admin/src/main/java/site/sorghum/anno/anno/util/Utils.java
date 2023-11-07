package site.sorghum.anno.anno.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.db.param.TableParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 树工具
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public class Utils {
    private static MetadataManager metadataManager;

    public static <T> List<AnnoTreeDTO<String>> toTrees(List<T> dataList) {
        init();
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>(1);
        }
        AnEntity anEntity = metadataManager.getEntity(dataList.get(0).getClass());
        return AnnoUtil.buildAnnoTree(dataList, anEntity.getTreeLabel(), anEntity.getTreeKey(), anEntity.getTreeParentKey());
    }

    public static <T> List<AnnoTreeDTO<String>> toTrees(List<T> dataList,String idKey,String labelKey) {
        init();
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>(1);
        }
        AnEntity anEntity = metadataManager.getEntity(dataList.get(0).getClass());
        return AnnoUtil.buildAnnoTree(dataList, labelKey, idKey, anEntity.getTreeParentKey());
    }

    public static <T> String m2mSql(Map<?, ?> param) {
        init();
        if (StrUtil.isBlank(MapUtil.getStr(param, "m2mMediumTableClass"))) {
            return "";
        }
        String mediumOtherField = MapUtil.getStr(param, "m2mMediumTargetField");
        String thisValue = MapUtil.getStr(param, "joinValue");
        String mediumThisField = MapUtil.getStr(param, "m2mMediumThisField");
        AnEntity mediumEntity = metadataManager.getEntity(MapUtil.getStr(param, "m2mMediumTableClass"));
        String mediumTable = mediumEntity.getTableName();
        String sql = "select " + mediumOtherField + " from " + mediumTable + " where " + mediumThisField + " = '" + thisValue + "'";
        return sql(mediumEntity.getClazz(), sql);
    }


    private static <T> String sql(Class<T> clazz, String sql) {
        init();
        // 如果有配置逻辑删除
        TableParam tableParam = metadataManager.getTableParam(clazz);
        if (tableParam.getRemoveParam().getLogic()) {
            sql = sql + " and " + tableParam.getRemoveParam().getRemoveColumn() + " = " + tableParam.getRemoveParam().getNotRemoveValue();
        }
        return sql;
    }


    private static void init() {
        if (metadataManager != null) {
            return;
        }
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
    }


}
