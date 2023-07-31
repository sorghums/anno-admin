package site.sorghum.anno.anno.util;

import site.sorghum.anno._common.entity.BiHashMap;

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
     */
    public static Map<Class<?>, BiHashMap<String, Field>> clazz2Sql2Field = new HashMap<>();

    /**
     * 类到java变量名到field的映射
     */
    public static Map<Class<?>,BiHashMap<String,Field>> clazz2Name2Field = new HashMap<>();

    /**
     * 存入缓存
     *
     * @param sqlColumnName   数据库字段名
     * @param field field
     */
    public static synchronized void putFieldName2FieldAndSql(Class<?> clazz, String sqlColumnName, Field field) {
        clazz2Sql2Field.putIfAbsent(clazz, new BiHashMap<>());
        clazz2Name2Field.putIfAbsent(clazz, new BiHashMap<>());
        clazz2Sql2Field.get(clazz).put(sqlColumnName, field);
        clazz2Name2Field.get(clazz).put(field.getName(), field);
    }

    /**
     * 获取field by 数据库字段名
     *
     * @param sqlColumnName 数据库字段名
     * @return {@link Field}
     */
    public static synchronized Field getFieldBySqlColumn(Class<?> clazz, String sqlColumnName) {
        return clazz2Sql2Field.get(clazz).get(sqlColumnName);
    }

    /**
     * 获取数据库字段名 by field
     *
     * @param field field
     * @return {@link Field}
     */
    public static synchronized String getSqlColumnByField(Class<?> clazz, Field field) {
        return clazz2Sql2Field.get(clazz).reverseGet(field);
    }

    /**
     * 获取field by java变量名
     *
     * @param name java变量名
     * @return {@link Field}
     */
    public static synchronized Field getFieldByJavaName(Class<?> clazz, String name) {
        return clazz2Name2Field.get(clazz).get(name);
    }

    /**
     * 获取Sql字段名 by java变量名
     * @param clazz 类
     * @param name java变量名
     * @return {@link String}
     */
    public static synchronized String getSqlColumnByJavaName(Class<?> clazz, String name) {
        return clazz2Sql2Field.get(clazz).reverseGet(clazz2Name2Field.get(clazz).get(name));
    }



}
