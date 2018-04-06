package com.piksel.sequoia.clientsdk.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Getter;
import lombok.ToString;

/**
 * A ResourceMap is an unordered set of name/value pairs, limiting the values to
 * only string, number, boolean, timestamp or arrays thereof.
 */
@PublicEvolving
@Getter
@ToString
public class ResourceMap {

    private Map<String, Object> map;

    public ResourceMap() {
        map = new HashMap<>();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String put(String key, String value) {
        return (String) map.put(key, value);
    }

    public Number put(String key, Number value) {
        return (Number) map.put(key, value);
    }

    public Boolean put(String key, Boolean value) {
        return (Boolean) map.put(key, value);
    }

    public ResourceList put(String key, ResourceList value) {
        return (ResourceList) map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <O> O remove(Object key) {
        return (O) map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<?> values() {
        return map.values();
    }

}
