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
