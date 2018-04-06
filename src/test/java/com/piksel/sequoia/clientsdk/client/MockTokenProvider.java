package com.piksel.sequoia.clientsdk.client;

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
