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

import java.lang.reflect.Field;
import java.net.URL;

/**
 * Locates a URL by resolving a test resource location path via the declaring
 * class's class loader.
 */
public class TestResourceUrlLocator {

    /**
     * Given a field, return the URL pointing to the resource provided in
     * the @TestResource annotation.
     */
    public URL locate(Field field) {
        String resourcePath = getResourcePath(field);
        Class<?> declaringClass = getDeclaringClass(field);
        URL path = locateResource(resourcePath, declaringClass);
        if (path == null) {
            throw new IllegalArgumentException(
                    "Could not locate resource from path: " + resourcePath);
        }
        return path;
    }

    private URL locateResource(String resourcePath, Class<?> declaringClass) {
        return declaringClass.getResource(resourcePath);
    }

    private String getResourcePath(Field field) {
        TestResource testResourceAnnotation = field
                .getAnnotation(TestResource.class);
        return testResourceAnnotation.value();
    }

    private Class<?> getDeclaringClass(Field field) {
        return field.getDeclaringClass();
    }
}
