package site.sorghum.anno.anno.controller;

import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.datasupplier.AnnoDynamicFormAndDataSupplier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 功能控制器
 * 提供实体元数据查询和默认数据添加功能
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AnEntityBaseController {

    @Inject
    protected MetadataManager metadataManager;

    /**
     * 获取实体元数据
     *
     * @param clazz 实体类名
     * @return 包含实体元数据的响应结果
     */
    public AnnoResult<Map<Object,Object>> getEntityMetadata(String clazz) {
        Objects.requireNonNull(clazz, "Entity class name cannot be null");

        AnEntity entity = metadataManager.getEntity(clazz);
        if (entity == null) {
            return AnnoResult.failure("Entity not found for class: " + clazz);
        }

        //noinspection unchecked
        return AnnoResult.succeed(JSONUtil.toBean(entity, Map.class));
    }

    /**
     * 获取默认添加数据
     *
     * @param entityName   实体名称
     * @param columnIdList 列ID列表
     * @return 包含默认数据的Map
     * @throws IllegalArgumentException 如果参数为null
     */
    public Map<String, Object> getDefaultAddData(String entityName, List<String> columnIdList) {
        Objects.requireNonNull(entityName, "Entity name cannot be null");
        Objects.requireNonNull(columnIdList, "Column ID list cannot be null");

        return AnnoDynamicFormAndDataSupplier.getInstance(entityName)
            .execute(entityName, columnIdList);
    }
}