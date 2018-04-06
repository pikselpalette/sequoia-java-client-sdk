package com.piksel.sequoia.clientsdk;

import java.util.Optional;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Response holds the information retrieved after interacting with a service.
 */
@PublicEvolving
public interface Response<T> {

    Optional<T> getPayload();

    int getStatusCode();

    boolean isSuccessStatusCode();

}
