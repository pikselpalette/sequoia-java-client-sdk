package com.piksel.sequoia.clientsdk.resource;

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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UrlQueryStringParserTest {

    @Test
    public void testParser() {
        Map<String, String> queryParameters = UrlQueryStringParser.urlParser(
                "http://my url.com?param1=value1&test=true&continue=a=bc=d=e%3D&param2=value2&param3=value3")
                .queryString();

        Map<String, String> queryParametersExpected = new HashMap<String, String>() {{
            put("test", "true");
            put("continue", "a=bc=d=e=");
            put("param1", "value1");
            put("param2", "value2");
            put("param3", "value3");
        }};

        assertThat(queryParameters, is(queryParametersExpected));
    }
}
