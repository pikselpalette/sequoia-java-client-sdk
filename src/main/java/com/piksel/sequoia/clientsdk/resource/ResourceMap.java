package com.piksel.sequoia.clientsdk.resource;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 Piksel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
