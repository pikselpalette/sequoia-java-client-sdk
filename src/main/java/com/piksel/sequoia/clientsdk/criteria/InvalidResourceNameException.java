package com.piksel.sequoia.clientsdk.criteria;

public class InvalidResourceNameException extends CriteriaException {

    private static final long serialVersionUID = 6448826872360652957L;

    public InvalidResourceNameException(String message, Exception cause) {
        super("Invalid resource name. " + message, cause);
    }

}
