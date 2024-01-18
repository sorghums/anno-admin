package site.sorghum.anno._metadata;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.db.DbTableContext;
import site.sorghum.anno.db.TableParam;
import site.sorghum.anno.method.MethodTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 元数据管理
 *
 * @author songyinyin
 * @since 2023/7/12 21:32
 */
@Slf4j
@Named
public class MetadataManager {

    private final Map<String, AnEntity> entityMap = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private Set<String> scanPackages;

    public static final String METADATA_TOPIC = "anno.metadata";
    public static final String METADATA_TOPIC_REFRESH = "anno.metadata.refresh";


    @Inject
    private EntityMetadataLoader entityMetadataLoader;
    @Inject
    DbTableContext dbTableContext;

    /**
     * 从实体类中加载元数据，已存在的实体不会重复加载
     *
     * @param clazz anno 实体类
     */
    public AnEntity loadEntity(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        if (entityMap.containsKey(entityName)) {
            return entityMap.get(entityName);
        }

        AnEntity entity = entityMetadataLoader.load(clazz);

        postProcess(entity);
        return entity;
    }

    /**
     * 加载自定义元数据
     */
    public void loadCustomized(Object object) {
        CustomizedMetadataLoader customizedMetadataLoader = AnnoBeanUtils.getBean(CustomizedMetadataLoader.class);
        if (customizedMetadataLoader == null) {
            log.warn("CustomizedMetadataLoader bean is not found");
            return;
        }
        if (entityMap.containsKey(customizedMetadataLoader.getEntityName(object))) {
            return;
        }

        AnEntity entity = customizedMetadataLoader.load(object);

        postProcess(entity);
    }

    /**
     * 当所有anno实体加载完成后，刷新元数据
     */
    public void refresh() {
        MetadataContext sender = MethodTemplateManager.create(MetadataContext.class);
        sender.refresh(getAllEntity());
    }

    protected void postProcess(AnEntity entity) {
        entityMap.put(entity.getEntityName(), entity);
    }

    /**
     * 获取表数据
     *
     * @param entityName entityName
     * @return {@link TableParam}
     */
    public TableParam getTableParam(String entityName) {
        return dbTableContext.getTableParam(entityName);
    }

    /**
     * 获取表数据
     *
     * @param clazz clazz
     * @return {@link TableParam}
     */
    public TableParam getTableParam(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        return dbTableContext.getTableParam(entityName);
    }

    /**
     * 获取表数据（直接存缓存中获取的，不可以修改，修改后会影响缓存的内容）
     *
     * @param clazz clazz
     */
    public TableParam getTableParamImmutable(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        return dbTableContext.getTableParamImmutable(entityName);
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
