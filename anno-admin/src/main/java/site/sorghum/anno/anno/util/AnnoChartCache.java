package site.sorghum.anno.anno.util;

import site.sorghum.anno._common.exception.BizException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnnoChartCache {

    private static final Map<String, Class<?>> ANNO_CHART_CACHE = new HashMap<>();

    public static synchronized void put(String key, Class<?> clazz) {
        ANNO_CHART_CACHE.put(key, clazz);
    }

    public static synchronized Class<?> get(String key) {
        return ANNO_CHART_CACHE.get(key);
    }

    public static synchronized Collection<Class<?>> fetchAllClazz() {
        if (ANNO_CHART_CACHE.isEmpty()) {
            throw new BizException("Anno Chart Cache is Empty");
        }
        return ANNO_CHART_CACHE.values();
    }
}
