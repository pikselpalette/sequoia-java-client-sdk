package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.Internal;

@Internal
enum LogicalLocation {

    ONEORMORE {
        @Override
        public String toMatchString(String[] values) {
            return String.join(OR, values);
        }
    };

    private static final String OR = "||";

    public abstract String toMatchString(String[] values);
}
