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

import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.ClientException;

/**
 * Indicates that the component failed to acquire the access token. The message
 * will contain the reason as to why the access token could not be acquired.
 */
@Internal
public final class AccessTokenAcquisitionException extends ClientException {

    private static final long serialVersionUID = 1L;

    /**
     * Create the exception from the source cause exception.
     */
    public AccessTokenAcquisitionException(Exception exception) {
        super(exception);
    }

    /**
     * Create the exception from the provided {@link TokenErrorResponse}
     * details.
     */
    public AccessTokenAcquisitionException(TokenErrorResponse details) {
        super("Error getting access token with error [" + details.getError()
                + "], description [" + details.getErrorDescription()
                + "] and URI [" + details.getErrorUri() + "]");
    }

}
