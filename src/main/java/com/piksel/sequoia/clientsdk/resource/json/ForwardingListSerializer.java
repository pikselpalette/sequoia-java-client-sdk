package com.piksel.sequoia.clientsdk.resource.json;

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
