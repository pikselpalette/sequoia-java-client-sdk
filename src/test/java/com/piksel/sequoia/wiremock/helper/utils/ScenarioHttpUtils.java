package com.piksel.sequoia.wiremock.helper.utils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.time.OffsetDateTime.now;

import java.io.IOException;
import java.nio.charset.Charset;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.util.Base64;
import com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings;

/**
 * Utilities to be used by {@link ScenarioHttpMappings}
 */
public class ScenarioHttpUtils {

    public static String encodedUserAuth(String user, String password) {
        return Base64.encodeBase64String((user + ":" + password).getBytes(Charset.defaultCharset()));
    }

    /**
     * Create a success response token response
     * 
     * @param token
     *            to add to the response.
     * @return A response with successful authentication.
     */
    public static ResponseDefinitionBuilder createSuccessAuth(String token) {
        try {
            return aResponse().withStatus(200)
                    .withBody(new TokenResponse().setAccessToken(token).setExpiresInSeconds(now().plusYears(100).toEpochSecond()).toPrettyString());
        } catch (IOException e) {
            throw new PrettyStringException(e);
        }
    }

    public static String serviceUrl(int port) {
        return "http://127.0.0.1:" + port;
    }

    static class PrettyStringException extends RuntimeException {

        private static final long serialVersionUID = 7396646916447118512L;

        PrettyStringException(IOException e) {
            super(e);
        }

    }

}
