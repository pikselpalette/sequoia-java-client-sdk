package com.piksel.sequoia.clientsdk.endpoint;

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

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.endpoint.headers.DefaultRequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.headers.RequestHeaders;
import com.piksel.sequoia.clientsdk.endpoint.params.DefaultQueryParams;
import com.piksel.sequoia.clientsdk.endpoint.params.QueryParams;

@PublicEvolving
public class Endpoints {

    public static QueryParams withParams(String key, String value) {
        return params().add(key, value);
    }

    public static DefaultQueryParams params() {
        return new DefaultQueryParams();
    }

    public static RequestHeaders withHeaders(String key, String value) {
        return headers().add(key, value);
    }

    public static DefaultRequestHeaders headers() {
        return new DefaultRequestHeaders();
    }
}
