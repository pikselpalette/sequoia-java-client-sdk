package com.piksel.sequoia.clientsdk.registry;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;

/**
 * A client for interacting with registry services.
 */
@PublicEvolving
public interface RegistryClient {

    /**
     * Return a single {@link RegisteredService} with the serviceName as name.
     */
    RegisteredService getServiceRegistry(String serviceName);

    /**
     * Return a {@link ResourceIterable} with all the services.
     */
    ResourceIterable<RegisteredService> getServiceRegistryList();

    /**
     * Return a {@link ResourceIterable} with all the services for an owner.
     */
    ResourceIterable<RegisteredService> getServiceRegistryListForOwner(String owner);

}