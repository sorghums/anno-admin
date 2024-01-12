package site.sorghum.anno.db.interfaces;


import site.sorghum.anno.db.param.TableParam;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * AnnoAdmin核心拓展服务
 *
 * @author sorghum
 * @date 2023/09/06
 */
public class AnnoAdminCoreFunctions {

    /**
     * 表参数获取函数
     */
    public static Function<Class, TableParam> tableParamFetchFunction;

    /**
     * 字段是否可以清空函数
     */
    public static BiFunction<Class, String, Boolean> sqlFieldCanClearFunction;

    /**
     * 数据库字段到Java字段映射函数
     */
    public static BiFunction<Class, String, Field> sqlFieldToJavaFieldFunction;
}
