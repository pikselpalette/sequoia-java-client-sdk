package com.piksel.sequoia.clientsdk.token;

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
     * Called when the component is shutting down to revoke its token.
     */
    void revokeToken();

    /**
     * Returns false if the {@link #revokeToken()} method has been called. This
     * prevents other components from refreshing the token inadvertently during
     * the shutdown phase.
     */
    boolean isAllowedToRefreshToken();
}