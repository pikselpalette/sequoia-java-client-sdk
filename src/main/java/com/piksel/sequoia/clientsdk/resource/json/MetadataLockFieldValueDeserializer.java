package com.piksel.sequoia.clientsdk.resource.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.MetadataLockFieldValue;

import java.lang.reflect.Type;

@Internal
public class MetadataLockFieldValueDeserializer implements JsonDeserializer<MetadataLockFieldValue> {
    @Override
    public MetadataLockFieldValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            return MetadataLockFieldValue.fromString(json.getAsString());
        }
        return null;
    }
}
