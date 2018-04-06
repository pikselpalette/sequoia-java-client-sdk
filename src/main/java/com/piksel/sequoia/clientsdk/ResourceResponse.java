package com.piksel.sequoia.clientsdk;

import java.util.Optional;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;

/**
 * <p>
 * Response holds the information retrieved after interacting with the
 * resourceful services.
 * </p>
 */
@SuppressWarnings("rawtypes")
@PublicEvolving
public interface ResourceResponse<T extends Resource> extends Response {

    Optional<ResourceIterable<T>> getPayload();

}
