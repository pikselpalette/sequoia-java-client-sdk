package com.piksel.sequoia.clientsdk.criteria;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Builder;
import lombok.Getter;

@PublicEvolving
@Getter
@Builder
public class Language {

    private final String lang;

}