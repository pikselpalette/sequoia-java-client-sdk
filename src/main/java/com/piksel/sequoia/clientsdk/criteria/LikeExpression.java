package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.Internal;

import lombok.EqualsAndHashCode;

/**
 * Used for selecting values from a partial string.
 * 
 * @see MatchLocation
 */
@Internal
@EqualsAndHashCode(callSuper = true)
public class LikeExpression extends SimpleExpression {

    private final MatchLocation matchLocation;

    public LikeExpression(String propertyName, String value) {
        this(propertyName, value, MatchLocation.ANYWHERE);
    }

    public LikeExpression(String propertyName, String value,
            MatchLocation matchLocation) {
        super(propertyName, value, Operator.LIKE);
        this.matchLocation = matchLocation;
    }

    public void applyExpression(DocumentName documentName, QueryString qs) {
        qs.put(documentName.get().concat(this.getPropertyName()), this.matchLocation
                .toMatchString(String.valueOf(this.getValue())));
    }

}
