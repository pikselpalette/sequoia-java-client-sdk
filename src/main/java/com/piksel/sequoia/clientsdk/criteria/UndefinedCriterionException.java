package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class UndefinedCriterionException extends CriteriaException {

    private static final long serialVersionUID = 1L;

    public UndefinedCriterionException(Exception e) {
        super("Criterion cannot be null", e);
    }

}
