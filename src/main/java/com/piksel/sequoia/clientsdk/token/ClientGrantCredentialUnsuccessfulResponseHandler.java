package com.piksel.sequoia.clientsdk.token;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.piksel.sequoia.annotations.Internal;

@Internal
public interface ClientGrantCredentialUnsuccessfulResponseHandler {

    boolean handleUnsuccessful(Credential credential,
                               HttpRequest request, HttpResponse response, boolean supportsRetry);
}
