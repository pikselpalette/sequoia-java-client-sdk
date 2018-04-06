package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;

/**
 * Provider for the creation of {@link ServiceFactory} instances with
 * an associated owner.
 *
 * <p>This extension point allows for pluggable strategies for the
 * creation of service factories. For example, client code wishing
 * to dynamically select different endpoint creation code based upon
 * the service name, could do so by utilising this extension point.
 *
 * @see ServiceFactory
 *
 * @since 1.2.0
 */
@PublicEvolving
public interface ServiceFactoryProviderWithOwner {

    /**
     * Create a new {@link ServiceFactory} from the provided service
     * registration and owner.
     *
     * @param service the registered service from which to create the factory
     * @param owner the fixed owner for the registered service.
     * @return a service factory for creating services
     */
    ServiceForOwnerFactory from(RegisteredService service, String owner);

}
