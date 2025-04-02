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
     * 类到主键名的映射
     */
    public static Map<Class<?>,String> class2PkName = new HashMap<>();

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
     * 将给定的类对象与主键名映射关系存入class2PkName映射中。
     *
     * @param clazz 要映射的类对象
     * @param pkName 与该类对象对应的主键名
     */
    public static void putClass2PkName(Class<?> clazz,String pkName){
        class2PkName.put(clazz,pkName);
    }

    /**
     * 获取指定类对应的主键名称。
     *
     * @param clazz 需要获取主键名称的类对象
     * @return 返回指定类对应的主键名称，如果未找到则返回null
     */
    public static String getPkName(Class<?> clazz){
        return class2PkName.get(clazz);
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
        return AnnoBeanUtils.getBean(MetadataManager.class).getEntity(entityName).getThisClass();
    }

}
