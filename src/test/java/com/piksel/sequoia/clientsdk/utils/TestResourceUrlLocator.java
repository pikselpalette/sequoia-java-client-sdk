package com.piksel.sequoia.clientsdk.utils;

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