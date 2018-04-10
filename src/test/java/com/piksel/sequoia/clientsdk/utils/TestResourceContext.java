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
