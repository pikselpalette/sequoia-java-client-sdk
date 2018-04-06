package com.piksel.sequoia.clientsdk.resource;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.criteria.Criterion;

@PublicEvolving
public class Resources {

    /**
     * Convenience method for creating a criteria.
     * 
     * @param criterion the starting criteria expression
     * @return the resource criteria (which can be further added to)
     */
    public static ResourceCriteria where(Criterion criterion) {
        return criteria().add(criterion);
    }

    public static ResourceCriteria criteria() {
        return new DefaultResourceCriteria();
    }

}
