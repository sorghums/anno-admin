package site.sorghum.anno._common.util;


import cn.hutool.core.date.DateUtil;
import org.noear.snack.ONode;
import org.noear.snack.core.Feature;
import org.noear.snack.core.Options;
import site.sorghum.anno.pf4j.Pf4jWholeClassLoader;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author Sorghum
 * @since 2023/06/25
 */
public class JSONUtil {

    private static final Options DEFAULT_OPTIONS;

    private static final String SPLIT = "\\.";

    static {
        DEFAULT_OPTIONS = Options.def();
        DEFAULT_OPTIONS.setClassLoader(new Pf4jWholeClassLoader());
        DEFAULT_OPTIONS.addEncoder(
            LocalDateTime.class,
            (data,node) -> {
                if (data == null) {
                    node.val().set(null);
                    return;
                } else {
                    node.val().set(DateUtil.format(data, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        );
        DEFAULT_OPTIONS.addEncoder(
            Date.class,
            (data,node) -> {
                if (data == null) {
                    node.val().set(null);
                    return;
                } else {
                    node.val().set(DateUtil.format(data, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        );

        DEFAULT_OPTIONS.addEncoder(
            java.sql.Date.class,
            (data,node) -> {
                if (data == null) {
                    node.val().set(null);
                    return;
                } else {
                    node.val().set(DateUtil.format(data, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        );

        DEFAULT_OPTIONS.addEncoder(
            LocalDate.class,
            (data,node) -> {
                if (data == null) {
                    node.val().set(null);
                    return;
                } else {
                    node.val().set(data.toString());
                }
            }
        );



        DEFAULT_OPTIONS.addEncoder(Class.class,(data, node)  -> {
            if (data == null) {
                node.val().set(null);
                return;
            } else {
                String[] names = data.getName().split(SPLIT);
                node.val().set(names[names.length - 1]);
            }
        });
        DEFAULT_OPTIONS.add(Feature.EnumUsingName);
    }

    public static <T> List<T> toBeanList(String json, Class<T> objectClass) {
        return ONode.loadStr(json, DEFAULT_OPTIONS).toObjectList(objectClass);
    }
    public static <T> List<T> toBeanList(Object object, Class<T> objectClass) {
        return ONode.loadObj(object, DEFAULT_OPTIONS).toObjectList(objectClass);
    }

    public static <T> T toBean(Map<?,?> map, Class<T> objectClass) {
        return ONode.loadObj(map, DEFAULT_OPTIONS).toObject(objectClass);
    }

    public static <T> T toBean(String json, Class<T> objectClass) {
        return ONode.loadStr(json, DEFAULT_OPTIONS).toObject(objectClass);
    }

    public static <T> T toBean(Object object, Class<T> objectClass) {
        if (object instanceof Map) {
            return toBean((Map<?, ?>) object, objectClass);
        }
        if (object instanceof String) {
            return toBean((String) object, objectClass);
        }
        return ONode.loadObj(object, DEFAULT_OPTIONS).toObject(objectClass);
    }

    public static <T> T toBean(URL url, Class<T> objectClass) {
        return ONode.loadObj(url, DEFAULT_OPTIONS).toObject(objectClass);
    }

    public static <T> T copy(T object) {
        return ONode.loadObj(object, DEFAULT_OPTIONS).toObject(object.getClass());
    }

    public static <T> T read(Object obj, String path, Class<T> type) {
        return ONode.loadObj(obj, DEFAULT_OPTIONS).select(path).toObject(type);
    }

    public static <T> List<T> readList(Object obj, String path, Class<T> type) {
        return ONode.loadObj(obj, DEFAULT_OPTIONS).select(path).toObjectList(type);
    }

    public static Object write(Object obj, String path, Object value) {
        return ONode.loadObj(obj, DEFAULT_OPTIONS).set(path, value).toObject(obj.getClass());
    }

    public static String toJsonString(Object object) {
        return ONode.loadObj(object, DEFAULT_OPTIONS).toJson();
    }
}
