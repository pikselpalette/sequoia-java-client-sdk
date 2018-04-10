package com.piksel.sequoia.clientsdk.endpoint;

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
import com.piksel.sequoia.clientsdk.Response;
import com.piksel.sequoia.clientsdk.endpoint.headers.RequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.params.QueryParams;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;

/**
 * The {@link Endpoint} interface provides methods for interacting with a plain
 * HTTP endpoint, as opposed to a {@link ResourcefulEndpoint}. Both
 * {@link RequestHeaders} and {@link QueryParams} can be set on the request
 * directly.
 *
 * @param <T>
 *            for Response Type
 * @param <K>
 *            for Request Type
 */
@PublicEvolving
public interface Endpoint<T, K> {

    /**
     * Performs a GET over the endpoint and returns the {@link Response} holding
     * the information returned by the endpoint.
     */
    Response<T> get();

    /**
     * Performs a GET over the endpoint with {@link RequestHeaders} and returns
     * the {@link Response} holding the information returned by the endpoint.
     */
    Response<T> get(RequestHeaders headers);

    /**
     * Performs a GET using {@link QueryParams} over the endpoint and returns
     * the {@link Response} holding the information returned by the endpoint.
     */
    Response<T> get(QueryParams params);

    /**
     * Performs a GET using {@link QueryParams} and {@link RequestHeaders} over
     * the endpoint and returns the {@link Response} holding the information
     * returned by the endpoint.
     */
    Response<T> get(QueryParams params, RequestHeaders headers);

    /**
     * Performs a POST with the payload and returns the {@link Response} holding
     * the information returned by the endpoint.
     */
    Response<T> post(K payload);

    /**
     * Performs a POST with the payload and using {@link RequestHeaders}
     * returning the {@link Response} holding the information returned by the
     * endpoint.
     */
    Response<T> post(K payload, RequestHeaders headers);

}
