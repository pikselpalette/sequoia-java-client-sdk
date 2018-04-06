package com.piksel.sequoia.clientsdk;

import com.google.api.client.http.HttpRequestFactory;
import com.piksel.sequoia.annotations.Internal;

@Internal
public interface RequestFactory {

    HttpRequestFactory getRequestFactory();

}