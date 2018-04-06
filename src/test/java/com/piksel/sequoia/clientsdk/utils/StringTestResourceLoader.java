package com.piksel.sequoia.clientsdk.utils;

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