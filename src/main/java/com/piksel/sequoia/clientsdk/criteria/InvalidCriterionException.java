package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class InvalidCriterionException extends CriteriaException {

    private static final long serialVersionUID = 4092289573943642617L;

    public InvalidCriterionException(Exception e) {
        super("Cannot filter for an empty or null value", e);
    }
}
