package com.piksel.sequoia.clientsdk.configuration;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Value;

/**
 * Holds the <code>username</code> and <code>password</code> for the client.
 */
@Value
@PublicEvolving
public class ComponentCredentials {

    String username;

    String password;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(username=" + getUsername() + ", password=***)";
    }
}