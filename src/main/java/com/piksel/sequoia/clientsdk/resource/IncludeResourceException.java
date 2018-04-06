package com.piksel.sequoia.clientsdk.resource;

import java.util.Collection;
import java.util.stream.Collectors;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public class IncludeResourceException extends RuntimeException {

    private static final long serialVersionUID = 713695156113045870L;

    public IncludeResourceException(Collection<String> messages) {
        super(messages.stream().collect(Collectors.joining(", ")));
    }

}
