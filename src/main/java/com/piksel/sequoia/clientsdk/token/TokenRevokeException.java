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

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Indicates that the component failed to revoke the access token. The message
 * will contain the reason as to why the access token could not be revoked.
 */
@PublicEvolving
public final class TokenRevokeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create the exception with the specified message.
     */
    public TokenRevokeException(String message) {
        super(message);
    }

    /**
     * Create the exception from the source cause exception.
     */
    public TokenRevokeException(Exception exception) {
        super(exception);
    }

    /**
     * Create the exception from the source cause exception with the specified
     * message.
     */
    public TokenRevokeException(String message, Exception exception) {
        super(message, exception);
    }

}
