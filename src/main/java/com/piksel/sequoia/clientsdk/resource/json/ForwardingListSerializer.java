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
import static com.piksel.sequoia.clientsdk.resource.json.JsonSerializationHelper.getValue;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.api.client.util.Key;
import com.google.common.collect.ForwardingList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.piksel.sequoia.annotations.Internal;

@Internal
public class ForwardingListSerializer
        implements JsonSerializer<ForwardingList<?>> {

    @Override
    public JsonElement serialize(ForwardingList<?> value, Type type,
            JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        Class<?> typeAsClass = getTypeAsClass(type);

        @SuppressWarnings("unchecked")
        Set<Field> fields = getAllFields(typeAsClass,
                withAnnotation(Key.class));
        for (Field field : fields) {
            enableFieldAccess(field);
            result.add(getName(field),
                    context.serialize(getValue(field, value)));
        }

        return result;
    }
}
