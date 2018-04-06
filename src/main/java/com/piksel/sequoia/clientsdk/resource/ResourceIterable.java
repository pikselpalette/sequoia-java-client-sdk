package com.piksel.sequoia.clientsdk.resource;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Provides iterable access over a collection of resources.
 */
@PublicEvolving
public interface ResourceIterable<T extends Resource> extends Iterator<T> {

    /**
     * Used to retrieve the single instance that this collection contains.
     * 
     * @return the single instance that this represents
     * @throws NotSingularException if the iterable has a size that is not 1
     */
    T single();

    /**
     * Used to retrieve the number of resources presents on the query's result.
     */
    Optional<Integer> totalCount();

    /**
     * Used to retrieve the facetCount presents on the query's result.
     */
    Optional<Map<String, Map<String, Integer>>> facetCount();

}