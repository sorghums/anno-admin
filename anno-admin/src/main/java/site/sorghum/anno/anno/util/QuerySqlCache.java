package site.sorghum.anno.anno.util;

import site.sorghum.anno._common.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

public class QuerySqlCache {

    private static final Map<String, String> SQL_CACHE = new HashMap<>();

    public static String generateKey(String fileName, String sql) {
        return fileName + ":" + MD5Util.digestHex(sql);
    }

    public static void put(String key, String value) {
        SQL_CACHE.put(key, value);
    }

    public static String get(String key) {
        return SQL_CACHE.get(key);
    }

}
