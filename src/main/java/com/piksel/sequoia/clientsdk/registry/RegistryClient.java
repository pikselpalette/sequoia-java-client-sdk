package com.piksel.sequoia.clientsdk.registry;

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
