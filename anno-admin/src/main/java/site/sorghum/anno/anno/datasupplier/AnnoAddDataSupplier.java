package site.sorghum.anno.anno.datasupplier;

import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.EntityMetadataLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AnnoAddDataSupplier {

    /**
     * 缓存
     */
    Map<String, AnnoAddDataSupplier> cache = new HashMap<>();

    /**
     * 空
     */
    AnnoAddDataSupplier empty = (entityName, columnDataIds) -> Collections.emptyMap();

    /**
     * 获取实例
     * @param entityName 实体名称
     * @return 实例
     */
    static AnnoAddDataSupplier getInstance(String entityName) {
        return cache.getOrDefault(entityName, empty);
    }

    /**
     * 设置实例
     * @param entityClass 实体类
     * @param supplier 实例
     */
    static void putInstance(Class<?> entityClass, AnnoAddDataSupplier supplier) {
        cache.put(AnnoBeanUtils.getBean(EntityMetadataLoader.class).getEntityName(entityClass), supplier);
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
}
