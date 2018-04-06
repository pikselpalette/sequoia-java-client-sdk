package com.piksel.sequoia.clientsdk.resource;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import com.piksel.sequoia.clientsdk.ClientException;
import com.piksel.sequoia.clientsdk.validation.Validatable;

public class ReferenceValidationException extends ClientException {

    private static final long serialVersionUID = 2790753074429742887L;

    private ReferenceValidationException(String message) {
        super(message);
    }

    public static ReferenceValidationException thrown(
            Set<ConstraintViolation<Validatable>> violations) {
        return new ReferenceValidationException(
                buildMessageFromViolations(violations));
    }

    private static String buildMessageFromViolations(
            Set<ConstraintViolation<Validatable>> violations) {
        return violations.stream().map(entry -> entry.getMessage())
                .collect(Collectors.joining(", "));
    }
}
