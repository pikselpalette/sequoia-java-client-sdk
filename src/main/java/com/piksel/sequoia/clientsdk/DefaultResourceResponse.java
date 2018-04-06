package com.piksel.sequoia.clientsdk;

import java.util.Optional;

import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Internal
public class DefaultResourceResponse<T extends Resource>
        implements ResourceResponse<T> {

    private final Optional<ResourceIterable<T>> payload;

    private final int statusCode;

    private final boolean successStatusCode;

}
