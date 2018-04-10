package com.piksel.sequoia.clientsdk.validation;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 Piksel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
