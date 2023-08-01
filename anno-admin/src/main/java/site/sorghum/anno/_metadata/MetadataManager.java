package site.sorghum.anno._metadata;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.db.param.RemoveParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.anno.util.AnnoTableParamCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 元数据管理
 *
 * @author songyinyin
 * @since 2023/7/12 21:32
 */
@Named
public class MetadataManager {

    private final Map<String, AnEntity> entityMap = new ConcurrentHashMap<>();


    @Inject
    private EntityMetadataLoader entityMetadataLoader;

    /**
     * 从实体类中加载元数据
     *
     * @param clazz anno 实体类
     */
    public void loadEntity(Class<?> clazz) {
        if (entityMap.containsKey(entityMetadataLoader.getEntityName(clazz))) {
            return;
        }

        AnEntity entity = entityMetadataLoader.load(clazz);

        postProcess(entity);
    }

    /**
     * 加载自定义元数据
     */
    public void loadCustomized(Object object) {
        CustomizedMetadataLoader customizedMetadataLoader = AnnoBeanUtils.getBean(CustomizedMetadataLoader.class);
        if (entityMap.containsKey(customizedMetadataLoader.getEntityName(object))) {
            return;
        }

        AnEntity entity = customizedMetadataLoader.load(object);

        postProcess(entity);
    }

    private void postProcess(AnEntity entity) {
        entityMap.put(entity.getEntityName(), entity);
        TableParam tableParam = new TableParam();

        tableParam.setClazz(entity.getClazz());
        tableParam.setTableName(entity.getTableName());
        List<String> columns = entity.getFields().stream().map(AnField::getTableFieldName).collect(Collectors.toList());
        tableParam.setColumns(columns);
        if (entity.getRemoveType() == 0) {
            tableParam.setRemoveParam(new RemoveParam());
        } else {
            tableParam.setRemoveParam(new RemoveParam(true, entity.getRemoveField(), entity.getRemoveValue(), entity.getNotRemoveValue()));
        }
        AnnoTableParamCache.put(entity.getEntityName(), tableParam);
    }

    /**
     * 获取表数据
     *
     * @param entityName entityName
     * @return {@link TableParam}
     */
    public TableParam getTableParam(String entityName) {
        return AnnoTableParamCache.get(entityName);
    }

    /**
     * 获取表数据
     *
     * @param clazz clazz
     * @return {@link TableParam}
     */
    public TableParam getTableParam(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        return AnnoTableParamCache.get(entityName);
    }

    /**
     * 获取所有实体的元数据（无序）
     */
    public List<AnEntity> getAllEntity() {
        return new ArrayList<>(entityMap.values());
    }

    /**
     * 获取实体类的元数据
     *
     * @param entityName 实体名
     * @return 元数据
     */
    public AnEntity getEntity(String entityName) {
        AnEntity anEntity = entityMap.get(entityName);
        if (anEntity == null) {
            throw new BizException("entity: %s is not found".formatted(entityName));
        }
        return anEntity;
    }

    /**
     * 获取实体类的元数据
     *
     * @param entityClass 实体类
     * @return 元数据
     */
    public AnEntity getEntity(Class<?> entityClass) {
        String entityName = entityMetadataLoader.getEntityName(entityClass);
        return getEntity(entityName);
    }

    /**
     * 获取实体类的字段信息
     *
     * @param entityName 实体名
     * @param fieldName  字段名
     * @return 字段信息
     */
    public AnField getEntityField(String entityName, String fieldName) {
        AnEntity entity = getEntity(entityName);

        AnField field = entity.getField(fieldName);
        if (field == null) {
            throw new BizException("%s field: %s is not found".formatted(entityName, fieldName));
        }
        return field;
    }

    /**
     * 获取实体类的字段信息
     *
     * @param clazz     实体类
     * @param fieldName 字段名
     * @return 字段信息
     */
    public <T> AnField getEntityField(Class<T> clazz, String fieldName) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        AnEntity entity = getEntity(entityName);
        AnField field = entity.getField(fieldName);
        if (field == null) {
            throw new BizException("%s field: %s is not found".formatted(entityName, fieldName));
        }
        return field;
    }

}