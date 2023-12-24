package site.sorghum.anno.anno.util;

import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.entity.BiHashMap;
import site.sorghum.anno._metadata.MetadataManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Anno字段缓存
 *
 * @author Sorghum
 * @since 2023/07/10
 */
public class AnnoFieldCache {

    /**
     * 类到数据库字段到field的映射
     * entityClass -> sqlColumnName -> fieldName
     */
    public static Map<Class<?>, BiHashMap<String, String>> clazz2Sql2Field = new HashMap<>();

    /**
     * 存入缓存
     *
     * @param sqlColumnName 数据库字段名
     * @param fieldName     java变量名
     */
    public static void putFieldName2FieldAndSql(Class<?> clazz, String sqlColumnName, String fieldName) {
        BiHashMap<String, String> map = clazz2Sql2Field.computeIfAbsent(clazz, k -> new BiHashMap<>());
        if (!map.containsKey(sqlColumnName)) {
            map.put(sqlColumnName, fieldName);
        }
    }

    /**
     * 获取field by 数据库字段名
     *
     * @param sqlColumnName 数据库字段名
     * @return {@link Field}
     */
    public static String getFieldNameBySqlColumn(Class<?> clazz, String sqlColumnName) {
        return clazz2Sql2Field.get(clazz).get(sqlColumnName);
    }

    /**
     * 获取Sql字段名 by java变量名
     *
     * @param clazz     类
     * @param fieldName java变量名
     * @return {@link String}
     */
    public static String getSqlColumnByJavaName(Class<?> clazz, String fieldName) {
        return clazz2Sql2Field.get(clazz).reverseGet(fieldName);
    }

    /**
     * 按实体名称获取clazz
     *
     * @param entityName 实体名称
     * @return {@link Class}<{@link ?}>
     */
    public static synchronized Class<?> getClazzByEntityName(String entityName) {
        return AnnoBeanUtils.getBean(MetadataManager.class).getEntity(entityName).getClazz();
    }

}
