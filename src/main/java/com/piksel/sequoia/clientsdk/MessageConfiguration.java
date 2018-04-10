package com.piksel.sequoia.clientsdk;

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

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Value;

/**
 * Provides configuration about how to create and deliver JSON messages over
 * HTTP.
 */
@Value
@PublicEvolving
public class MessageConfiguration {

    HttpTransport transport;

    JsonFactory jsonFactory;
}
