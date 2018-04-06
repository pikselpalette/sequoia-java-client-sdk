package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Builder;
import lombok.Getter;

@PublicEvolving
@Builder
@Getter
public class Page {

    private final int page;

}
