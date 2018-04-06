package com.piksel.sequoia.clientsdk.token;

import static com.google.api.client.auth.oauth2.BearerToken.authorizationHeaderAccessMethod;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.inject.Inject;

import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.MessageConfiguration;
import com.piksel.sequoia.clientsdk.PreconfiguredHostRegistry;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.recovery.RequestRecoveryStrategyProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * An {@link DataServicesCredentialProvider} that communicates with the data
 * services over HTTP. This provider uses the Google OAuth Client Library which
 * provides out-of-the-box support for interacting with OAuth services.
 * 
 * @see <a href="https://github.com/google/google-oauth-java-client"> https://
 *      github.com/google/google-oauth-java-client</a>
 */
@Slf4j
@Internal
public class HttpClientAccessCredentialProvider
        implements DataServicesCredentialProvider {

    private static final String OAUTH_TOKEN_URL_PATH = "/oauth/token";
    private static final String OAUTH_REVOKE_URL_PATH = "/oauth/revoke";

    private final PreconfiguredHostRegistry hostRegistry;
    private final RequestRecoveryStrategyProvider requestRecoveryStrategyProvider;
    private final MessageConfiguration messageConfiguration;
    private final ClientGrantCredentialUnsuccessfulResponseHandler unsuccessfulResponseHandler;

    private ClientGrantCredential credential;
    private boolean allowCredentialRefresh = true;

    @Inject
    public HttpClientAccessCredentialProvider(
            PreconfiguredHostRegistry hostRegistry,
            RequestRecoveryStrategyProvider requestRecoveryStrategyProvider,
            MessageConfiguration transportConfiguration,
            ClientGrantCredentialUnsuccessfulResponseHandler unsuccessfulResponseHandler) {
        this.hostRegistry = hostRegistry;
        this.requestRecoveryStrategyProvider = requestRecoveryStrategyProvider;
        this.messageConfiguration = transportConfiguration;
        this.unsuccessfulResponseHandler = unsuccessfulResponseHandler;
        log.debug("HttpClientAccessCredentialProvider constructed");
    }

    @Override
    public Credential getCredential() {
        if (credential == null && allowCredentialRefresh) {
            log.debug("No client credential stored - requesting a new one");
            TokenResponse response = executeTokenRequest();

            credential = ClientGrantCredential.builder()
                    .unsuccessfulResponseHandler(unsuccessfulResponseHandler)
                    .credentialBuilder(new ClientGrantCredential.Builder(
                            authorizationHeaderAccessMethod())
                                    .setTransport(
                                            messageConfiguration.getTransport())
                                    .setJsonFactory(messageConfiguration
                                            .getJsonFactory())
                                    .setTokenServerUrl(
                                            buildIdentityAccessTokenUrl())
                                    .setClientAuthentication(
                                            createBasicAuthHeader()))
                    .build();
            log.debug("Built client credential [{}]", credential);
            credential.setFromTokenResponse(response);
        }
        return credential;
    }

    @Override
    public void revokeToken() {
        if (credential != null) {
            executeRevokeRequest();
            credential.disableRefreshToken();
        }
        allowCredentialRefresh = false;
    }

    @Override
    public boolean isAllowedToRefreshToken() {
        return allowCredentialRefresh;
    }

    private TokenResponse executeTokenRequest() {
        log.debug("Executing a token request");
        GenericUrl tokenServerUrl = buildIdentityAccessTokenUrl();
        JsonFactory jsonFactory = messageConfiguration.getJsonFactory();
        HttpTransport transport = messageConfiguration.getTransport();
        return callDataServices(tokenServerUrl, jsonFactory, transport);
    }

    private void executeRevokeRequest() {
        try {
            log.debug("Executing a revoke request");
            aRequestFactory().buildPostRequest(buildIdentityRevokeTokenUrl(),
                    buildRevokeContent()).execute();
        } catch (IOException e) {
            throw anUnexpectedTokenRevokeException(e);
        }
    }

    private TokenResponse callDataServices(GenericUrl tokenServerUrl,
            JsonFactory jsonFactory, HttpTransport transport) {
        log.debug("Calling oauth services with url [{}]", tokenServerUrl);
        try {
            return new ClientCredentialsTokenRequest(transport, jsonFactory,
                    tokenServerUrl)
                            .setClientAuthentication(createBasicAuthHeader())
                            .setRequestInitializer(requestInitializer())
                            .execute();
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                throw new AccessTokenAcquisitionException(e.getDetails());
            } else {
                throw new AccessTokenAcquisitionException(e);
            }
        } catch (IOException e) {
            throw new AccessTokenAcquisitionException(e);
        }
    }

    private HttpRequestInitializer requestInitializer() {
        return request -> {
            request.setUnsuccessfulResponseHandler(backOffHandler());
            request.setNumberOfRetries(numOfRetries());
        };
    }

    private Integer numOfRetries() {
        return requestRecoveryStrategyProvider.getRecoveryStrategy()
                .getNumberOfRetries();
    }

    private BasicAuthentication createBasicAuthHeader() {
        ComponentCredentials identityComponentCredentials = hostRegistry
                .identityClientCredentials();
        return new BasicAuthentication(
                identityComponentCredentials.getUsername(),
                identityComponentCredentials.getPassword());
    }

    private HttpUnsuccessfulResponseHandler backOffHandler() {
        return new HttpBackOffUnsuccessfulResponseHandler(
                requestRecoveryStrategyProvider.getRecoveryStrategy()
                        .getBackOff());
    }

    private GenericUrl buildIdentityAccessTokenUrl() {
        return resolveHostUrlWithPath(OAUTH_TOKEN_URL_PATH);
    }

    private GenericUrl buildIdentityRevokeTokenUrl() {
        return resolveHostUrlWithPath(OAUTH_REVOKE_URL_PATH);
    }

    private GenericUrl resolveHostUrlWithPath(String path) {
        HostConfiguration hostConfiguration = hostRegistry.identity();
        URL resolvedUri = hostConfiguration.getBaseUrl();
        GenericUrl genericUrl = new GenericUrl(resolvedUri);
        genericUrl.appendRawPath(OAUTH_TOKEN_URL_PATH);
        return genericUrl;
    }

    private HttpRequestFactory aRequestFactory() {
        return aRequestFactory(withTransportConfiguration(messageConfiguration),
                credential);
    }

    private HttpRequestFactory aRequestFactory(
            MessageConfiguration transportConfiguration,
            HttpRequestInitializer initializer) {
        return transportConfiguration.getTransport()
                .createRequestFactory(initializer);
    }

    private MessageConfiguration withTransportConfiguration(
            MessageConfiguration transportConfiguration) {
        return transportConfiguration;
    }

    private HttpContent buildRevokeContent() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("token", credential.getAccessToken());
        body.put("token_type_hint", "access_token");
        return new UrlEncodedContent(body);
    }

    private TokenRevokeException anUnexpectedTokenRevokeException(Exception e) {
        return new TokenRevokeException(
                "An unexpected error occurs when trying to revoke the token",
                e);
    }

}
