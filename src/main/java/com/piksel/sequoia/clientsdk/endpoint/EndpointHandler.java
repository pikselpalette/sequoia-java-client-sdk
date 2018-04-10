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

import java.util.Optional;

import com.google.api.client.http.GenericUrl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.piksel.sequoia.clientsdk.DefaultResponse;
import com.piksel.sequoia.clientsdk.DefaultResponse.DefaultResponseBuilder;
import com.piksel.sequoia.clientsdk.Response;
import com.piksel.sequoia.clientsdk.endpoint.headers.RequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.params.QueryParams;
import com.piksel.sequoia.clientsdk.request.RequestClient;
import com.piksel.sequoia.clientsdk.validation.Validatable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EndpointHandler<T, K>
        implements Endpoint<T, K> {

    private final RequestClient requestClient;
    private final GenericUrl endpointUrl;
    private final Class<T> payloadClass;
    private final Gson gson;

    @Override
    public Response<T> get() {
        GenericUrl url = endpointUrl.clone();
        return toResponse(requestClient.executeGetRequest(url));
    }

    @Override
    public Response<T> get(QueryParams params) {
        GenericUrl url = endpointUrl.clone();
        return toResponse(requestClient.executeGetRequest(
                new UrlApplier().applyQueryParams(url, params)));
    }

    @Override
    public Response<T> get(RequestHeaders headers) {
        GenericUrl url = endpointUrl.clone();
        return toResponse(
                requestClient.executeGetRequest(url, headers.getHeaders()));
    }

    @Override
    public Response<T> get(QueryParams params, RequestHeaders headers) {
        GenericUrl url = endpointUrl.clone();
        return toResponse(requestClient.executeGetRequest(
                new UrlApplier().applyQueryParams(url, params),
                headers.getHeaders()));
    }

    @Override
    public Response<T> post(K payload) {
        validate(payload);
        GenericUrl url = endpointUrl.clone();
        return toResponse(
                requestClient.executePostRequest(url, payload, payloadClass));
    }
    
    @Override
    public Response<T> post(K payload, RequestHeaders headers) {
        validate(payload);
        GenericUrl url = endpointUrl.clone();
        return toResponse(requestClient.executePostRequest(url, payload,
                payloadClass, headers.getHeaders()));
    }
    
    private void validate(K payload) {
        if (payload instanceof Validatable) {
            Validatable validatablePayload = (Validatable) payload;
            if (!validatablePayload.isValid().isEmpty()) {
                throw PayloadValidationException.thrown(validatablePayload.isValid());
            }
        }
    }

    private Response<T> toResponse(Response<JsonElement> jsonResponse) {
        DefaultResponseBuilder<T> builder = DefaultResponse.<T>builder()
                .statusCode(jsonResponse.getStatusCode())
                .successStatusCode(jsonResponse.isSuccessStatusCode());
        if (jsonResponse.getPayload().isPresent()
                && !jsonResponse.getPayload().get().isJsonNull()) {
            builder.payload(Optional.ofNullable(gson
                    .fromJson(jsonResponse.getPayload().get(), payloadClass)));
        } else {
            builder.payload(Optional.empty());
        }
        return builder.build();
    }

}
