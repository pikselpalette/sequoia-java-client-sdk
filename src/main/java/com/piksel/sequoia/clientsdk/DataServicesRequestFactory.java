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

import javax.inject.Inject;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;

final class DataServicesRequestFactory implements RequestFactory {

    private final DataServicesRequestInitializer requestInitializer;
    private final MessageConfiguration transportConfiguration;

    @Inject
    public DataServicesRequestFactory(
            DataServicesRequestInitializer requestInitializer,
            MessageConfiguration transportConfiguration) {
        this.requestInitializer = requestInitializer;
        this.transportConfiguration = transportConfiguration;
    }

    @Override
    public HttpRequestFactory getRequestFactory() {
        return aRequestFactory(transportConfiguration, requestInitializer);
    }

    private HttpRequestFactory aRequestFactory(
            MessageConfiguration transportConfiguration,
            HttpRequestInitializer initializer) {
        return transportConfiguration.getTransport()
                .createRequestFactory(initializer);
    }

}
