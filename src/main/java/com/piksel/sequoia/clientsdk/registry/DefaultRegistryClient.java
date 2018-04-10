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

import javax.inject.Inject;

import com.google.api.client.http.GenericUrl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.PreconfiguredHostRegistry;
import com.piksel.sequoia.clientsdk.Response;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.request.RequestClient;
import com.piksel.sequoia.clientsdk.resource.LazyLoadingResourceIterable;
import com.piksel.sequoia.clientsdk.resource.ResourceEndpointHandler;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;

@Internal
@Slf4j
public final class DefaultRegistryClient implements RegistryClient {

    private static final String DATA_SERVICES = "/services";
    private static final String SERVICES = "services";

    private final RequestClient requestClient;
    private final PreconfiguredHostRegistry registry;
    private final RegistryClientConfiguration configuration;
    private final Gson gson;

    @Inject
    public DefaultRegistryClient(RequestClient requestClient, PreconfiguredHostRegistry registry, RegistryClientConfiguration configuration,
            Gson gson) {
        this.requestClient = requestClient;
        this.registry = registry;
        this.configuration = configuration;
        this.gson = gson;
    }

    public RegisteredService getServiceRegistry(String serviceName) {
        log.debug("Getting service with service name [{}]", serviceName);
        return getRegisteredService(serviceName, buildUrl().build());
    }

    @Override
    public ResourceIterable<RegisteredService> getServiceRegistryList() {
        log.debug("Getting all service registrations");
        return getResourceIterableRegisteredServices(buildUrl());
    }

    @Override
    public ResourceIterable<RegisteredService> getServiceRegistryListForOwner(String owner) {
        log.debug("Getting all service registrations for owner {}", owner);
        return getResourceIterableRegisteredServices(getRegistryBaseUrl(owner));
    }

    private GenericUrl buildUrl() {
        return getRegistryBaseUrl(configuration.getRegistryServiceOwner());
    }
    
    private GenericUrl buildUrl(String serviceName) {
        return getRegistryBaseUrl(serviceName, configuration.getRegistryServiceOwner());
    }

    private GenericUrl getRegistryBaseUrl(String owner) {
        log.debug("Getting host configuration");
        HostConfiguration registryHostConfiguration = registry.serviceRegistry();
        log.debug("Got host configuration [{}]", registryHostConfiguration);
        return new GenericUrl(registryHostConfiguration.getUrl() + String.join("/", DATA_SERVICES, owner));
    }
    
    private GenericUrl getRegistryBaseUrl(String serviceName, String owner) {
        log.debug("Getting host configuration");
        HostConfiguration registryHostConfiguration = registry.serviceRegistry();
        log.debug("Got host configuration [{}]", registryHostConfiguration);
        return new GenericUrl(
                registryHostConfiguration.getUrl() + String.join("/", DATA_SERVICES, owner, serviceName));
    }

    private ResourceIterable<RegisteredService> getResourceIterableRegisteredServices(GenericUrl genericUrl) {
        Response<JsonElement> response = requestClient.executeGetRequest(genericUrl);
        ResourceEndpointHandler<RegisteredService> endpoint = new ResourceEndpointHandler<>(requestClient, SERVICES,
            genericUrl.build(), RegisteredService.class, gson);
        return new LazyLoadingResourceIterable<>(response.getPayload().get(), endpoint, gson);
    }

    @SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
    private RegisteredService getRegisteredService(String serviceName, String genericUrl) {
        Response<JsonElement> response = requestClient.executeGetRequest(buildUrl(serviceName));
        ResourceEndpointHandler<RegisteredService> endpoint = new ResourceEndpointHandler<>(requestClient, SERVICES,
            genericUrl, RegisteredService.class, gson);
        return new LazyLoadingResourceIterable<>(response.getPayload().get(), endpoint, gson).single();
    }

}
