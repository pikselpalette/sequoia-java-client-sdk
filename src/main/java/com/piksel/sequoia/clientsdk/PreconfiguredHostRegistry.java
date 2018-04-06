package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;

/**
 * Registry for the identity service host.
 */
@PublicEvolving
public interface PreconfiguredHostRegistry {

    /**
     * Host configuration for the identity service.
     */
    HostConfiguration identity();

    /**
     * Client credentials for the identity service.
     */
    ComponentCredentials identityClientCredentials();

    /**
     * Host configuration for the registry service.
     */
    HostConfiguration serviceRegistry();

}
