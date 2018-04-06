package com.piksel.sequoia.clientsdk.resource;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ValidationException;
import com.piksel.sequoia.clientsdk.validation.Validatable;

/**
 * Exceptions thrown when validation errors are found over the {@link Resource
 * resource}.
 */
@PublicEvolving
public class ResourceValidationException extends ValidationException {

    private static final long serialVersionUID = 2790753074429742887L;

    private ResourceValidationException(String message) {
        super(message);
    }

    /**
     * Create an exception with the validations errors found.
     * 
     * @param violations
     *            found.
     */
    public static ResourceValidationException thrown(
            Set<ConstraintViolation<Validatable>> violations) {
        return new ResourceValidationException(
                buildMessageFromViolations(violations));
    }

    /**
     * Create an exception with the validations errors found.
     * 
     * @param violations
     *            found.
     * @param index
     *            of the {@link Resource resource} inside of the collection
     */
    public static ResourceValidationException thrown(
            Set<ConstraintViolation<Validatable>> violations, int index) {
        return new ResourceValidationException(
                buildMessageFromViolations(violations, index));
    }

}
