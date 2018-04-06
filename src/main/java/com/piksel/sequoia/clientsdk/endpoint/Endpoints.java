package com.piksel.sequoia.clientsdk.endpoint;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.endpoint.headers.DefaultRequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.headers.RequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.params.DefaultQueryParams;
import com.piksel.sequoia.clientsdk.endpoint.params.QueryParams;

@PublicEvolving
public class Endpoints {

    public static QueryParams withParams(String key, String value) {
        return params().add(key, value);
    }

    public static DefaultQueryParams params() {
        return new DefaultQueryParams();
    }

    public static RequestHeaders withHeaders(String key, String value) {
        return headers().add(key, value);
    }

    public static DefaultRequestHeaders headers() {
        return new DefaultRequestHeaders();
    }
}
