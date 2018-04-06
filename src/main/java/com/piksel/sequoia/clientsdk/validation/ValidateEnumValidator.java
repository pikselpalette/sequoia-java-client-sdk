package com.piksel.sequoia.clientsdk.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.piksel.sequoia.annotations.Internal;

@Internal
public class ValidateEnumValidator
        implements ConstraintValidator<ValidateEnum, String> {

    private List<String> valueList = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || valueList.contains(value.toUpperCase(Locale.ENGLISH));
    }

    @Override
    public void initialize(ValidateEnum constraintAnnotation) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            valueList.add(enumVal.toString().toUpperCase(Locale.ENGLISH));
        }

    }

}
