package com.piksel.sequoia.clientsdk.resource;

import java.util.Collection;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.RequestExecutionException;
import com.piksel.sequoia.clientsdk.ResourceResponse;

/**
 * Set of methods to interact with the resourceful endpoint.
 */
@PublicEvolving
public interface ResourcefulEndpoint<T extends Resource> {

    /**
     * Retrieves one or more resources given their reference and returns the
     * {@link ResourceResponse response} retrieved.
     * 
     * @throws RequestExecutionException
     *             if there is an error during the process
     */
    ResourceResponse<T> read(Reference ref);

    /**
     * Retrieves a batch of resources given their references and returns the
     * response {@link ResourceResponse} retrieved.
     * 
     * @throws RequestExecutionException
     *             if there is an error during the process
     */
    ResourceResponse<T> read(Collection<Reference> ref);

    /**
     * Retrieves the list of resources that matches with the criteria and
     * returns the {@link ResourceResponse response} retrieved.
     * 
     * @throws RequestExecutionException
     *             if there is an error during the process
     */
    ResourceResponse<T> browse(ResourceCriteria criteria);

    /**
     * Creates one resource and returns the {@link ResourceResponse response}
     * retrieved.
     * 
     * <p>This operation will replace or create the resource, without checking if
     * there is a version conflict with an already stored resource.
     * 
     * @throws ResourceValidationException
     *             if the resource to be created is not valid.
     */
    ResourceResponse<T> store(T resource);

    /**
     * Creates a batch of resources and returns the {@link ResourceResponse
     * response} retrieved.
     * 
     * <p>This operation will replace or create the resources, without checking if
     * there is a version conflict with an already stored resource.
     * 
     * @throws ResourceValidationException
     *             if the resource's batch to be created is not valid.
     */
    ResourceResponse<T> store(Collection<T> resources);

    /**
     * Updates one resource given its reference and returns the response
     * {@link ResourceResponse} retrieved.
     * 
     * <p>This operation requires that the resource contains the version
     * number of the resource that it expects to update. If no
     * <code>version</code> is provided, this method will throw a
     * {@link ResourceValidationException}.
     * 
     * <p>If the resource does not exist or exists with a different version number,
     * this method will throw a {@link RequestExecutionException}.
     * 
     * @throws ResourceValidationException
     *             if the resource to be updated is not valid.
     * @throws RequestExecutionException
     *             if the resource has a version that conflicts with the stored
     *             resource
     */
    ResourceResponse<T> update(T resource, Reference reference);

    /**
     * Deletes one resource given its references and returns the
     * {@link ResourceResponse response} retrieved.
     * 
     * @throws RequestExecutionException
     *             if there is an error during the process
     */
    ResourceResponse<T> delete(Reference reference);

    /**
     * Deletes a batch of resources and returns the {@link ResourceResponse
     * response} retrieved.
     * 
     * @throws RequestExecutionException
     *             if there is an error during the process.
     */
    ResourceResponse<T> delete(Collection<Reference> references);

    /**
     * Provides the class type for the resource endpoint.
     */
    Class<T> getEndpointType();

    /**
     * Returns the string name of the resources that this endpoint accesses.
     */
    String getResourceKey();

}