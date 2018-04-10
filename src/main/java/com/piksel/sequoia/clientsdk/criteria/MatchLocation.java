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
 * Location that a search string should be matched on.
 * 
 * @see /docs/api
 */
@Internal
enum MatchLocation {

    /**
     * Match the search text at the beginning of the field or fields being
     * searched.
     */
    BEGIN {
        @Override
        public String toMatchString(String value) {
            return value + WILDCARD;
        }
    },

    /**
     * Match the search text at the end of the field or fields being searched.
     */
    END {
        @Override
        public String toMatchString(String value) {
            return WILDCARD + value;
        }
    },

    /**
     * Match the search text anywhere in the field or fields being searched.
     * 
     * @TODO FIXME there is no such feature, this would be a full text search
     *       corresponding to a Q filter
     */
    ANYWHERE {
        @Override
        public String toMatchString(String value) {
            return WILDCARD + value + WILDCARD;
        }
    },

    EXIST {
        @Override
        public String toMatchString(String value) {
            return WILDCARD;
        }
    },

    NOTEXIST {
        @Override
        public String toMatchString(String value) {
            return NOT + WILDCARD;
        }
    },

    NEGATION {
        @Override
        public String toMatchString(String value) {
            return NOT + value;
        }
    };

    private static final String WILDCARD = "*";

    private static final String NOT = "!";

    public abstract String toMatchString(String value);

}
