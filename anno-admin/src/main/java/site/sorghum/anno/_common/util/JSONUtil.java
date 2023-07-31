package site.sorghum.anno._common.util;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.*;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno._common.exception.BizException;

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

    public static <T> T toBean(Map<?,?> map, Class<T> objectClass) {
        if (map instanceof JSONObject) {
            return ((JSONObject) map).toJavaObject(objectClass);
        }
        JSONObject jsonObject = new JSONObject();
        BeanUtil.copyProperties(map,jsonObject);
        return jsonObject.toJavaObject(objectClass, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> T toBean(String json, Class<T> objectClass) {
        return JSONObject.parseObject(json, objectClass);
    }

    public static <T> T toBean(Object object, Class<T> objectClass) {
        if (object instanceof Map) {
            return toBean((Map<?, ?>) object, objectClass);
        }
        if (object instanceof String) {
            return toBean((String) object, objectClass);
        }
        return JSONObject.parseObject(JSONObject.toJSONString(object), objectClass);
    }

    public static <T> T toBean(URL url, Class<T> objectClass) {
        return JSON.parseObject(url, objectClass);
    }

    public static <T> T copy(T object) {
        return JSON.copy(object);
    }

    public static <T> T read(Object obj, String path, Class<T> type) {
        Object eval = JSONPath.eval(obj, path);
        if (eval instanceof JSONArray) {
            throw new BizException("类型不匹配");
        }
        if (eval instanceof JSONObject evalJson) {
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

    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    public static List<Options.Option> parseArray(String jsonString, Class<Options.Option> optionClass) {
        return JSONArray.parseArray(jsonString, optionClass);
    }
}
