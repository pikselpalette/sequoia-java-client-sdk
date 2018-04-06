package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Indicates that some error condition occurred whilst starting the client.
 */
@PublicEvolving
public class SequoiaClientInitialisationException extends ClientException {

    private static final long serialVersionUID = -5395253985658948891L;

    public SequoiaClientInitialisationException(String message) {
        super(message);
    }

    public SequoiaClientInitialisationException(Exception e) {
        super(e);
    }

}
