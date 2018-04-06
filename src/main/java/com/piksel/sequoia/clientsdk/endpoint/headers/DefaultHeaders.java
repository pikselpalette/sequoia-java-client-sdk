package com.piksel.sequoia.clientsdk.endpoint.headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link Headers} for holding the headers added.
 */
class DefaultHeaders<T extends Headers<T>> implements Headers<T> {

    Map<String, Object> headers;

    protected DefaultHeaders() {
        headers = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T add(String key, Object value) {
        headers.put(key, value);
        return (T) this;
    }

    @Override
    public Map<? extends String, ?> getHeaders() {
        return headers;
    }

}
