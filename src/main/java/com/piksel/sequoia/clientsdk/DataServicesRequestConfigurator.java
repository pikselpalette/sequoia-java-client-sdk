package com.piksel.sequoia.clientsdk;

import com.google.api.client.http.HttpRequest;

interface DataServicesRequestConfigurator {

    void configure(HttpRequest request);

}