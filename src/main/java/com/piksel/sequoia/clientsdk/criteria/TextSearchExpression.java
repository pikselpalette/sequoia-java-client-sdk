package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.Internal;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Internal
@Getter
@EqualsAndHashCode
public class TextSearchExpression implements Criterion {

    private final Object value;
    private static final String PROPERTY_NAME = "q";
    private final Operator op;

    public TextSearchExpression(Object value, Operator op) {
        this.value = value;
        this.op = op;
    }

    public void applyExpression(DocumentName documentName, QueryString qs) {
        qs.put(documentName.get().concat(PROPERTY_NAME), String.valueOf(this.value));
    }
}
