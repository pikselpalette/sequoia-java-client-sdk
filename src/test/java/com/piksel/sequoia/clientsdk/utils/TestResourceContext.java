package com.piksel.sequoia.clientsdk.utils;

import java.lang.reflect.Field;

import org.junit.runner.Description;

/**
 * Contextual information for the loading of test resources.
 */
public final class TestResourceContext {

    private final Field field;
    private final Description testDescription;
    private final Object testInstance;

    /**
     * Create a new test resource context.
     */
    public TestResourceContext(Field field, Description testDescription,
            Object testInstance) {
        this.field = field;
        this.testDescription = testDescription;
        this.testInstance = testInstance;
    }

    /**
     * Provides the field instance of the annotated test resource.
     * 
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * The Junit test execution description.
     * 
     * @return the test description
     */
    public Description getTestDescription() {
        return testDescription;
    }

    /**
     * The object instance of the executing test suite.
     * 
     * @return the test instance object
     */
    public Object getTestInstance() {
        return testInstance;
    }

    @Override
    public String toString() {
        return "TestResourceContext [field=" + field + ", testDescription="
                + testDescription + ", testInstance=" + testInstance + "]";
    }
}