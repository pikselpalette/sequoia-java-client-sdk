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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
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
public interface DataServicesCredentialProvider {

    /**
     * Get a {@link Credential} for the given client context. This method does
     * perform any caching of the acquired {@link TokenResponse}.
     * 
     * @return the acquired token response
     */
    Credential getCredential();

    /**
     * Get a {@link Credential} for the given client context and owner. This method does
     * perform any caching of the acquired {@link TokenResponse}.
     *
     * @param owner the owner for which retrieve the identity url from registry
     * @return the acquired token response
     */
    Credential getCredential(String owner);

    /**
     * Called when the component is shutting down to revoke its token.
     */
    void revokeToken();

    /**
     * Called when the component is shutting down to revoke its token.
     */
    void revokeToken(String owner);

    /**
     * Returns false if the {@link #revokeToken()} method has been called. This
     * prevents other components from refreshing the token inadvertently during
     * the shutdown phase.
     */
    boolean isAllowedToRefreshToken();
}
