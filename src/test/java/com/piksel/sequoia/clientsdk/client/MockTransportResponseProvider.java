package com.piksel.sequoia.clientsdk.client;

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