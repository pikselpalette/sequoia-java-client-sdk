package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class UnsafeFieldNameException extends CriteriaException {

    private static final long serialVersionUID = 1L;

    public UnsafeFieldNameException(String message, Exception cause) {
        super("Unsafe field name. " + message, cause);
    }

}
