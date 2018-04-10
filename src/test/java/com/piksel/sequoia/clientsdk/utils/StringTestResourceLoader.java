package com.piksel.sequoia.clientsdk.utils;

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

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Satisfies @TestResource annotated field with a String.
 */
public class StringTestResourceLoader implements TestResourceLoader {

    @Override
    public boolean canSatisfy(Field field) {
        return isStringType(field);
    }

    private boolean isStringType(Field field) {
        return field.getType().isAssignableFrom(String.class);
    }

    @Override
    public Object load(URL testResourceLocation,
            TestResourceContext testResourceContext) {
        try {

            return Resources.toString(testResourceLocation, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
