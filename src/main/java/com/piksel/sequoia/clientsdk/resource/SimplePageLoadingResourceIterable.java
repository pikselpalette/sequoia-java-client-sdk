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
import java.util.NoSuchElementException;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public final class SimplePageLoadingResourceIterable<T extends Resource>
        extends AbstractLoadingIterable<T> implements ResourceIterable<T> {

    public SimplePageLoadingResourceIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson) {
        super(payload, endpoint, gson);
        init(payload);
    }

    public SimplePageLoadingResourceIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson, Map<? extends String,?> headers) {
        super(payload, endpoint, gson, headers);
        init(payload);
    }

    @Override
    public boolean hasNext() {
        return theCurrentPageHasContents() && this.currentPage().containsIndex(resourceIndex);
    }

    private boolean theCurrentPageHasContents() {
        return this.currentPage().items() > 0;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return this.currentPage().at(resourceIndex++);
        } catch (Page.PageResourceDoesNotExist pne) {
            throw new NoSuchElementException(pne.getMessage());
        }
    }

    @Override
    public T single() {
        if (this.pageIndex != FIRST_PAGE || currentPage().items() != 1) {
            throw new NotSingularException();
        }
        return next();
    }

    @Override
    public Optional<Integer> totalCount() {
        return super.totalCount();
    }

    @Override
    protected void loadNextAndUpdateIndexes() {}

    protected void init(JsonElement payload) {
        this.nextUrl = getNextUrl(payload);
        this.pageIndex = addPage(payload);
    }

}
