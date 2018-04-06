package com.piksel.sequoia.clientsdk;

import javax.inject.Inject;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

class DataServicesRequestInitializer implements HttpRequestInitializer {

    private final DataServicesRequestConfigurator dataServicesRequestConfigurator;

    @Inject
    public DataServicesRequestInitializer(
            DataServicesRequestConfigurator dataServicesRequestConfigurator) {
        this.dataServicesRequestConfigurator = dataServicesRequestConfigurator;
    }

    @Override
    public void initialize(HttpRequest request) {
        dataServicesRequestConfigurator.configure(request);
    }

}