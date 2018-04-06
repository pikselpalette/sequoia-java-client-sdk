package com.piksel.sequoia.clientsdk.model;

import org.junit.Rule;

import com.google.gson.Gson;
import com.piksel.sequoia.clientsdk.configuration.DefaultClientConfiguration;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;

/**
 * Test abstract class for tests wishing to load and parse JSON documents using
 * the {@link GsonBuilder} used in the data services client.
 */
public abstract class ModelResourceTestBase {

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    private Gson gson = DefaultClientConfiguration.getDefaultGson();

    protected <T> T parse(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    protected String serialize(Object object) {
        return gson.toJson(object);
    }
}