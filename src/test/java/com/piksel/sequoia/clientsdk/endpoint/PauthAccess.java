package com.piksel.sequoia.clientsdk.endpoint;

import java.util.Collection;

import lombok.Data;

@Data
public class PauthAccess {

    private final String expiresAt;

    private final String userType;

    private final String userRef;

    private final Collection<Permission> permissions;

    @Data
    static class Permission {
        private final String tenant;

        private final Collection<String> actions;
    }

}
