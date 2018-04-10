package com.piksel.sequoia.clientsdk.configuration;

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
