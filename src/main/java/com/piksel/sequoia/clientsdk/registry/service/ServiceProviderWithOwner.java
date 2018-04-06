package com.piksel.sequoia.clientsdk.registry.service;

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
