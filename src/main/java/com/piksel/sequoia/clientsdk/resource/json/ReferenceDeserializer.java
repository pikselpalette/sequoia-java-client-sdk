package com.piksel.sequoia.clientsdk.resource.json;

import java.lang.reflect.Type;
import java.util.Objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.resource.Reference;

@Internal
public class ReferenceDeserializer implements JsonDeserializer<Reference> {

    @Override
    public Reference deserialize(JsonElement json, Type arg1,
            JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            return Reference.fromReference(json.getAsString());
        }
        return Reference.fromOwnerAndName(
                getField(json.getAsJsonObject(), "owner"),
                getField(json.getAsJsonObject(), "name"));
    }

    private String getField(JsonObject jsonObject, String field) {
        return Objects.nonNull(jsonObject.get(field))
                ? jsonObject.get(field).getAsString() : null;
    }

}
