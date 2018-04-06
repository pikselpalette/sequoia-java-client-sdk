package com.piksel.sequoia.clientsdk;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Value;

/**
 * Provides configuration about how to create and deliver JSON messages over
 * HTTP.
 */
@Value
@PublicEvolving
public class MessageConfiguration {

    HttpTransport transport;

    JsonFactory jsonFactory;
}