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
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.util.BackOff;
import com.google.api.client.util.Objects;
import com.piksel.sequoia.annotations.Internal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Internal
public class DataServicesClientGrantUnsuccessfulResponseHandler extends HttpBackOffUnsuccessfulResponseHandler
        implements ClientGrantCredentialUnsuccessfulResponseHandler {
 
    public DataServicesClientGrantUnsuccessfulResponseHandler(BackOff backOff) {
        super(backOff);
    }

    private static final String AUTHENTICATE_INVALID_TOKEN_TYPE = "Bearer ";
    private static final Pattern AUTHENTICATE_REGEX_INVALID_TOKEN_MESSAGE = Pattern
            .compile("\\s*error\\s*=\\s*\"?Invalid token\"?");
    private final Lock lock = new ReentrantLock();

    @Override
    public boolean handleUnsuccessful(Credential credential,
            HttpRequest request, HttpResponse response, boolean supportsRetry) {
        boolean result;

        if (refreshTokenWhenBearerAndTokenIsInvalid(response)) {
            result = refreshToken(credential, request, response, supportsRetry);
        } else if (refreshTokenWhenUnauthorizedAndNoBearer(response)) {
            result = refreshToken(credential, request, response, supportsRetry);
        } else {
            result = handleHttpBackOff(request, response, supportsRetry);
        }

        return result;
    }

    private boolean handleHttpBackOff(HttpRequest request, HttpResponse response, boolean supportsRetry) {
        try {
            return super.handleResponse(request, response, supportsRetry);
        } catch (IOException e) {
            throw new HttpBackOffUnsuccessfulResponseHandlerException(e);
        }
    }

    private boolean refreshTokenWhenBearerAndTokenIsInvalid(
            HttpResponse response) {
        boolean result = false;

        List<String> authenticateList = response.getHeaders()
                .getAuthenticateAsList();

        // if authenticate list is not null we will check if one of the entries
        // contains "Bearer"
        if (authenticateList != null) {
            for (String authenticate : authenticateList) {
                if (authenticate.startsWith(AUTHENTICATE_INVALID_TOKEN_TYPE)) {
                    // mark that we found a "Bearer" value, and check if there
                    // is a invalid_token
                    // error
                    result = AUTHENTICATE_REGEX_INVALID_TOKEN_MESSAGE
                            .matcher(authenticate).find();
                    break;
                }
            }
        }

        return result;
    }

    private boolean refreshTokenWhenUnauthorizedAndNoBearer(
            HttpResponse response) {
        return response
                .getStatusCode() == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED;
    }

    private boolean refreshToken(Credential credential, HttpRequest request,
            HttpResponse response, boolean supportsRetry) {
        boolean result = false;
        try {
            lock.lock();
            try {
                // need to check if another thread has already refreshed the
                // token
                result = !Objects.equal(credential.getAccessToken(),
                        credential.getMethod()
                                .getAccessTokenFromRequest(request))
                        || credential.refreshToken();
            } finally {
                lock.unlock();
            }
        } catch (IOException exception) {
            log.error("unable to refresh token", exception);
        }

        return result;
    }
    
    public static class HttpBackOffUnsuccessfulResponseHandlerException extends RuntimeException {

        private static final long serialVersionUID = -5476489005643445491L;
        
        public HttpBackOffUnsuccessfulResponseHandlerException(IOException e) {
            super(e);
        }
        
    }
}
