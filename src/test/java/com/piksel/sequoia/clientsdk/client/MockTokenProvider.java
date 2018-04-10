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

import static java.time.OffsetDateTime.now;
import static java.time.Period.ofYears;

import java.io.IOException;

import javax.inject.Inject;

import com.google.api.client.auth.oauth2.TokenResponse;

/**
 * Provides mocked token responses for test purposes.
 */
public class MockTokenProvider {

    @Inject
    MockTransportResponseProvider mockTransportResponseProvider;

    public void provideSuccessToken() {
        mockTransportResponseProvider
                .addResponseFromString(successTokenResponse(), 200);
    }

    public static String successTokenResponse() {
        try {
            return new TokenResponse().setAccessToken("success-token")
                    .setExpiresInSeconds(theDistantFuture()).toPrettyString();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    private static long theDistantFuture() {
        return now().plus(ofYears(100)).toEpochSecond();
    }

}
