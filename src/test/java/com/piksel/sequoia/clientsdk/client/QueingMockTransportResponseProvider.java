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
