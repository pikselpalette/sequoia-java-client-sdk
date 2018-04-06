package com.piksel.sequoia.clientsdk.resource.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.MetadataLockFieldValue;

import java.lang.reflect.Type;

@Internal
public class MetadataLockFieldValueSerializer implements JsonSerializer<MetadataLockFieldValue> {
    @Override
    public JsonElement serialize(MetadataLockFieldValue src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString().isEmpty() ? null : src.toString());
    }
}
