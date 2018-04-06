package com.piksel.sequoia.clientsdk.configuration;

import java.lang.reflect.Type;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Class for holding type and typeAdapters, it's used for registered adapterTypes using {@link ClientConfiguration}
 * 
 * superClass - for managing hierarchy adapters.
 * type - the type definition for the type adapter being registered
 * typeAdapter - This object must implement at least one of the InstanceCreator, JsonSerializer, and a JsonDeserializer interfaces.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Class for holding type and typeAdapter.
 */
@Getter
@AllArgsConstructor
@Builder
@PublicEvolving
public class TypeAdapter {

    private final Class<?> superClass;

    /**
     * The type definition for the type adapter being registered.
     */
    private final Type type;

    /**
     * This object must implement at least one of the InstanceCreator,
     * JsonSerializer, and a JsonDeserializer interfaces.
     */
    private final Object typeAdapter;

    public boolean isHierarchyAdapter() {
        return superClass != null;
    }
}
