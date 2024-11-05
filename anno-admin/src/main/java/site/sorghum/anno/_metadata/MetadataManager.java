package site.sorghum.anno._metadata;

import cn.hutool.core.lang.Singleton;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.MetaClassUtil;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
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

    @Inject
    private EntityMetadataLoader entityMetadataLoader;

    /**
     * 从实体类中加载元数据，已存在的实体不会重复加载
     *
     * @param clazz anno 实体类
     */
    public AnEntity loadEntity(Class<?> clazz) {
        return loadEntity(clazz, false);
    }

    /**
     * 从YAML内容加载AnEntity对象列表。
     * 不强制加载
     *
     * @param ymlContent YAML格式的内容字符串
     * @return 包含从YAML内容解析得到的AnEntity对象的列表
     */
    public List<AnEntity> loadEntityListByYml(String ymlContent) {
        return loadEntityListByYml(ymlContent, false);
    }

    /**
     * 从YAML内容加载AnEntity对象列表。
     *
     * @param ymlContent YAML格式的内容字符串
     * @param forceLoad  是否强制加载，如果为true，则忽略缓存，重新加载所有实体；如果为false，则优先从缓存中获取实体
     * @return 包含从YAML内容解析得到的AnEntity对象的列表
     */
    public List<AnEntity> loadEntityListByYml(String ymlContent, boolean forceLoad) {
        Map<TypeDescription, Class<?>> classMap = MetaClassUtil.loadClass(ymlContent);
        return classMap.values().stream().map((aClass) -> {
            String entityName = entityMetadataLoader.getEntityName(aClass);
            if (!forceLoad && entityMap.containsKey(entityName)) {
                return entityMap.get(entityName);
            }
            AnEntity entity = entityMetadataLoader.load(aClass);
            postProcess(entity);
            return entity;
        }).toList();
    }

    /**
     * 从实体类中加载表单元数据，已存在的实体不会重复加载
     *
     * @param clazz anno 实体类
     */
    public AnEntity loadFormEntity(Class<?> clazz) {
        return loadFormEntity(clazz, false);
    }


    /**
     * 从实体类中加载元数据，已存在的实体不会重复加载
     *
     * @param clazz     anno 实体类
     * @param forceLoad 强制重新加载
     * @return {@link AnEntity }
     */
    public AnEntity loadEntity(Class<?> clazz, boolean forceLoad) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        if (!forceLoad && entityMap.containsKey(entityName)) {
            return getEntity(entityName);
        }

        AnEntity entity = entityMetadataLoader.load(clazz);
        postProcess(entity);

        return entity;
    }


    /**
     * 从实体映射中移除指定类型的实体。
     *
     * @param clazz 要移除的实体的类型
     */
    public void removeEntity(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        entityMap.remove(entityName);
    }


    /**
     * 根据给定的YAML内容移除实体列表
     *
     * @param ymlContent 包含要移除实体类的类描述的YAML内容
     */
    public void removeEntityListByYml(String ymlContent) {
        Map<TypeDescription, Class<?>> classMap = MetaClassUtil.loadClass(ymlContent);
        classMap.values().stream().forEach((aClass) -> {
            String entityName = entityMetadataLoader.getEntityName(aClass);
            entityMap.remove(entityName);
        });
    }

    /**
     * 从实体类中加载表单元数据，已存在的实体不会重复加载
     *
     * @param clazz     anno 实体类
     * @param forceLoad 强制重新加载
     * @return {@link AnEntity }
     */
    public AnEntity loadFormEntity(Class<?> clazz, boolean forceLoad) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        if (!forceLoad && entityMap.containsKey(entityName)) {
            return getEntity(entityName);
        }
        AnEntity entity = entityMetadataLoader.loadForm(clazz);
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
        Singleton.remove(entity.getEntityName());
        _dynamicProcess(entity);
    }

    /**
     * 获取所有实体的元数据（无序）
     */
    public List<AnEntity> getAllEntity() {
        return _dynamicProcess(new ArrayList<>(entityMap.values()));
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
        return _dynamicProcess(anEntity);
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

    /**
     * 动态处理AnEntity对象
     *
     * @param entity 待处理的AnEntity对象
     * @return 处理后的AnEntity对象
     */
    public AnEntity _dynamicProcess(AnEntity entity) {
        for (AnField field : entity.getFields()) {
            Class<? extends FieldBaseSupplier> defaultValueSupplier = field.getSearch().getDefaultValueSupplier();
            // 搜索默认值手动重新设置
            if (field.getSearch().enable() && defaultValueSupplier != EmptyFieldBaseSupplier.class) {
                runIgnoreException(
                    () -> field.getSearch().setDefaultValue(AnnoBeanUtils.getBean(defaultValueSupplier).get())
                );
            }
        }
        return entity;
    }

    /**
     * 动态处理AnEntity对象
     *
     * @param entity 待处理的AnEntity对象
     * @return 处理后的AnEntity对象
     */
    public List<AnEntity> _dynamicProcess(List<AnEntity> entity) {
        return new ArrayList<>(entity.stream().map(this::_dynamicProcess).toList());
    }

    /**
     * 运行忽略异常
     *
     * @param runnable 可运行
     */
    public void runIgnoreException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("Ignore metadata exception: {}", e.getMessage());
        }
    }
}
