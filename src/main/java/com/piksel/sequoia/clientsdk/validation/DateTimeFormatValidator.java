package com.piksel.sequoia.clientsdk.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.piksel.sequoia.annotations.Internal;

@Internal
public class DateTimeFormatValidator
        implements ConstraintValidator<DateTimeFormat, String> {

    private String regex;

    @Override
    public void initialize(DateTimeFormat constraintAnnotation) {
        this.regex = constraintAnnotation.regex();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(regex));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
