package com.piksel.sequoia.clientsdk.resource.json;

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


import static com.google.api.client.util.Lists.newArrayList;
import static com.piksel.sequoia.clientsdk.configuration.DefaultClientConfiguration.getDefaultGson;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.api.client.util.Key;
import com.google.common.collect.ForwardingList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ForwardingListTest {

    @Test
    public void serialize_and_deserialize_works_properly_default_fields_names() {
        ListObjectWithDefaultFieldName test = new ListObjectWithDefaultFieldName();
        addValues(test);

        JsonElement json = getDefaultGson().toJsonTree(test);
        assertThat(expectedJson(), is(json));
        assertThat(test, is(getDefaultGson().fromJson(json,
                ListObjectWithDefaultFieldName.class)));
    }

    @Test
    public void serialize_and_deserialize_works_properly_custom_fields_names() {
        ListObjectWithCustomFieldName test = new ListObjectWithCustomFieldName();
        addValues(test);

        JsonElement json = getDefaultGson().toJsonTree(test);
        assertThat(expectedJson(), is(json));
        assertThat(test, is(getDefaultGson().fromJson(json,
                ListObjectWithDefaultFieldName.class)));
    }

    private static void addValues(List<String> list) {
        list.add("value1");
        list.add("value2");
        list.add("value3");
    }

    private static JsonElement expectedJson() {
        JsonArray values = new JsonArray();
        values.add(new JsonPrimitive("value1"));
        values.add(new JsonPrimitive("value2"));
        values.add(new JsonPrimitive("value3"));
        JsonObject result = new JsonObject();
        result.add("values", values);
        return result;
    }

    private static class ListObjectWithDefaultFieldName
            extends ForwardingList<String> {
        @Key
        private List<String> values = newArrayList();

        @Override
        protected List<String> delegate() {
            return values;
        }

    }

    private static class ListObjectWithCustomFieldName
            extends ForwardingList<String> {
        @Key("values")
        private List<String> vals = newArrayList();

        @Override
        protected List<String> delegate() {
            return vals;
        }

    }
}
