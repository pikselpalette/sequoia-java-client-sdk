package com.piksel.sequoia.clientsdk.utils;

import java.lang.reflect.Field;
import java.net.URL;

/**
 * Strategy interface for components capable of supplying a resource to a test
 * case field.
 */
public interface TestResourceLoader {

    /**
     * Determine whether the component is capable of satisfying the annotated
     * field.
     * 
     * @param field
     *            the {@link Field} instance annotated with @TestResource
     * @return true if this component can satisfy the test resource field type
     */
    boolean canSatisfy(Field field);

    /**
     * Given a test resource location, load the test resource instance.
     * 
     * @param testResourceLocation
     *            the test resource location to load
     * @param testResourceContext
     *            contextual information
     * @return the object to load
     */
    Object load(URL testResourceLocation,
            TestResourceContext testResourceContext);

}