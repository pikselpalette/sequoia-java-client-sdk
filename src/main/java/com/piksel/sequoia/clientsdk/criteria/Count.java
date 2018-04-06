package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@PublicEvolving
public class Count {

    private final boolean count;

}
