package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.Reference;

@Internal
public class ReferenceSerializer implements JsonSerializer<Reference> {

    @Override
    public JsonElement serialize(Reference reference, Type arg1, JsonSerializationContext context) {
        return context.serialize(reference.toString().isEmpty() ? null : reference.toString());
    }
}
