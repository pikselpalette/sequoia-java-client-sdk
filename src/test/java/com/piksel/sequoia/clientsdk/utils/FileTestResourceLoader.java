package com.piksel.sequoia.clientsdk.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Satisfies @TestResource annotated fields with a File.
 */
public class FileTestResourceLoader implements TestResourceLoader {

    @Override
    public boolean canSatisfy(Field field) {
        return isFile(field);
    }

    private boolean isFile(Field field) {
        return field.getType().isAssignableFrom(File.class);
    }

    @Override
    public Object load(URL testResourceLocation,
            TestResourceContext testResourceContext) {
        return new File(testResourceLocation.getFile());
    }

}