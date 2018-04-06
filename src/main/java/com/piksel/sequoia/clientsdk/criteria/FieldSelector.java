package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.EqualsAndHashCode;

@PublicEvolving
@EqualsAndHashCode
public class FieldSelector {

    private final String field;

    public FieldSelector(String fieldName) {
        ArgumentValidation.checkFieldName(fieldName);
        this.field = fieldName;
    }

    public String toString() {
        return this.field;
    }

}
