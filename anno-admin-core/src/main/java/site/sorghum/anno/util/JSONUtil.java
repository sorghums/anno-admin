package site.sorghum.anno.util;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import site.sorghum.anno.exception.BizException;

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

    public static <T> T parseObject(Map<?,?> map, Class<T> objectClass) {
        if (map instanceof JSONObject) {
            return ((JSONObject) map).toJavaObject(objectClass);
        }
        JSONObject jsonObject = new JSONObject();
        BeanUtil.copyProperties(map,jsonObject);
        return jsonObject.toJavaObject(objectClass);
    }

    public static <T> T parseObject(String json, Class<T> objectClass) {
        return JSONObject.parseObject(json, objectClass);
    }

    public static <T> T parseObject(Object object, Class<T> objectClass) {
        if (object instanceof Map) {
            return parseObject((Map<?, ?>) object, objectClass);
        }
        if (object instanceof String) {
            return parseObject((String) object, objectClass);
        }
        return JSONObject.parseObject(JSONObject.toJSONString(object), objectClass);
    }

    public static <T> T parseObject(URL url, Class<T> objectClass) {
        return JSON.parseObject(url, objectClass);
    }

    public static <T> T copyObject(T object) {
        return JSON.copy(object);
    }

    public static <T> T read(Object obj, String path, Class<T> type) {
        Object eval = JSONPath.eval(obj, path);
        if (eval instanceof JSONArray) {
            throw new BizException("类型不匹配");
        }
        if (eval instanceof JSONObject) {
            JSONObject evalJson = (JSONObject) eval;
            return evalJson.toJavaObject(type);
        }
        return JSONObject.parseObject(JSON.toJSONString(obj), type);
    }

    public static <T> List<T> readList(Object obj, String path, Class<T> type) {
        Object eval = JSONPath.eval(obj, path);
        if (eval instanceof JSONArray) {
            JSONArray evalArray = (JSONArray) eval;
            return evalArray.toJavaList(type);
        }
        if (eval instanceof JSONObject) {
            throw new BizException("类型不匹配");
        }
        return JSONArray.parseArray(JSON.toJSONString(eval), type);
    }

    public static Object write(Object obj, String path, Object value) {
        return JSONPath.set(obj, path, value);
    }

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

}
