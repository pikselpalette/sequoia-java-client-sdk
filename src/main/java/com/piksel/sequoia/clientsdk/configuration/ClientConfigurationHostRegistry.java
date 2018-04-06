package com.piksel.sequoia.clientsdk.configuration;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.PreconfiguredHostRegistry;

/**
 * Provides a {@link PreconfiguredHostRegistry} from the supplied client
 * credentials.
 */
@PublicEvolving
public class ClientConfigurationHostRegistry
        implements PreconfiguredHostRegistry {

    private final ClientConfiguration clientConfiguration;

    public ClientConfigurationHostRegistry(
            ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    public HostConfiguration identity() {
        return clientConfiguration.getIdentityHostConfiguration();
    }

    @Override
    public ComponentCredentials identityClientCredentials() {
        return clientConfiguration.getIdentityComponentCredentials();
    }

    @Override
    public HostConfiguration serviceRegistry() {
        return clientConfiguration.getRegistryHostConfiguration();
    }

}