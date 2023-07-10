package site.sorghum.anno.common;

import java.util.*;

/**
 * CopyFromRedisson
 *
 * @author Sorghum
 * @since 2023/07/10
 */
public class BiHashMap<K, V> implements Map<K, V> {

    private Map<K, V> keyValueMap = new HashMap<K, V>();
    private Map<V, K> valueKeyMap = new HashMap<V, K>();
    
    @Override
    public int size() {
        return keyValueMap.size();
    }

    @Override
    public boolean isEmpty() {
        return keyValueMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keyValueMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueKeyMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return keyValueMap.get(key);
    }
    
    public K reverseGet(Object key) {
        return valueKeyMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (keyValueMap.containsKey(key)) {
            valueKeyMap.remove(keyValueMap.get(key));
        }
        valueKeyMap.put(value, key);
        return keyValueMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        V removed = keyValueMap.remove(key);
        if (removed != null) {
            valueKeyMap.remove(removed);
        }
        return removed;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keyValueMap.clear();
        valueKeyMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return keyValueMap.keySet();
    }
    
    public Set<V> valueSet() {
        return valueKeyMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return keyValueMap.values();
    }
    
    public Collection<K> keys() {
        return valueKeyMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return keyValueMap.entrySet();
    }
    
    public Set<Entry<V, K>> reverseEntrySet() {
        return valueKeyMap.entrySet();
    }
    
    public void makeImmutable() {
        keyValueMap = Collections.unmodifiableMap(keyValueMap);
        valueKeyMap = Collections.unmodifiableMap(valueKeyMap);
    }
}