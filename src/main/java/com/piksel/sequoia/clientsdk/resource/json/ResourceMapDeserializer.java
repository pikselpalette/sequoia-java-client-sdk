package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.ResourceMap;

@Internal
public class ResourceMapDeserializer implements JsonDeserializer<ResourceMap> {

    @Override
    public ResourceMap deserialize(JsonElement json, Type arg1,
            JsonDeserializationContext context) throws JsonParseException {
        ResourceMap resourceMap = new ResourceMap();
        final JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonPrimitive()
                    && entry.getValue().getAsJsonPrimitive().isBoolean()) {
                resourceMap.put(entry.getKey(),
                        entry.getValue().getAsBoolean());
            } else if (entry.getValue().isJsonPrimitive()
                    && entry.getValue().getAsJsonPrimitive().isString()) {
                resourceMap.put(entry.getKey(), entry.getValue().getAsString());
            } else if (entry.getValue().isJsonArray()) {
                resourceMap.put(entry.getKey(), ResourceListDeserializer
                        .toResourceList(entry.getValue().getAsJsonArray()));
            } else if (entry.getValue().isJsonPrimitive()
                    && entry.getValue().getAsJsonPrimitive().isNumber()) {
                resourceMap.put(entry.getKey(),
                        entry.getValue().getAsNumber().longValue());
            }
        });
        return resourceMap;
    }

}
