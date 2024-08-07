package site.sorghum.anno.anno.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnnoMtm;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoTreeImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.db.DbTableContext;
import site.sorghum.anno.db.TableParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 树工具
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public class Utils {
    private static MetadataManager metadataManager;
    private static final DbTableContext dbTableContext = AnnoBeanUtils.getBean(DbTableContext.class);

    public static <T> List<AnnoTreeDTO<String>> toTrees(List<T> dataList) {
        init();
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>(1);
        }
        AnEntity anEntity = metadataManager.getEntity(dataList.get(0).getClass());
        AnnoTreeImpl annoTree = anEntity.getAnnoTree();
        return AnnoUtil.buildAnnoTree(dataList, annoTree.label(), annoTree.key(), annoTree.parentKey());
    }

    public static <T> List<AnnoTreeDTO<String>> toTrees(List<T> dataList,String idKey,String labelKey) {
        init();
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>(1);
        }
        AnEntity anEntity = metadataManager.getEntity(dataList.get(0).getClass());
        AnnoTreeImpl annoTree = anEntity.getAnnoTree();
        return AnnoUtil.buildAnnoTree(dataList, labelKey, idKey, annoTree.parentKey());
    }

    public static <T> String m2mSql(Map<?, ?> param) {
        init();
        if (StrUtil.isBlank(MapUtil.getStr(param, "m2mMediumTableClazz"))) {
            return "";
        }
        AnEntity mediumEntity = metadataManager.getEntity(MapUtil.getStr(param, "m2mMediumTableClazz"));
        String mediumOtherField =AnnoFieldCache.getSqlColumnByJavaName(mediumEntity.getThisClass(),MapUtil.getStr(param,"m2mMediumTargetField"));
        String thisValue = MapUtil.getStr(param, "joinValue");
        String mediumThisField = AnnoFieldCache.getSqlColumnByJavaName(mediumEntity.getThisClass(),MapUtil.getStr(param,"m2mMediumThisField"));
        String mediumTable = mediumEntity.getTableName();
        String sql = "select " + mediumOtherField + " from " + mediumTable + " where " + mediumThisField + " = '" + thisValue + "'";
        return sql(mediumEntity.getThisClass(), sql);
    }

    public static <T> String m2mSql(AnnoButtonImpl.M2MJoinButtonImpl annoMtm, String thisValue) {
        init();
        if (Objects.isNull(annoMtm)) {
            return "";
        }
        AnEntity mediumEntity = metadataManager.getEntity(annoMtm.mediumTableClazz());
        String mediumOtherFieldSql = AnnoMtm.getM2mMediumTargetFieldSql(annoMtm);
        String mediumThisFieldSql = AnnoMtm.getM2mMediumThisFieldSql(annoMtm);
        String mediumTable = mediumEntity.getTableName();
        String sql = "select " + mediumOtherFieldSql + " from " + mediumTable + " where " + mediumThisFieldSql + " = '" + thisValue + "'";
        return sql(mediumEntity.getThisClass(), sql);
    }


    private static <T> String sql(Class<T> clazz, String sql) {
        init();
        // 如果有配置逻辑删除
        TableParam<?> tableParam = dbTableContext.getTableParam(clazz);
        if (tableParam.getDbRemove().getLogic()) {
            sql = sql + " and " + tableParam.getDbRemove().getRemoveColumn() + " = " + tableParam.getDbRemove().getNotRemoveValue();
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
