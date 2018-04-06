package com.piksel.sequoia.clientsdk;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.piksel.sequoia.annotations.Internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Internal
public class DefaultJsonElementResponse implements Response<JsonElement> {

    private final Optional<JsonElement> payload;

    private final int statusCode;

    private final boolean successStatusCode;

}
