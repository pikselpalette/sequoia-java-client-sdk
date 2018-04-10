package com.piksel.sequoia.clientsdk.resource;

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
