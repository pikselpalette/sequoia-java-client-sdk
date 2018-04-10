package com.piksel.sequoia.clientsdk.client;

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

import java.io.InputStream;
import java.util.Scanner;

import lombok.Value;

/**
 * Enqueues and provides mock responses for use in tests.
 */
public interface MockTransportResponseProvider {

    void enqueueResponse(MockResponse mockResponse);

    MockResponse provideStringResponse();

    void clearResponses();

    boolean hasAllResponsesBeenDelivered();

    default void addResponseFromString(String body, int status) {
        enqueueResponse(MockResponse.of(body, status));
    }

    default void addResponseFromFile(String filename, int status) {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(filename);
        try (Scanner s = new Scanner(stream)) {
            String body = s.useDelimiter("\\A").hasNext() ? s.next() : "";
            addResponseFromString(body, status);
        }
    }

    @Value(staticConstructor = "of")
    final class MockResponse {
        public String body;
        public int status;
    }

}
