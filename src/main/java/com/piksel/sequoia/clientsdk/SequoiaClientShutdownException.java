package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Indicates that some error condition occurred whilst shutdown the client.
 */
@PublicEvolving
public class SequoiaClientShutdownException extends ClientException {

    private static final long serialVersionUID = -8412748337020306750L;

    public SequoiaClientShutdownException(String message) {
        super(message);
    }

    public SequoiaClientShutdownException(Exception e) {
        super(e);
    }

}
