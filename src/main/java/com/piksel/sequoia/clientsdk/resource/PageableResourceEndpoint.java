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

import java.util.Optional;

import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.exception.NotImplementedException;

/**
 * An extension to a normal resource endpoint that is capable of providing paged
 * resources from a provided url.
 *
 * @param <T>
 *            the resource over which the endpoint operates
 */
@PublicEvolving
public interface PageableResourceEndpoint<T extends Resource> extends ResourcefulEndpoint<T> {

    Optional<JsonElement> getPagedResource(String url);

    Optional<JsonElement> getPagedLinkedResource(String next);

    default PageableResourceEndpoint<T> getLinkedPages(String resourceKey, String endpointLocation, Class<T> resourceClass) {
        throw new NotImplementedException();
    }

}
