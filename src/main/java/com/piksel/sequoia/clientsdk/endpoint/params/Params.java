package com.piksel.sequoia.clientsdk.endpoint.params;

import java.util.Map;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Methods for managing parameters for endpoints.
 */
@PublicEvolving
public interface Params<T> {

    /**
     * Allows to add parameters key and value.
     */
    T add(String key, String value);

    /**
     * Returns the parameters added.
     */
    Map<String, String> getParams();

}
