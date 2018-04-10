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
import com.piksel.sequoia.clientsdk.registry.RegisteredService;

/**
 * Provider for the creation of {@link ServiceFactory} instances. 
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
public interface ServiceFactoryProvider {

    /**
     * Create a new {@link ServiceFactory} from the provided service 
     * registration.  
     * 
     * @param service the registered service from which to create the factory 
     * @return a service factory for creating services
     */
    ServiceFactory from(RegisteredService service);

}
