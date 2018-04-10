package com.piksel.sequoia.clientsdk.configuration;

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
