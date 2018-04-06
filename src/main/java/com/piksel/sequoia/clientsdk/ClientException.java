package com.piksel.sequoia.clientsdk;

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