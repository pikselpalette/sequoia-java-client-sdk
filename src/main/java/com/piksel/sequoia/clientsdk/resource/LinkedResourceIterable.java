package com.piksel.sequoia.clientsdk.resource;

import java.util.Iterator;
import java.util.Optional;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Provides iterable access over a collection of linked resources.
 */
@PublicEvolving
public interface LinkedResourceIterable<T extends Resource> extends Iterator<T> {
    
    /**
     * Used to retrieve the number of resources presents on the query's result.
     */
    Optional<Integer> totalCount();


}
