package com.piksel.sequoia.clientsdk;

import java.util.Optional;

import com.piksel.sequoia.annotations.Internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Internal
public class DefaultResponse<T> implements Response<T> {

    private final Optional<T> payload;

    private final int statusCode;

    private final boolean successStatusCode;
}
