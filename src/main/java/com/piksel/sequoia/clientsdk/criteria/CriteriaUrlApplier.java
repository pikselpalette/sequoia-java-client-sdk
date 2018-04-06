package com.piksel.sequoia.clientsdk.criteria;

import com.google.api.client.http.GenericUrl;
import com.piksel.sequoia.annotations.Internal;

import lombok.extern.slf4j.Slf4j;

/**
 * Applies the {@link Criteria} to an URL by creating .
 */
@Slf4j
@Internal
public class CriteriaUrlApplier {

    private final QueryStringFactory queryStringFactory = new QueryStringFactory();

    /**
     * Apply the criteria to the {@link GenericUrl url}.
     */
    public GenericUrl applyCriteria(GenericUrl url, Criteria<?> criteria) {
        log.debug("Applying criteria to URL [{}]", url);
        processQueryString(url, criteria);
        log.debug("Converted criteria to URL [{}]", url);
        return url;
    }

    private void processQueryString(GenericUrl url, Criteria<?> criteria) {
        QueryString qs = queryStringFactory
                .createQueryString((DefaultCriteria<?>) criteria);
        qs.asMap().forEach((key, value) -> url.set(key, value));
    }

}