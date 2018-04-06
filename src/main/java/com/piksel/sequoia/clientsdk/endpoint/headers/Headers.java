package com.piksel.sequoia.clientsdk.endpoint.headers;

import java.util.Map;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Methods for managing headers for endpoints.
 */
@PublicEvolving
public interface Headers<T> {

    /**
     * Retrieves the headers.
     */
    Map<? extends String, ?> getHeaders();

    /**
     * Allow to add new header.
     */
    T add(String key, Object value);

}
