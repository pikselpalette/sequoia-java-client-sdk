package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Builder;
import lombok.Getter;

/**
 * Controls how result resources are processed. 
 */
@PublicEvolving
@Getter
@Builder
public class ResourceSettings {

    private final boolean skipResources;

}
