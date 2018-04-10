package com.piksel.sequoia.clientsdk;

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
