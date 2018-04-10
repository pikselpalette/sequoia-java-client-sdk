package com.piksel.sequoia.clientsdk.endpoint.params;

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

import java.util.Map;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Methods for managing parameters for endpoints.
 */
@PublicEvolving
public interface Params<T> {

    /**
     * Allows to add parameters key and value.
     */
    T add(String key, String value);

    /**
     * Returns the parameters added.
     */
    Map<String, String> getParams();

}
