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

import static java.util.Objects.nonNull;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class AbstractLazyLoadingIterable<T extends Resource> extends AbstractLoadingIterable<T> {

    public AbstractLazyLoadingIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson) {
        super(payload, endpoint, gson);
    }

    public AbstractLazyLoadingIterable(JsonElement payload,
            PageableResourceEndpoint<T> endpoint, Gson gson, Map<? extends String,?> headers) {
        super(payload, endpoint, gson, headers);
    }

    @Override
    public boolean hasNext() {
        return theCurrentPageHasContents()
                && ((currentPage().containsIndex(resourceIndex) || currentPage().isNotLast())
                        && nextPageIsNotEmpty() && nextPageContainsResources());
    }

    protected abstract boolean theCurrentPageHasContents();

    protected abstract boolean nextPageContainsResources();

    protected String getNextPage() {
        return currentPage().getMeta().getContinuesPage();
    }

    protected boolean metaHasNext() {
        return nonNull(currentPage().getMeta().getContinuesPage());
    }

    private boolean nextPageIsNotEmpty() {
        if (lastPageItem(currentPage().getMeta()) && metaHasNext()) {
            loadNextAndUpdateIndexes();
            return (currentPage().items() > 0);
        }
        return true;
    }

    private boolean lastPageItem(AbstractMeta meta) {
        return resourceIndex == meta.getPerPage();
    }

}
