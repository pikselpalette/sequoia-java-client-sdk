package com.piksel.sequoia.clientsdk;

import com.piksel.sequoia.annotations.PublicEvolving;
import lombok.Getter;

/**
 * Indicates that the requested service does not exist.
 */
@PublicEvolving
@Getter
public class NoSuchServiceException extends ClientException {

    private static final long serialVersionUID = 3521224394123458444L;

    private final String serviceName;

    public NoSuchServiceException(String serviceName) {
        super("Unable to create the service with name: " + serviceName);
        this.serviceName = serviceName;
    }
}
