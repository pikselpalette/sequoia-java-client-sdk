package com.piksel.sequoia.clientsdk;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.validation.Validatable;

@PublicEvolving
public class ValidationException extends ClientException {

    private static final long serialVersionUID = 4479188989849333801L;

    protected ValidationException(String message) {
        super(message);
    }

    protected static String buildMessageFromViolations(
            Set<ConstraintViolation<Validatable>> violations) {
        return violations.stream()
                .map(entry -> String.join(" ",
                        entry.getPropertyPath().toString(), entry.getMessage()))
                .collect(Collectors.joining(", "));
    }

    protected static String buildMessageFromViolations(
            Set<ConstraintViolation<Validatable>> violations, int index) {
        return violations.stream()
                .map(entry -> String.join(" ",
                        entry.getPropertyPath().toString(), entry.getMessage(),
                        "- resource number " + index + " in the collection"))
                .collect(Collectors.joining(", "));
    }

}
