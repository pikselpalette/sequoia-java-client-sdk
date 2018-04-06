package com.piksel.sequoia.clientsdk.token;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Indicates that the component failed to refresh the access token. The message
 * will contain the reason as to why the access token could not be acquired.
 */
@PublicEvolving
public final class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create the exception with the specified message.
     */
    public TokenRefreshException(String message) {
        super(message);
    }

    /**
     * Create the exception from the source cause exception.
     */
    public TokenRefreshException(Exception exception) {
        super(exception);
    }

    /**
     * Create the exception from the source cause exception with the specified
     * message.
     */
    public TokenRefreshException(String message, Exception exception) {
        super(message, exception);
    }

}