package com.piksel.sequoia.clientsdk.criteria;

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
