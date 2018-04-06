package com.piksel.sequoia.clientsdk.endpoint;

import com.google.api.client.http.GenericUrl;
import com.piksel.sequoia.clientsdk.endpoint.params.QueryParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Applies the {@link QueryParams} to an URL by creating .
 */
@Slf4j
class UrlApplier {

    public GenericUrl applyQueryParams(GenericUrl url, QueryParams params) {
        log.debug("Applying params to URL [{}]", url);
        processQueryString(url, params);
        log.debug("Converted params to URL [{}]", url);
        return url;
    }

    private void processQueryString(GenericUrl url, QueryParams params) {
        params.getParams().forEach((key, value) -> url.set(key, value));
    }

}