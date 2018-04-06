package com.piksel.sequoia.clientsdk.token;

import static com.google.api.client.http.HttpStatusCodes.STATUS_CODE_OK;
import static com.google.api.client.http.HttpStatusCodes.STATUS_CODE_SERVER_ERROR;
import static com.google.api.client.http.HttpStatusCodes.STATUS_CODE_UNAUTHORIZED;
import static com.google.api.client.json.gson.GsonFactory.getDefaultInstance;
import static com.google.inject.Guice.createInjector;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.piksel.sequoia.clientsdk.client.DataServiceClientTestModule;
import com.piksel.sequoia.clientsdk.client.MockTransportResponseProvider;
import com.piksel.sequoia.clientsdk.client.QueingMockTransportResponseProvider;
import com.piksel.sequoia.clientsdk.request.RequestClient;

public class HttpClientAccessCredentialProviderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Inject
    DataServicesCredentialProvider accessTokenProvider;
    @Inject
    MockTransportResponseProvider mockResponseProvider;
    @Inject
    RequestClient requestClient;

    @Before
    public void setUpApplicationContext() {
        createInjector(new HttpClientAccessCredentialTestModule())
                .injectMembers(this);
    }

    @Test
    public void shouldAcquireAccessTokenOnHappyPath() {
        withAccessToken("SOME TOKEN");

        Credential credential = accessTokenProvider.getCredential();

        assertThat(credential.getAccessToken(), is("SOME TOKEN"));
        assertThat(credential.getRefreshToken(), is(nullValue()));
    }

    @Test
    public void shouldReportErrorOnFailureScenario() {
        thrown.expect(AccessTokenAcquisitionException.class);

        //10 retries by default.
        setUpRetries(10);

        accessTokenProvider.getCredential();
    }

    private void setUpRetries(int retries) {
        for(int i = 0; i <= retries ; i++){
            withAccessToken("SOME TOKEN", STATUS_CODE_SERVER_ERROR);            
        }
    }

    @Test
    public void shouldUseTheRefreshTheAccessTokenUsingCredsWhenUnauthorisedReturnedFromService() {
        withAccessToken("FIRST ACCESS TOKEN");
        whenDataServiceCalled(STATUS_CODE_UNAUTHORIZED);
        withAccessToken("SECOND ACCESS TOKEN");
        whenDataServiceCalled("OK RESPONSE", STATUS_CODE_OK);

        Credential credential = accessTokenProvider.getCredential();

        assertThat(credential.getAccessToken(), is("FIRST ACCESS TOKEN"));

        callDataService(); // unauth response code!

        assertThat(credential.getAccessToken(), is("SECOND ACCESS TOKEN"));
    }

    @Test
    public void shouldRevokeTheToken() {
        withAccessToken("FIRST ACCESS TOKEN");
        withAccessToken("REVOKED TOKEN");

        Credential credential = accessTokenProvider.getCredential();
        assertThat(credential.getAccessToken(), is("FIRST ACCESS TOKEN"));

        accessTokenProvider.revokeToken();
        assertThat(accessTokenProvider.isAllowedToRefreshToken(), is(false));
    }

    @Test
    public void shouldFailIfATokenIsRequestedAfterRevokingTheToken()
            throws Exception {
        thrown.expect(TokenRefreshException.class);
        withAccessToken("FIRST ACCESS TOKEN");
        withAccessToken("FIRST ACCESS TOKEN");

        Credential credential = accessTokenProvider.getCredential();
        assertThat(credential.getAccessToken(), is("FIRST ACCESS TOKEN"));

        accessTokenProvider.revokeToken();
        credential.refreshToken();
    }

    private TokenResponse aTokenResponseWithAccessTokenOf(String accessToken) {
        return successResponse(accessToken, 3600, "bearer", null);
    }

    private TokenResponse successResponse(String accessToken, long expiresIn,
            String tokenType, String refreshToken) {
        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setExpiresInSeconds(expiresIn);
        response.setTokenType(tokenType);
        response.setRefreshToken(refreshToken);
        return response;
    }

    private void withAnOauthEndpointResponseFromFile(String body, int status) {
        mockResponseProvider.addResponseFromString(body, status);
    }

    private void whenDataServiceCalled(String response, int statusCode) {
        TokenResponse token = aTokenResponseWithAccessTokenOf(response);
        try {
            withAnOauthEndpointResponseFromFile(
                    getDefaultInstance().toPrettyString(token), statusCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void whenDataServiceCalled(int statusCode) {
        withAnOauthEndpointResponseFromFile(null, statusCode);
    }

    private void callDataService() {
        requestClient.executeGetRequest(new GenericUrl("http://host"),
                TokenResponse.class);
    }

    private void withAccessToken(String accessToken, int statusCode) {
        TokenResponse token = aTokenResponseWithAccessTokenOf(accessToken);
        try {
            withAnOauthEndpointResponseFromFile(
                    getDefaultInstance().toPrettyString(token), statusCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void withAccessToken(String accessToken) {
        withAccessToken(accessToken, 200);
    }

    private static class HttpClientAccessCredentialTestModule
            extends DataServiceClientTestModule {

        public HttpClientAccessCredentialTestModule() {
            super(new QueingMockTransportResponseProvider());
        }

        @Override
        protected void configure() {
            super.configure();
        }

    }

}
