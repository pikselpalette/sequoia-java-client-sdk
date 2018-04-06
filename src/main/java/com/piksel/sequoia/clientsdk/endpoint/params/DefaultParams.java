package com.piksel.sequoia.clientsdk.endpoint.params;

import java.util.Map;
import java.util.TreeMap;

/**
 * Implementation of {@link Params} for holding the params added.
 */
class DefaultParams<T extends Params<T>> implements Params<T> {

    Map<String, String> params;

    protected DefaultParams() {
        params = new TreeMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T add(String key, String value) {
        params.put(key, value);
        return (T) this;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
