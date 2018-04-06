package com.piksel.sequoia.clientsdk.client;

import java.util.LinkedList;

import lombok.extern.slf4j.Slf4j;

/**
 * Holds a queue of mocked responses that can be populated and servied by the
 * mocked transport layer.
 */
@Slf4j
public class QueingMockTransportResponseProvider
        implements MockTransportResponseProvider {

    private static LinkedList<MockResponse> responseChain = new LinkedList<>();

    @Override
    public void enqueueResponse(MockResponse mockResponse) {
        responseChain.add(mockResponse);
    }

    @Override
    public MockResponse provideStringResponse() {
        MockResponse response = responseChain.remove();
        log.debug("Providing response [{}]", response);
        return response;
    }

    @Override
    public void clearResponses() {
        responseChain.clear();
    }

    @Override
    public boolean hasAllResponsesBeenDelivered() {
        return responseChain.isEmpty();
    }

}