package site.sorghum.anno._common.entity;

import cn.hutool.core.convert.Convert;
import site.sorghum.anno._common.exception.BizException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 忽略大小写散列映射
 *
 * @author Sorghum
 * @since 2024/06/28
 */
public class IgnoreCaseHashMap implements Map<String, Object>, Serializable {

    /**
     * 实际存储Map
     */
    HashMap<String, Object> actualMap;


    @Serial
    private static final long serialVersionUID = 1L;

    public IgnoreCaseHashMap() {
        actualMap = new HashMap<>();
    }

    public IgnoreCaseHashMap(int initialCapacity, float loadFactor) {
        actualMap = new HashMap<>(initialCapacity, loadFactor);
    }

    public IgnoreCaseHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IgnoreCaseHashMap(Map<String, Object> m) {
        actualMap = new HashMap<>(_toLowerCaseMap(m));
    }


    @Override
    public int size() {
        return actualMap.size();
    }

    @Override
    public boolean isEmpty() {
        return actualMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return actualMap.containsKey(_toLowerCase(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return actualMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return actualMap.get(_toLowerCase(key));
    }

    public String getString(Object key) {
        return Convert.toStr(actualMap.get(_toLowerCase(key)));
    }
    public Boolean getBol(Object key) {
        return Convert.toBool(actualMap.get(_toLowerCase(key)));
    }
    public Integer getInteger(Object key) {
        return Convert.toInt(actualMap.get(_toLowerCase(key)));
    }
    public Long getLong(Object key) {
        return Convert.toLong(actualMap.get(_toLowerCase(key)));
    }

    public <T> T getObject(Object key, Class<T> clazz) {
        return Convert.convert(clazz, actualMap.get(_toLowerCase(key)));
    }

    @Override
    public Object put(String key, Object value) {
        return actualMap.put(_toLowerCase(key), value);
    }

    @Override
    public Object remove(Object key) {
        return actualMap.remove(_toLowerCase(key));
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        actualMap.putAll(_toLowerCaseMap((Map<String, Object>) m));
    }

    @Override
    public void clear() {
        actualMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return actualMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return actualMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return actualMap.entrySet();
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return actualMap.getOrDefault(_toLowerCase(key), defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        actualMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        actualMap.replaceAll(function);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return actualMap.putIfAbsent(_toLowerCase(key), value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return actualMap.remove(_toLowerCase(key), value);
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        return actualMap.replace(_toLowerCase(key), oldValue, newValue);
    }

    @Override
    public Object replace(String key, Object value) {
        return actualMap.replace(_toLowerCase(key), value);
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return actualMap.computeIfAbsent(_toLowerCase(key), mappingFunction);
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return actualMap.computeIfPresent(_toLowerCase(key), remappingFunction);
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return actualMap.compute(_toLowerCase(key), remappingFunction);
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return actualMap.merge(_toLowerCase(key), value, remappingFunction);
    }


    private static Map<String, Object> _toLowerCaseMap(Map<String, Object> paramMap) {
        Set<String> keySet = new HashSet<>(paramMap.keySet());
        for (String key : keySet) {
            Object value = paramMap.get(key);
            key = _toLowerCase(key);
            if (value instanceof Map) {
                paramMap.put(key, _toLowerCaseMap((Map<String, Object>) value));
            } else {
                paramMap.put(key, value);
            }
        }
        return paramMap;
    }

    private static String _toLowerCase(Object key) {
        if (key == null) {
            throw new BizException("IgnoreCaseHashMap's key cannot be null!");
        }
        if (key instanceof String) {
            return ((String) key).toLowerCase();
        }
        if (key instanceof StringBuilder) {
            return ((StringBuilder) key).toString().toLowerCase();
        }
        if (key instanceof StringBuffer) {
            return ((StringBuffer) key).toString().toLowerCase();
        }
        return key.toString().toLowerCase();
    }
}
