package com.piksel.sequoia.clientsdk.endpoint;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ValidationException;
import com.piksel.sequoia.clientsdk.validation.Validatable;

/**
 * Exceptions thrown when validation errors are found over the payload.
 */
@PublicEvolving
public class PayloadValidationException extends ValidationException {

    private static final long serialVersionUID = 2790753074429742887L;

    private PayloadValidationException(String message) {
        super(message);
    }

    /**
     * Create an exception with the validations errors found.
     * 
     * @param violations
     *            found.
     */
    public static PayloadValidationException thrown(
            Set<ConstraintViolation<Validatable>> violations) {
        return new PayloadValidationException(
                buildMessageFromViolations(violations));
    }

}