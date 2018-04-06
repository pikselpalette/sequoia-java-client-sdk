package com.piksel.sequoia.clientsdk.token;

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