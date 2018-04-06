package com.piksel.sequoia.clientsdk.token;

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