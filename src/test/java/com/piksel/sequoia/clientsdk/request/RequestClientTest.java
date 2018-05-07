package com.piksel.sequoia.clientsdk.request;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.piksel.sequoia.clientsdk.RequestExecutionException;

public class RequestClientTest {
    @Mock
    DefaultRequestClient requestClient;

    @Before
    public void initMocks() throws IOException {
        MockitoAnnotations.initMocks(this);

        doNothing().when(requestClient).initializeRequest(any(HttpRequest.class));
        when(requestClient.executeRequest(any(HttpRequest.class))).thenCallRealMethod();
    }

    @Test
    public void executeRequest_throwSocketTimeoutException_RequestExecutionExceptionWithSocketTimeoutExceptionInCause()
            throws IOException {
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        throw new SocketTimeoutException();
                    }
                };
            }
        };
        HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
        try {
            requestClient.executeRequest(request);
        } catch (RequestExecutionException reEx) {
            assertThat(reEx.getCause().getClass(), is(equalTo(SocketTimeoutException.class)));
        }
    }

    @Test
    public void executeRequest_throwHttpResponseExceptionWith500_RequestExecutionExceptionWith500() throws IOException {
        HttpResponseException httpResponseException =
                (new HttpResponseException.Builder(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, "", new HttpHeaders()))
                        .build();

        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        throw httpResponseException;
                    }
                };
            }
        };

        HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
        try {
            requestClient.executeRequest(request);
        } catch (RequestExecutionException reEx) {
            assertThat(reEx.getStatusCode(), is(equalTo(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)));
        }
    }
}
