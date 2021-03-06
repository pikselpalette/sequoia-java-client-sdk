package com.piksel.sequoia.clientsdk.endpoint.headers;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link Headers} for holding the headers added.
 */
class DefaultHeaders<T extends Headers<T>> implements Headers<T> {

    Map<String, Object> headers;

    protected DefaultHeaders() {
        headers = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T add(String key, Object value) {
        headers.put(key, value);
        return (T) this;
    }

    @Override
    public Map<? extends String, ?> getHeaders() {
        return headers;
    }

}
