package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Marker interface for various individual criteria that can be applied to
 * resource selection requests.
 */
@PublicEvolving
public interface Criterion {

    void applyExpression(DocumentName documentName, QueryString qs);

}