package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ClientException;

@PublicEvolving
public class CriteriaException extends ClientException {

    private static final long serialVersionUID = 1L;

    public CriteriaException(Exception e) {
        super(e);
    }

    public CriteriaException(String message, Exception cause) {
        super(message, cause);
    }

    public CriteriaException(String message) {
        super(message);
    }

}
