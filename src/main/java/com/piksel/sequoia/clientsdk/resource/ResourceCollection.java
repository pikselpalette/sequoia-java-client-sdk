package com.piksel.sequoia.clientsdk.resource;

import java.util.LinkedList;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public final class ResourceCollection<T extends Resource>
        extends LinkedList<T> {

    private static final long serialVersionUID = -8952160264793461567L;

}
