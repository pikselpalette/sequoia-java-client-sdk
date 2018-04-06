package com.piksel.sequoia.clientsdk.resource;

import com.google.api.client.util.Key;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbstractMeta {

    @Key
    private int page = 1;

    @Key
    private int perPage;

    @Key
    private String first;

    @Key
    private String next;

    @Key
    private String prev;

    @Key
    private Integer totalCount;

}
