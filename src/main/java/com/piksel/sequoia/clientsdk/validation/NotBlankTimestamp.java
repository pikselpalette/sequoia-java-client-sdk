package com.piksel.sequoia.clientsdk.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.NotBlank;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Timestamp and mandatory validation.
 */
@ConstraintComposition(CompositionType.AND)
@NotBlank
@DateTimeFormat
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Documented
@PublicEvolving
public @interface NotBlankTimestamp {
    
    /**
     * A message to associate with the violation.
     */
    String message() default "Validation for a mandatory timestamp failed.";

    /**
     * Pass validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Some payload related to the violation, such as potentially the
     * severity of the violation. 
     * 
     * See http://beanvalidation.org/1.0/spec/#constraintsdefinitionimplementation-constraintdefinition-payload
     */
    Class<? extends Payload>[] payload() default {};
}