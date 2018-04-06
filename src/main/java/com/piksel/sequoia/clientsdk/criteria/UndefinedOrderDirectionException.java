package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class UndefinedOrderDirectionException extends CriteriaException {

    private static final long serialVersionUID = 1L;

    public UndefinedOrderDirectionException(Exception e) {
        super("Cannot change ordering of unknown field", e);
    }

}
