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
