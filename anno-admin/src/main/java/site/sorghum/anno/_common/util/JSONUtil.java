package site.sorghum.anno._common.util;


import org.noear.snack.ONode;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author Sorghum
 * @since 2023/06/25
 */
public class JSONUtil {

    public static <T> List<T> toBeanList(String json, Class<T> objectClass) {
        return ONode.loadStr(json).toObjectList(objectClass);
    }

    public static <T> T toBean(Map<?,?> map, Class<T> objectClass) {
        return ONode.loadObj(map).toObject(objectClass);
    }

    public static <T> T toBean(String json, Class<T> objectClass) {
        return ONode.loadStr(json).toObject(objectClass);
    }

    public static <T> T toBean(Object object, Class<T> objectClass) {
        if (object instanceof Map) {
            return toBean((Map<?, ?>) object, objectClass);
        }
        if (object instanceof String) {
            return toBean((String) object, objectClass);
        }
        return ONode.loadObj(object).toObject(objectClass);
    }

    public static <T> T toBean(URL url, Class<T> objectClass) {
        return ONode.loadObj(url).toObject(objectClass);
    }

    public static <T> T copy(T object) {
        return ONode.loadObj(object).toObject(object.getClass());
    }

    public static <T> T read(Object obj, String path, Class<T> type) {
        return ONode.loadObj(obj).select(path).toObject(type);
    }

    public static <T> List<T> readList(Object obj, String path, Class<T> type) {
        return ONode.loadObj(obj).select(path).toObjectList(type);
    }

    public static Object write(Object obj, String path, Object value) {
        return ONode.loadObj(obj).set(path, value).toObject(obj.getClass());
    }

    public static String toJsonString(Object object) {
        return ONode.loadObj(object).toJson();
    }
}
