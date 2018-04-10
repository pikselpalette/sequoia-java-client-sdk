package com.piksel.sequoia.clientsdk.resource.json;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 Piksel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
