package com.piksel.sequoia.clientsdk.registry.service;

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
