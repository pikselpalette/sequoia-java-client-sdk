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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UrlQueryStringParserTest {

    @Test
    public void testParser() {
        assertThat(
                UrlQueryStringParser.urlParser("http://my url.com?test=true")
                        .queryString().keySet().iterator().next(),
                is("test"));
        assertThat(
                UrlQueryStringParser.urlParser("http://my url.com?test=true")
                        .queryString().values().iterator().next(),
                is("true"));
    }
}
