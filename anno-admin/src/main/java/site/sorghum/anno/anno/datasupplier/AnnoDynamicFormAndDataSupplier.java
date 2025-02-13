package site.sorghum.anno.anno.datasupplier;

import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.EntityMetadataLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AnnoDynamicFormAndDataSupplier {

    /**
     * 缓存
     */
    Map<String, AnnoDynamicFormAndDataSupplier> cache = new HashMap<>();

    /**
     * 空
     */
    AnnoDynamicFormAndDataSupplier empty = (entityName, columnDataIds) -> Collections.emptyMap();

    /**
     * 获取实例
     * @param entityName 实体名称
     * @return 实例
     */
    static AnnoDynamicFormAndDataSupplier getInstance(String entityName) {
        return cache.getOrDefault(entityName, empty);
    }

    /**
     * 设置实例
     * @param entityClass 实体类
     * @param supplier 实例
     */
    static void putInstance(Class<?> entityClass, AnnoDynamicFormAndDataSupplier supplier) {
        cache.put(EntityMetadataLoader.entityName(entityClass), supplier);
    }

    /**
     * 获取数据
     *
     * @param entityName    实体名称
     * @param columnDataIds 列数据id
     * @return 数据
     */
    Map<String, Object> get(
        String entityName,
        List<String> columnDataIds
    );

    /**
     * 获取额外动态输入
     * @param entityName    实体名称
     * @param columnDataIds 列数据id
     * @return  额外动态输入
     */
    default List<AnField> getExtraDynamicInput(
        String entityName,
        List<String> columnDataIds
    ){
        return Collections.emptyList();
    }

    /**
     * 执行获取数据
     * @param entityName 实体名称
     * @param columnDataIds 列数据id
     * @return 数据
     */
    default Map<String,Object> execute(String entityName, List<String> columnDataIds){
        Map<String, Object> stringObjectMap = get(entityName, columnDataIds);
        if (stringObjectMap == null) {
            return Collections.emptyMap();
        }
        if (stringObjectMap instanceof HashMap<String,Object> hashMap) {
            hashMap.put("__extraDynamicInputList", getExtraDynamicInput(entityName, columnDataIds));
            return hashMap;
        }else {
            HashMap<String, Object> hashMap = new HashMap<>(stringObjectMap);
            hashMap.put("__extraDynamicInputList", getExtraDynamicInput(entityName, columnDataIds));
            return hashMap;
        }
    }
}
