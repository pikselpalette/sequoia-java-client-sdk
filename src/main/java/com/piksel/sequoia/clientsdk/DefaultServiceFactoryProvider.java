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

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.service.DefaultServiceProvider;
import com.piksel.sequoia.clientsdk.request.RequestClient;

@Internal
public class DefaultServiceFactoryProvider implements ServiceFactoryProvider {

    protected final RequestClient requestClient;
    protected final Gson gson;

    @Inject
    public DefaultServiceFactoryProvider(RequestClient requestClient, Gson gson) {
        this.requestClient = requestClient;
        this.gson = gson;
    }

    @Override
    public ServiceFactory from(RegisteredService service) {
        checkNotNull(service, "Registered service must not be null");
        return s -> new DefaultServiceProvider(requestClient, service, gson);
    }

}
