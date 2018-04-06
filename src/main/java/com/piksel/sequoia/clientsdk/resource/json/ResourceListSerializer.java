package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.ResourceList;

@Internal
public class ResourceListSerializer implements JsonSerializer<ResourceList> {

    @Override
    public JsonElement serialize(ResourceList resourceList, Type arg1,
            JsonSerializationContext context) {
        return context.serialize(resourceList.getCollection());
    }
}
