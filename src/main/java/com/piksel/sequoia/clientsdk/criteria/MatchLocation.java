package com.piksel.sequoia.clientsdk.criteria;

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