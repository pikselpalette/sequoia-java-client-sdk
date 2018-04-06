package com.piksel.sequoia.clientsdk;

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