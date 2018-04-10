package com.piksel.sequoia.clientsdk;

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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

public class RequestExecutionExceptionTest {
    private static final String X_CORRELATION_ID_HEADER = "x-correlation-id";
    private static final String X_CORRELATION_ID_HEADER_VALUE = "123";

    private static HttpHeaders httpHeadersWithCorrelation = new HttpHeaders();
    static {
        httpHeadersWithCorrelation.set(X_CORRELATION_ID_HEADER, X_CORRELATION_ID_HEADER_VALUE);
    }

    private HttpRequest request;

    private HttpResponseException httpResponseException =
            (new HttpResponseException.Builder(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, "", httpHeadersWithCorrelation))
                    .build();

    private HttpTransport transport = new MockHttpTransport() {
        @Override
        public LowLevelHttpRequest buildRequest(String method, String url) {
            return new MockLowLevelHttpRequest() {
                @Override
                public LowLevelHttpResponse execute() {
                    MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                    response.setStatusCode(200);
                    response.setContent("OK");
                    return response;
                }
            };
        }
    };

    @Before
    public void setUp() throws IOException {
        request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
    }

    @Test
    public void constructorWithHttpRequestAndException(){
        RequestExecutionException requestExecutionException = new RequestExecutionException(request, httpResponseException);

        assertTrue(requestExecutionException.getRequest().isPresent());
        assertThat(requestExecutionException.getRequest().get(), is(request));
        assertThat(requestExecutionException.getStatusCode(), is(equalTo(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)));
        assertThat(requestExecutionException.getCause().getClass(), is(equalTo(HttpResponseException.class)));
        assertThat(requestExecutionException.getCorrelationId(), is(equalTo(X_CORRELATION_ID_HEADER_VALUE)));
    }

    @Test
    public void constructorWithIOException_exceptionIsRequestExecutionException(){
        RequestExecutionException requestExecutionException = new RequestExecutionException(httpResponseException);

        assertFalse(requestExecutionException.getRequest().isPresent());
        assertThat(requestExecutionException.getStatusCode(), is(equalTo(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)));
        assertThat(requestExecutionException.getCause().getClass(), is(equalTo(HttpResponseException.class)));
        assertThat(requestExecutionException.getCorrelationId(), is(equalTo(X_CORRELATION_ID_HEADER_VALUE)));
    }

    @Test
    public void constructorWithIOException_exceptionIsIOException(){
        IOException ioException = new IOException();
        RequestExecutionException requestExecutionException = new RequestExecutionException(ioException);

        assertFalse(requestExecutionException.getRequest().isPresent());
        assertThat(requestExecutionException.getStatusCode(), is(equalTo(0)));
        assertThat(requestExecutionException.getCause().getClass(), is(equalTo(IOException.class)));
        assertThat(requestExecutionException.getCorrelationId(), is(nullValue()));
    }
}
