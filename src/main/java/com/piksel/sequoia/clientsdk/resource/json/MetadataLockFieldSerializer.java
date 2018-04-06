package com.piksel.sequoia.clientsdk.resource.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.clientsdk.resource.MetadataLockField;

import java.lang.reflect.Type;

public class MetadataLockFieldSerializer implements JsonSerializer<MetadataLockField> {

    @Override
    public JsonElement serialize(MetadataLockField src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getLockValues());
    }

}
