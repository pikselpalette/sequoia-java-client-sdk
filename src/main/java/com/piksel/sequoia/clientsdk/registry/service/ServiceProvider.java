package com.piksel.sequoia.clientsdk.registry.service;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.endpoint.Endpoint;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;
import com.piksel.sequoia.clientsdk.validation.Validatable;

/**
 * Provides access to Sequoia Services either as a <em>resourcefulEndpoint</em>
 * or as a plain <em>endpoint</em>. Note that services where a dynamic tenant is
 * used require the <code>owner</code> to be supplied in all requests. As a
 * convenience, the {@link #resourcefulEndpoint(String, String, Class)} allows
 * this to be supplied.
 */
@PublicEvolving
public interface ServiceProvider {

    /**
     * Provide one {@link ResourcefulEndpoint} given by the endpoint and the
     * resourceClass.
     * 
     * @param endpoint
     *            The endpoint name within the service
     * @param resourceClass
     *            The resource type used for serialisation and deserialisation
     */
    <T extends Resource> ResourcefulEndpoint<T> resourcefulEndpoint(
            String endpoint, Class<T> resourceClass);

    /**
     * Provide one {@link ResourcefulEndpoint} given by the owner, the endpoint
     * and the resourceClass.
     * 
     * @param endpoint
     *            The endpoint name within the service
     * @param owner
     *            The owner to use in all requests to this endpoint
     * @param resourceClass
     *            The resource type used for serialisation and deserialisation
     */
    <T extends Resource> ResourcefulEndpoint<T> resourcefulEndpoint(
            String owner, String endpoint, Class<T> resourceClass);

    /**
     * Provide one {@link Endpoint} given the endpoint and the class. The
     * request and response types must be provided to allow the client to
     * perform serialization and deserialization.
     * 
     * @param endpoint
     *            The endpoint name within the service
     * @param responseClass
     *            The resource type used for serialisation and deserialisation
     */
    <T, K extends Validatable> Endpoint<T, K> endpoint(String endpoint,
            Class<T> responseClass);

}
