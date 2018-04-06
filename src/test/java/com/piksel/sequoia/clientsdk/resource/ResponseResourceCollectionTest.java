package com.piksel.sequoia.clientsdk.resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.client.util.Key;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.piksel.sequoia.clientsdk.configuration.DefaultClientConfiguration;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;

public class ResponseResourceCollectionTest {

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final JsonParser JSONPARSER = new JsonParser();

    @TestResource("response-single-resources.json")
    private String singleResponseCollection;

    @TestResource("response-collection-multiple-resources.json")
    private String multipleResponsesCollection;

    @TestResource("response-empty-resources.json")
    private String emptyResponsesCollection;

    @TestResource("response-no-meta-resources.json")
    private String noMetaResponsesCollection;

    @Test
    public void shouldDeserialiseSingleItemCollection() {
        thrown.expect(NoSuchElementException.class);
        JsonElement jsonObject = JSONPARSER.parse(singleResponseCollection);

        PageableResourceEndpoint<MyResponseResource> endpoint = withEndpoint();

        ResourceIterable<MyResponseResource> resourceIterable = new LazyLoadingResourceIterable<>(
                jsonObject, endpoint,
                DefaultClientConfiguration.getDefaultGson());
        assertThat(resourceIterable.single().myField, is("theValue"));
        resourceIterable.next();
    }

    @Test
    public void shouldDeserialiseMultipleResponsesCollection() {
        thrown.expect(NoSuchElementException.class);
        JsonElement jsonObject = JSONPARSER.parse(multipleResponsesCollection);

        PageableResourceEndpoint<MyResponseResource> endpoint = withEndpoint();

        ResourceIterable<MyResponseResource> resourceIterable = new LazyLoadingResourceIterable<>(
                jsonObject, endpoint,
                DefaultClientConfiguration.getDefaultGson());
        assertThat(resourceIterable.next().myField, is("theValue"));
        assertThat(resourceIterable.next().myField, is("theOtherValue"));
        resourceIterable.next();
    }

    @Test
    public void shouldDeserialiseNoItems() {
        thrown.expect(NoSuchElementException.class);
        JsonElement jsonObject = JSONPARSER.parse(emptyResponsesCollection);

        PageableResourceEndpoint<MyResponseResource> endpoint = withEndpoint();

        ResourceIterable<MyResponseResource> resourceIterable = new LazyLoadingResourceIterable<>(
                jsonObject, endpoint,
                DefaultClientConfiguration.getDefaultGson());
        assertFalse(resourceIterable.hasNext());
        resourceIterable.next();
    }

    @Test
    public void shouldDeserialisedWithNoMeta() {
        JsonElement jsonObject = JSONPARSER.parse(noMetaResponsesCollection);

        PageableResourceEndpoint<MyResponseResource> endpoint = withEndpoint();

        ResourceIterable<MyResponseResource> resourceIterable = new LazyLoadingResourceIterable<>(
                jsonObject, endpoint,
                DefaultClientConfiguration.getDefaultGson());
        assertThat(resourceIterable.single().myField, is("theValue"));
    }

    private PageableResourceEndpoint<MyResponseResource> withEndpoint() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<MyResponseResource> endpoint = mock(
                PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(MyResponseResource.class);
        return endpoint;
    }

    private static class MyResponseResource extends Resource {
        @Key
        private String myField;
    }

}
