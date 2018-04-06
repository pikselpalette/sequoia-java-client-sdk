package com.piksel.sequoia.clientsdk.validation;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.piksel.sequoia.annotations.Internal;

@Internal
public class DurationFormatValidator
        implements ConstraintValidator<DurationFormat, String> {

    @Override
    public void initialize(DurationFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            Duration.parse(value);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
