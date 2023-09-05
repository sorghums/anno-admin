package site.sorghum.anno.anno.util;

import site.sorghum.anno.db.param.TableParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Anno Clazz 缓存
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class AnnoTableParamCache {
    /**
     * Anno Clazz 缓存
     */
    private static final Map<String, TableParam<?>> STRING_TABLE_PARAM_CACHE = new HashMap<>();


    /**
     * 存入缓存
     *
     * @param key   关键
     * @param tableParam tableParam
     */
    public static synchronized void put(String key,TableParam<?> tableParam){
        STRING_TABLE_PARAM_CACHE.put(key,tableParam);
    }

    /**
     * 获取缓存
     *
     * @param key 关键
     * @return {@link Class}<{@link ?}>
     */
    public static TableParam<?> get(String key){
        TableParam<?> tableParam = STRING_TABLE_PARAM_CACHE.get(key);
        // 复制一份，防止被修改
        TableParam<?> returnParam = new TableParam<>();
        returnParam.setTableName(tableParam.getTableName());
        returnParam.setClazz(tableParam.getClazz());
        returnParam.setColumns(new ArrayList<>(tableParam.getColumns()));
        returnParam.setRemoveParam(tableParam.getRemoveParam());
        returnParam.setVirtualTable(tableParam.isVirtualTable());
        returnParam.setJoinTables(new ArrayList<>(tableParam.getJoinTables()));
        return returnParam;
    }

    /**
     * 获取缓存（不可修改）
     *
     * @param entityName 实体名称
     */
    public static TableParam<?> getImmutable(String entityName){
        return STRING_TABLE_PARAM_CACHE.get(entityName);
    }

}
