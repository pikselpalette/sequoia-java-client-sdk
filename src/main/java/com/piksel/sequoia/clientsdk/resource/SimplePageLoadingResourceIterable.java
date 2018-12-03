package com.piksel.sequoia.clientsdk.resource;

import java.util.HashMap;

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

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PublicEvolving
public final class SimplePageLoadingResourceIterable<T extends Resource> extends LazyLoading<T>
        implements ResourceIterableOnePage<T> {

    private Map<String,Map<String,Integer>> facetCount;

    public SimplePageLoadingResourceIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson) {
        super(payload, endpoint, gson);
        init(payload, new HashMap<>());
    }

    public SimplePageLoadingResourceIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson, Map<? extends String,?> headers) {
        super(payload, endpoint, gson);
        init(payload, headers);
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    boolean theCurrentPageHasContents() {
        return currentPage().items() > 0;
    }

    @Override
    boolean nextPageContainsResources() {
        return true;
    }

    @Override
    public T next() {
        return super.next();
    }

    @Override
    public T single() {
        if (pageIndex != FIRST_PAGE || currentPage().isNotLast() || currentPage().items() != 1) {
            throw new NotSingularException();
        }
        return next();
    }

    @Override
    public Optional<Integer> totalCount() {
        return super.totalCount();
    }

    @Override
    public Optional<Map<String,Map<String,Integer>>> facetCount() {
        return Optional.ofNullable(facetCount);
    }

    private void init(JsonElement payload, Map<? extends String,?> headers) {
        this.pageIndex = addPage(payload);
        this.facetCount = getFacetCount(payload);
        this.headers = headers;
    }

    private Map<String,Map<String,Integer>> getFacetCount(JsonElement payload) {
        Meta meta = deserializer.metaFrom(payload).orElse(deserializer.emptyMeta());
        return meta.getFacetCount();
    }

    @Override
    void loadNextAndUpdateIndexes() {
        Optional<JsonElement> payload = endpoint.getPagedResource(getNextPage(), headers);
        pageIndex = addPage(payload.orElseThrow(noSuchElementException()));
        resourceIndex = 0;
    }

    @Override
    public Optional<String> nextUrl() {
        // TODO Auto-generated method stub
        return null;
    }

}
