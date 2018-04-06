package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.ResourceMap;

@Internal
public class ResourceMapSerializer implements JsonSerializer<ResourceMap> {

    @Override
    public JsonElement serialize(ResourceMap resourceMap, Type arg1,
            JsonSerializationContext context) {
        return context.serialize(resourceMap.getMap());
    }

}
