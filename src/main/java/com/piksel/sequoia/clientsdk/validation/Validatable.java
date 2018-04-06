package com.piksel.sequoia.clientsdk.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Enables validation for JSR303 style bean validation and declarative
 * validation annotations. Supports the ability to request validation groups to
 * be validated also.
 */
@PublicEvolving
public interface Validatable {

    ValidatorFactory VALIDATOR_FACTORY = Validation
            .buildDefaultValidatorFactory();

    default Set<ConstraintViolation<Validatable>> isValid() {
        return VALIDATOR_FACTORY.getValidator().validate(this);
    }

    default Set<ConstraintViolation<Validatable>> isValid(Class<?>... groups) {
        return VALIDATOR_FACTORY.getValidator().validate(this, groups);
    }

}