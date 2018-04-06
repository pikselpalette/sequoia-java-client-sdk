package com.piksel.sequoia.clientsdk.registry;

import com.piksel.sequoia.annotations.Internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Internal
public class RegistryClientConfiguration {

    private final String registryServiceOwner;

}
