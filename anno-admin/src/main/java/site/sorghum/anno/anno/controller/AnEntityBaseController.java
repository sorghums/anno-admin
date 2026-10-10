package site.sorghum.anno.anno.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.datasupplier.AnnoDynamicFormAndDataSupplier;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.ArrayList;
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

    /**
     * 权限编码字段
     */
    private static final String PERMISSION_CODE_FIELD = "permissionCode";

    /**
     * 表按钮列表字段名
     */
    private static final String TABLE_BUTTONS_FIELD = "tableButtons";

    /**
     * 行按钮列表字段名
     */
    private static final String COLUMN_BUTTONS_FIELD = "columnButtons";

    @Inject
    protected MetadataManager metadataManager;

    @Inject
    protected PermissionProxy permissionProxy;

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
        Map<Object, Object> metadata = JSONUtil.toBean(entity, Map.class);
        // 过滤掉无权限的按钮，避免展示出来，提交时又被拦截
        filterNoPermissionItems(entity, metadata);
        return AnnoResult.succeed(metadata);
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

    /**
     * 过滤掉无权限的按钮(表按钮、行按钮)
     *
     * @param entity   实体元数据
     * @param metadata 实体元数据序列化后的Map
     */
    private void filterNoPermissionItems(AnEntity entity, Map<Object, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return;
        }
        filterNoPermissionItems(entity, metadata, TABLE_BUTTONS_FIELD);
        filterNoPermissionItems(entity, metadata, COLUMN_BUTTONS_FIELD);
    }

    /**
     * 过滤掉列表中无权限的元素(按钮等)
     *
     * @param entity      实体元数据
     * @param metadata    实体元数据序列化后的Map
     * @param itemsName   列表字段名
     */
    private void filterNoPermissionItems(AnEntity entity, Map<Object, Object> metadata, String itemsName) {
        Object value = metadata.get(itemsName);
        if (!(value instanceof List)) {
            return;
        }
        List<?> items = (List<?>) value;
        List<Object> hasPermissionItems = new ArrayList<>(items.size());
        for (Object item : items) {
            if (hasItemPermission(entity, item)) {
                hasPermissionItems.add(item);
            }
        }
        metadata.put(itemsName, hasPermissionItems);
    }

    /**
     * 判断按钮是否有权限
     *
     * @param entity 实体元数据
     * @param item   按钮元数据序列化后的Map
     * @return 有权限(或未配置权限码)返回 true
     */
    private boolean hasItemPermission(AnEntity entity, Object item) {
        if (!(item instanceof Map)) {
            return true;
        }
        Object permissionCode = ((Map<?, ?>) item).get(PERMISSION_CODE_FIELD);
        // 未配置权限码时不做限制(序列化后通常是空串，故不能只判 null)
        if (permissionCode == null || StrUtil.isBlank(permissionCode.toString())) {
            return true;
        }
        return permissionProxy.hasPermission(entity, permissionCode.toString());
    }
}
