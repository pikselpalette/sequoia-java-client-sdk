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
    
    /**
     * Used to retrieve next url to invoke next page.
     */
    Optional<String> nextUrl();

}
