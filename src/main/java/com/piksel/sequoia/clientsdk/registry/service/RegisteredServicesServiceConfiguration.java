package com.piksel.sequoia.clientsdk.registry.service;

import com.piksel.sequoia.annotations.Internal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Internal
public class RegisteredServicesServiceConfiguration {

    private static final int DEFAULT_REFRESH_INTERVAL_SECONDS = 3600;

    private int refreshIntervalSeconds;

    private String registryServiceOwner;

    public static RegisteredServicesServiceConfigurationBuilder builder() {
        return new RegisteredServicesServiceConfigurationBuilder()
                .refreshIntervalSeconds(DEFAULT_REFRESH_INTERVAL_SECONDS);
    }

}
