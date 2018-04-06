package com.piksel.sequoia.clientsdk.recovery;

import com.google.api.client.util.BackOff;
import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Builder;
import lombok.Value;

/**
 * Describes how the data services client should response to failures.
 */
@PublicEvolving
@Value
@Builder
public class RecoveryStrategy {

    private final BackOff backOff;
    
    private Integer numberOfRetries;

}