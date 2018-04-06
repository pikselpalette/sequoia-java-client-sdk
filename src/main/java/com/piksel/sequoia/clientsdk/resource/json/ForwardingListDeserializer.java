package com.piksel.sequoia.clientsdk.resource.json;

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
