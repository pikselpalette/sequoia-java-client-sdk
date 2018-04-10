package com.piksel.sequoia.clientsdk.registry.service;

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

import com.google.gson.Gson;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.request.RequestClient;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;

public class ServiceProviderWithOwner extends DefaultServiceProvider {

    private final String owner;

    public ServiceProviderWithOwner(RequestClient requestClient,
        RegisteredService service, Gson gson, String owner) {
        super(requestClient, service, gson);
        this.owner = owner;
    }

    @Override
    public <T extends Resource> ResourcefulEndpoint<T> resourcefulEndpoint(String endpoint,
        Class<T> resourceClass) {
        return super.resourcefulEndpoint(owner, endpoint, resourceClass);
    }
}
