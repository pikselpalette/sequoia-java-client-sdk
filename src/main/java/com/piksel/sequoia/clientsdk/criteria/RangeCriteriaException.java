package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class RangeCriteriaException extends CriteriaException {

    private static final long serialVersionUID = 1L;

    public RangeCriteriaException() {
        super("Unmatched number of range parameters.");
    }

}
