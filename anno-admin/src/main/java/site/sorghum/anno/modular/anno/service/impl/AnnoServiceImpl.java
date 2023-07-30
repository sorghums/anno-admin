package site.sorghum.anno.modular.anno.service.impl;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.util.List;
import java.util.Map;

/**
 * Anno服务
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Named
@Slf4j
public class AnnoServiceImpl implements AnnoService {

    @Inject
    MetadataManager metadataManager;

    @Override
    public <T> String sql(Class<T> clazz, String sql) {
        try {
            // 如果有配置逻辑删除
            AnEntity anEntity = metadataManager.getEntity(clazz);
            if (anEntity.getRemoveType() == 1) {
                sql = sql + " and " + anEntity.getRemoveField() + " = " + anEntity.getNotRemoveValue();
            }
            return sql;
        } catch (Exception e) {
            log.error("AnnoService.page error:{}", e.getMessage());
            throw new BizException("权限不足或系统异常", e);
        }
    }

    @Override
    public <T> String m2mSql(Map<?, ?> param) {
        if (StrUtil.isBlank(MapUtil.getStr(param, "mediumTableClass"))) {
            return "";
        }
        String mediumOtherField = MapUtil.getStr(param, "mediumOtherField");
        String otherValue = MapUtil.getStr(param, "joinValue");
        String mediumThisField = MapUtil.getStr(param, "mediumThisField");
        AnEntity mediumEntity = metadataManager.getEntity(MapUtil.getStr(param, "mediumTableClass"));
        String mediumTable = mediumEntity.getTableName();
        String sql = "select " + mediumThisField + " from " + mediumTable + " where " + mediumOtherField + " = '" + otherValue + "'";
        return sql(mediumEntity.getClazz(), sql);
    }

    @Override
    public <T> List<AnnoTreeDTO<String>> annoTrees(Class<T> tClass, List<T> dataList) {
        try {
            AnEntity anEntity = metadataManager.getEntity(tClass);
            return AnnoUtil.buildAnnoTree(dataList, anEntity.getTreeLabel(), anEntity.getTreeKey(), anEntity.getTreeParentKey());
        } catch (Exception e) {
            log.error("AnnoService.annoTrees error:{}", e.getMessage());
            throw new BizException("权限不足或系统异常", e);
        }
    }
}
