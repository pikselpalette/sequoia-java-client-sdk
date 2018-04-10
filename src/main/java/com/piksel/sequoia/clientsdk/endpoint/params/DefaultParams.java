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
import java.util.TreeMap;

/**
 * Implementation of {@link Params} for holding the params added.
 */
class DefaultParams<T extends Params<T>> implements Params<T> {

    Map<String, String> params;

    protected DefaultParams() {
        params = new TreeMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T add(String key, String value) {
        params.put(key, value);
        return (T) this;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
