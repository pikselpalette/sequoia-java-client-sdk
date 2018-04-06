package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.ResourceList;

@Internal
public class ResourceListDeserializer implements JsonDeserializer<ResourceList> {

    @Override
    public ResourceList deserialize(JsonElement json, Type arg1, JsonDeserializationContext context) throws JsonParseException {
        return toResourceList(json.getAsJsonArray());
    }

    static ResourceList toResourceList(JsonArray jsonArray) {
        ResourceList resourceList = new ResourceList();
        jsonArray.forEach(entry -> {
            if (entry.getAsJsonPrimitive().isBoolean()) {
                resourceList.add(entry.getAsBoolean());
            } else if (entry.getAsJsonPrimitive().isString()) {
                resourceList.add(entry.getAsString());
            } else if (entry.getAsJsonPrimitive().isNumber()) {
                Number number = entry.getAsNumber();
                resourceList.add(numberToTypedNumber(number));
            }
        });

        return resourceList;
    }

    private static Number numberToTypedNumber(Number number) {
        return number.doubleValue();
    }

}
