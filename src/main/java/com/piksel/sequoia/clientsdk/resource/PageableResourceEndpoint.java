package com.piksel.sequoia.clientsdk.resource;

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
