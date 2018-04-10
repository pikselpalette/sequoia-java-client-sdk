package com.piksel.sequoia.clientsdk;

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
