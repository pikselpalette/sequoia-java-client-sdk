package com.piksel.sequoia.clientsdk.criteria;

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

import com.piksel.sequoia.annotations.Internal;

/**
 * A {@link RangeLocation} specifies the boundaries of a range criteria.
 * 
 * <p>For example, the <code>BETWEEN</code> range specifies that the range
 * is between upper and lower bounds of the provided criteria boundaries.
 */
@Internal
enum RangeLocation {

    BETWEEN(2) {
        @Override
        public String doMatchString(String... args) {
            String va = args[0];
            String vb = args[1];
            return va + RANGE + vb;
        }
    },

    NOTBETWEEN(2) {
        @Override
        public String doMatchString(String... args) {
            String va = args[0];
            String vb = args[1];
            return NOT + va + RANGE + vb;
        }
    },

    LESSTHAN(1) {
        @Override
        public String doMatchString(String... args) {
            String v = args[0];
            return NOT + v + RANGE;
        }
    },

    LESSTHANOREQUALTO(1) {
        @Override
        public String doMatchString(String... args) {
            String v = args[0];
            return RANGE + v;
        }
    },

    GREATERTHAN(1) {
        @Override
        public String doMatchString(String... args) {
            String v = args[0];
            return NOT + RANGE + v;
        }
    },

    GREATERTHANOREQUALTO(1) {
        @Override
        public String doMatchString(String... args) {
            String v = args[0];
            return v + RANGE;
        }
    };

    private static final String NOT = "!";
    private static final String RANGE = "/";
    
    private final int argNum;

    RangeLocation(int argNum) {
        this.argNum = argNum;
    }

    /**
     * Construct the match string for the provided range criteria.
     */
    public String toMatchString(String... rangeCriteria) {
        if (argNum != rangeCriteria.length) {
            throw new RangeCriteriaException();
        }
        return doMatchString(rangeCriteria);
    }

    abstract String doMatchString(String... args);

}
