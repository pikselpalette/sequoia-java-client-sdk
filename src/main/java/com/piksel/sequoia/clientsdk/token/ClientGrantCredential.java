package com.piksel.sequoia.clientsdk.token;

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

import java.io.IOException;

import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.piksel.sequoia.annotations.Internal;

/**
 * Provides a data services {@link Credential} using only its client credentials
 * as specified in <a href="http://tools.ietf.org/html/rfc6749#section-4.4">
 * Client Credentials Grant</a>.
 * 
 * <p>This provider does not perform any caching of access tokens and supplies a
 * {@link TokenResponse} to be used in conjunction with the <a href=
 * "https://developers.google.com/api-client-library/java/google-oauth-java-client/">
 * Google OAuth 2.0 and 1.0 Java Library</a>.
 * 
 * @see TokenResponse
 */
@Internal
public class ClientGrantCredential extends Credential {
    private ClientGrantCredentialUnsuccessfulResponseHandler unsuccessfulResponseHandler;

    private boolean allowRefreshToken = true;

    public void disableRefreshToken() {
        allowRefreshToken = false;
    }

    @lombok.Builder
    protected ClientGrantCredential(Builder credentialBuilder,
            ClientGrantCredentialUnsuccessfulResponseHandler unsuccessfulResponseHandler) {
        super(credentialBuilder);
        this.unsuccessfulResponseHandler = unsuccessfulResponseHandler;
    }

    @Override
    public boolean handleResponse(HttpRequest request, HttpResponse response,
            boolean supportsRetry) {
        return unsuccessfulResponseHandler != null
                && unsuccessfulResponseHandler.handleUnsuccessful(this, request,
                        response, supportsRetry);
    }

    @Override
    protected TokenResponse executeRefreshToken() throws IOException {
        if (!allowRefreshToken) {
            throw new TokenRefreshException(
                    "Token has been revoked, is not possible to acquire more tokens");
        }

        return new ClientCredentialsTokenRequest(getTransport(),
                getJsonFactory(), new GenericUrl(getTokenServerEncodedUrl()))
                        .setClientAuthentication(getClientAuthentication())
                        .setRequestInitializer(getRequestInitializer())
                        .execute();
    }
}
