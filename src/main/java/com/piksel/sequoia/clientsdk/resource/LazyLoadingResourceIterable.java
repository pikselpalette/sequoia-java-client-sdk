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

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public final class LazyLoadingResourceIterable<T extends Resource>
        extends AbstractLazyLoadingIterable<T> implements ResourceIterable<T> {

    public LazyLoadingResourceIterable(JsonElement payload, PageableResourceEndpoint<T> endpoint,
            Gson gson) {
        super(payload, endpoint, gson);
        init(payload);
    }

    public LazyLoadingResourceIterable(JsonElement payload, PageableResourceEndpoint<T> endpoint,
            Gson gson, Map<? extends String,?> headers) {
        super(payload, endpoint, gson, headers);
        init(payload);
    }

    @Override
    public T single() {
        if (pageIndex != FIRST_PAGE || currentPage().isNotLast() || currentPage().items() != 1) {
            throw new NotSingularException();
        }
        return next();
    }

    @Override
    public Optional<Map<String,Map<String,Integer>>> facetCount() {
        return Optional.ofNullable(facetCount);
    }

    protected void init(JsonElement payload) {
        this.pageIndex = addPage(payload);
    }

    @Override
    protected void loadNextAndUpdateIndexes() {
        Optional<JsonElement> payload = endpoint.getPagedResource(getNextPage(), headers);
        this.pageIndex = addPage(payload.orElseThrow(noSuchElementException()));
        resourceIndex = 0;
    }

    @Override
    protected boolean theCurrentPageHasContents() {
        return currentPage().items() > 0;
    }

    @Override
    protected boolean nextPageContainsResources() {
        return true;
    }

}
