package com.piksel.sequoia.clientsdk;

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
 * Base client exception in the hierarchy, establishing all exceptions as 
 * RuntimeException and all wrappable causes as {@link Exception}.
 */
@PublicEvolving
public class ClientException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Create the exception without message or cause.
     */
    public ClientException() {
    }
    
    /**
     * Create the exception with the provided message.
     * @param message the exception message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Create the exception with the provided exception cause.
     * @param cause the exception cause
     */
    public ClientException(Exception cause) {
        super(cause);
    }

    /**
     * Create the exception with the provided message and cause.
     * @param message the message for the exception
     * @param cause the cause of the exception
     */
    public ClientException(String message, Exception cause) {
        super(message, cause);
    }

}
