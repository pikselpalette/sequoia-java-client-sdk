package com.piksel.sequoia.clientsdk;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.Gson;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.service.DefaultServiceProvider;
import com.piksel.sequoia.clientsdk.registry.service.ServiceProviderWithOwner;
import com.piksel.sequoia.clientsdk.request.RequestClient;
import javax.inject.Inject;

@Internal
public class DefaultServiceFactoryProviderWithOwner extends DefaultServiceProvider implements ServiceFactoryProviderWithOwner {

    @Inject
    public DefaultServiceFactoryProviderWithOwner(RequestClient requestClient, RegisteredService service, Gson gson) {
        super(requestClient, service, gson);
    }

    @Override
    public ServiceForOwnerFactory from(RegisteredService service, String owner) {
        checkNotNull(service, "Registered service must not be null");
        return (s, o) -> new ServiceProviderWithOwner(requestClient, service, gson, owner);
    }
}
