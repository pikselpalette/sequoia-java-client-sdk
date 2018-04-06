package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.registry.service.ServiceProvider;

/**
 * Provides client access to registered services with a given name.
 * 
 * <p>The service factory is driven from a collection of registered services that
 * are retrieved from the Sequoia service registry. Only services that the
 * client has access to can be created by this factory.
 */
@PublicEvolving
public interface ServiceFactory {

    /**
     * Get a service by name.
     * 
     * @param serviceName
     *            the name of the service to return
     * @return the service provider giving access to service endpoints
     * @throws NoSuchServiceException
     *             if the service requested is not available for the the client
     *             credentials or the service does not otherwise exist.
     */
    ServiceProvider service(String serviceName);

}
