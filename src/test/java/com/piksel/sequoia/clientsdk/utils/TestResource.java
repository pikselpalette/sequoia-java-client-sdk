package com.piksel.sequoia.clientsdk.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as eligible for test resource loading.
 * 
 * <p>The resource specified in the annotation will be loaded as a class resource,
 * see {@link Class#getResourceAsStream(String)}.
 * 
 * @see Class#getResourceAsStream(String)
 * @see TestResourceRule
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface TestResource {

    /**
     * The class-relative path to the test resource.
     */
    String value();
}