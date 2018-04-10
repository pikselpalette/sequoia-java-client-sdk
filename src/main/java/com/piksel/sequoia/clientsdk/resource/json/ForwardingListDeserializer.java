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

import static com.piksel.sequoia.clientsdk.resource.json.JsonSerializationHelper.enableFieldAccess;
import static com.piksel.sequoia.clientsdk.resource.json.JsonSerializationHelper.getName;
import static com.piksel.sequoia.clientsdk.resource.json.JsonSerializationHelper.getTypeAsClass;
import static com.piksel.sequoia.clientsdk.resource.json.JsonSerializationHelper.setValue;
import static org.objenesis.ObjenesisHelper.newInstance;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.api.client.util.Key;
import com.google.common.collect.ForwardingList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.piksel.sequoia.annotations.Internal;

@Internal
public class ForwardingListDeserializer implements JsonDeserializer<ForwardingList<?>> {

    @Override
    public ForwardingList<?> deserialize(JsonElement json, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        Class<?> typeAsClass = getTypeAsClass(type);
        ForwardingList<?> result = (ForwardingList<?>) newInstance(typeAsClass);
        JsonObject jsonValue = json.getAsJsonObject();

        @SuppressWarnings("unchecked")
        Set<Field> fields = getAllFields(typeAsClass,
                withAnnotation(Key.class));
        for (Field field : fields) {
            enableFieldAccess(field);
            JsonElement fieldJsonValue = jsonValue.get(getName(field));
            if (!fieldJsonValue.isJsonNull()) {
                Object fieldValue = context.deserialize(fieldJsonValue,
                        field.getGenericType());
                setValue(field, result, fieldValue);
            }
        }

        return result;
    }

}
