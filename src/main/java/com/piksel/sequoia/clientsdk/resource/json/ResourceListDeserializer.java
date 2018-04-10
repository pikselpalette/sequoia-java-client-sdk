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
