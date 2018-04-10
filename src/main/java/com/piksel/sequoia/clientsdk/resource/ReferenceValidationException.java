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
