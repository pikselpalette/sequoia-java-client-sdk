package com.piksel.sequoia.clientsdk;

import com.google.api.client.http.HttpRequest;
import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Getter;

@PublicEvolving
@Getter
public final class RequestConfigurationException extends ClientException {
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Error configuring request [%s %s] with [%s]";

    private final transient HttpRequest request;
    private final transient Object configurator;

    /**
     * Create the exception from the source cause exception.
     */
    public RequestConfigurationException(Exception exception) {
        super(exception);
        request = null;
        configurator = null;
    }

    /**
     * Create the exception from the provided {@link HttpRequest} and
     * configurator details.
     */
    public RequestConfigurationException(HttpRequest request,
            Object configurator) {
        super(String.format(MESSAGE, request.getRequestMethod(),
                request.getUrl().getRawPath(), configurator));
        this.request = request;
        this.configurator = configurator;
    }

    /**
     * Create the exception from the provided {@link HttpRequest}, configurator
     * and source cause exception.
     */
    public RequestConfigurationException(HttpRequest request,
            Object configurator, Exception exception) {
        super(String.format(MESSAGE, request.getRequestMethod(),
                request.getUrl().getRawPath(), configurator), exception);
        this.request = request;
        this.configurator = configurator;
    }
}