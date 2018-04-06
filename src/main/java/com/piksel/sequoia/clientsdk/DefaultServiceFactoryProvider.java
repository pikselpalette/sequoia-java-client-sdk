package com.piksel.sequoia.clientsdk;

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
