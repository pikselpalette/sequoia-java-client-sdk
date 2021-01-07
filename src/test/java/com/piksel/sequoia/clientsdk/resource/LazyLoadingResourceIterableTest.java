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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.api.client.util.Key;
import com.google.gson.JsonParser;
import com.piksel.sequoia.clientsdk.configuration.DefaultClientConfiguration;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;

public class LazyLoadingResourceIterableTest {

    private static final JsonParser JSON_PARSER = new JsonParser();

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    private PageableResourceEndpoint<Resource> endpoint;

    @TestResource("response-single-resources.json")
    private String singleResponse;
    @TestResource("response-single-resources-with-single-linked.json")
    private String singleResponseWithDirectSingleLinked;
    @TestResource("response-resources-with-collection-linked.json")
    private String responseWithDirectCollectionLinked;
    @TestResource("response-resources-with-indirect-collection-linked.json")
    private String responseWithIndirectCollectionLinked;
    @TestResource("response-single-resources-with-collection-referenciated-without-linked-data.json")
    private String singleResponseWithDirectCollectionReferenciatedWithoutLinkedData;
    @TestResource("response-single-resource-with-next.json")
    private String singleResponseWithContinue;
    @TestResource("response-single-resource-with-continue-next.json")
    private String singleResponseWithContinuesNext;
    @TestResource("response-single-resource-from-other-page.json")
    private String responseFromOtherPage;
    @TestResource("response-collection-multiple-resources.json")
    private String multipleResponse;
    @TestResource("response-empty-resources.json")
    private String responseEmtpy;
    @TestResource("response-empty-content-with-pagination-links.json")
    private String responseWithEmptyContentAndPaginationLinks;

    @Before
    public void setUp() {
        endpoint = withEndpoint();
    }

    @Test(expected = NotSingularException.class)
    public void shouldThrowNotSingularExceptionWhenGettingSingleItemFromNonInitialPage() {
        iterableFor(responseFromOtherPage).single();
    }

    @Test(expected = NotSingularException.class)
    public void shouldThrowNotSingularExceptionWhenGettingSingleItemFromInitialPageWithMultiple() {
        iterableFor(multipleResponse).single();
    }

    @Test(expected = NotSingularException.class)
    public void shouldThrowNotSingularExceptionWhenGettingSingleItemFromInitialPageHavingContinue() {
        iterableFor(singleResponseWithContinue).single();
    }

    @Test
    public void shouldGetResourceWhenGettingSingleItemFromInitialPageWithSingleItemAndNoNextUrl() {
        assertThat(iterableFor(singleResponse).single(), is(notNullValue()));
    }

    @Test
    public void shouldGetResourceWithDirectSingleLinkedResource() {
        final ResourceWithDirect instanceWithLinked = new LazyLoadingResourceIterable<>(
                JSON_PARSER.parse(singleResponseWithDirectSingleLinked), withSingleLinkedEndpoint(),
                DefaultClientConfiguration.getDefaultGson()).single();
        assertThat(instanceWithLinked, is(notNullValue()));
        assertThat(instanceWithLinked.someLinkedType, is(notNullValue()));
        assertTrue(instanceWithLinked.someLinkedType.isPresent());
    }

    @Test
    public void shouldGetResourceWithDirectWithErrorInLinkedRef() {
        final ResourceWithDirectWithError instanceWithLinked = new LazyLoadingResourceIterable<>(
                JSON_PARSER.parse(singleResponseWithDirectSingleLinked), withErrorInLinkedRef(),
                DefaultClientConfiguration.getDefaultGson()).single();
        assertThat(instanceWithLinked, is(notNullValue()));
        assertThat(instanceWithLinked.someLinkedType, is(notNullValue()));
        assertFalse(instanceWithLinked.someLinkedType.isPresent());
    }

    @Test
    public void shouldGetResourceWithDirectCollectionLinkedResources() {
        final LazyLoadingResourceIterable<ResourceWithDirectCollection> lazyLoadingResourceIterable = new LazyLoadingResourceIterable<>(
                JSON_PARSER.parse(responseWithDirectCollectionLinked),
                withCollectionLinkedEndpoint(), DefaultClientConfiguration.getDefaultGson());

        ResourceWithDirectCollection resource1 = lazyLoadingResourceIterable.next();
        assertThat(resource1, is(notNullValue()));
        assertThat(resource1.getName(), is("56b9bb7d8e4aa6e54b874843"));
        assertThat(resource1.someLinkedType, is(notNullValue()));
        assertThat(resource1.someLinkedType, is(not(empty())));
        assertThat(resource1.someLinkedType.get(0).isPresent(), is(true));
        assertThat(resource1.someLinkedType.get(0).get().someField, is("someValue1"));
        assertThat(resource1.someLinkedType.get(1).isPresent(), is(true));
        assertThat(resource1.someLinkedType.get(1).get().someField, is("someValue2"));

        ResourceWithDirectCollection resource2 = lazyLoadingResourceIterable.next();
        assertThat(resource2, is(notNullValue()));
        assertThat(resource2.getName(), is("56b9bb7d8e4aa6e54b874844"));
        assertThat(resource2.someLinkedType, is(notNullValue()));
        assertThat(resource2.someLinkedType, is(not(empty())));
        assertThat(resource2.someLinkedType.get(0).isPresent(), is(true));
        assertThat(resource2.someLinkedType.get(0).get().someField, is("someValue3"));
    }

    @Test
    public void shouldGetResourceWithDirectCollectionReferencedWithoutLinkedData() {
        final ResourceWithDirectCollection instanceWithLinked = new LazyLoadingResourceIterable<>(
                JSON_PARSER.parse(singleResponseWithDirectCollectionReferenciatedWithoutLinkedData),
                withCollectionLinkedEndpoint(), DefaultClientConfiguration.getDefaultGson())
                        .single();
        assertThat(instanceWithLinked, is(notNullValue()));
        instanceWithLinked.someLinkedType
                .forEach((Optional<?> optional) -> assertFalse(optional.isPresent()));
    }

    @Test
    public void shouldGetResourceWithIndirectCollectionLinkedResources() {
        final LazyLoadingResourceIterable<ResourceWithIndirectCollection> lazyLoadingResourceIterable = new LazyLoadingResourceIterable<>(
                JSON_PARSER.parse(responseWithIndirectCollectionLinked),
                withIndirectCollectionLinkedEndpoint(),
                DefaultClientConfiguration.getDefaultGson());

        ResourceWithIndirectCollection resource1 = lazyLoadingResourceIterable.next();
        assertThat(resource1, is(notNullValue()));
        assertThat(resource1.getName(), is("56b9bb7d8e4aa6e54b874843"));
        assertThat(resource1.anotherLinkedType, is(notNullValue()));
        assertThat(resource1.anotherLinkedType, is(not(empty())));
        assertThat(resource1.anotherLinkedType.get(0).isPresent(), is(true));
        assertEquals(resource1.anotherLinkedType.get(0).get().someResourceRef,
                "test:56b9bb7d8e4aa6e54b874843");

        ResourceWithIndirectCollection resource2 = lazyLoadingResourceIterable.next();
        assertThat(resource2, is(notNullValue()));
        assertThat(resource2.getName(), is("56b9bb7d8e4aa6e54b874844"));
        assertThat(resource2.anotherLinkedType, is(notNullValue()));
        assertThat(resource2.anotherLinkedType, is(not(empty())));
        assertThat(resource2.anotherLinkedType.get(0).isPresent(), is(true));
        assertEquals(resource2.anotherLinkedType.get(0).get().someResourceRef,
                "test:56b9bb7d8e4aa6e54b874844");
        assertThat(resource2.anotherLinkedType.get(1).isPresent(), is(true));
        assertEquals(resource2.anotherLinkedType.get(1).get().someResourceRef,
                "test:56b9bb7d8e4aa6e54b874844");
        assertNotEquals(resource2.anotherLinkedType.get(0).get(),
                resource2.anotherLinkedType.get(1).get());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementWhenNextIsInvokedOnLastPageAfterLastElement() {
        LazyLoadingResourceIterable<Resource> iterable = iterableFor(multipleResponse);
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.next(), is(notNullValue()));
        iterable.next();
    }

    @Test
    public void shouldReturnTrueWhenHasNextIsInvokedAfterLastElementOfPageWithNextUrl() {
        when(endpoint.getPagedResource(anyString(), anyMap()))
                .thenReturn(Optional.of(JSON_PARSER.parse(responseFromOtherPage)));

        LazyLoadingResourceIterable<Resource> iterable = iterableFor(singleResponseWithContinue);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.totalCount().get(), is(10));
    }

    @Test
    public void shouldReturnTrueWhenHasNextIsInvokedAfterLastElementOfPageWithContinueUrl() {
        when(endpoint.getPagedResource(anyString(), anyMap()))
                .thenReturn(Optional.of(JSON_PARSER.parse(responseFromOtherPage)));
        LazyLoadingResourceIterable<Resource> iterable = iterableFor(
                singleResponseWithContinuesNext);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(true));
    }

    @Test
    public void shouldLoadNextPageWhenNextIsInvokedAfterLastElementOfPageWithNextUrl() {
        when(endpoint.getPagedResource(anyString(), anyMap()))
                .thenReturn(Optional.of(JSON_PARSER.parse(responseFromOtherPage)));

        LazyLoadingResourceIterable<Resource> iterable = iterableFor(singleResponseWithContinue);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(false));

        verify(endpoint).getPagedResource(anyString(), anyMap());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionWhenLoadingNextPageButNoContentIsReturned() {
        when(endpoint.getPagedResource(anyString(), anyMap())).thenReturn(Optional.empty());

        LazyLoadingResourceIterable<Resource> iterable = iterableFor(singleResponseWithContinue);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(true));
        iterable.next();
    }

    @Test
    public void shouldReturnDoesnotHaveNextPageWhenPageIsEmpty() {
        when(endpoint.getPagedResource(anyString(), anyMap()))
                .thenReturn(Optional.of(JSON_PARSER.parse(responseEmtpy)));

        LazyLoadingResourceIterable<Resource> iterable = iterableFor(singleResponseWithContinue);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(false));
    }

    @Test
    public void givenResponseWithNextPageLink_whenNoContent_shouldNotIteratePages() {
        LazyLoadingResourceIterable<Resource> iterable = iterableFor(
                responseWithEmptyContentAndPaginationLinks);

        assertThat(iterable.hasNext(), is(false));
    }

    @Test
    public void shouldIterateWithContinueUrl() {
        when(endpoint.getPagedResource(anyString(), anyMap()))
                .thenReturn(Optional.of(JSON_PARSER.parse(responseEmtpy)));
        LazyLoadingResourceIterable<Resource> iterable = iterableFor(
                singleResponseWithContinuesNext);

        assertThat(iterable.hasNext(), is(true));
        assertThat(iterable.next(), is(notNullValue()));
        assertThat(iterable.hasNext(), is(false));
    }

    private LazyLoadingResourceIterable<Resource> iterableFor(String json) {
        return new LazyLoadingResourceIterable<>(JSON_PARSER.parse(json), endpoint,
                DefaultClientConfiguration.getDefaultGson());
    }

    private PageableResourceEndpoint<Resource> withEndpoint() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<Resource> endpoint = mock(PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(Resource.class);
        return endpoint;
    }

    private PageableResourceEndpoint<ResourceWithDirect> withSingleLinkedEndpoint() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<ResourceWithDirect> endpoint = mock(
                PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(ResourceWithDirect.class);
        return endpoint;
    }

    private PageableResourceEndpoint<ResourceWithDirectWithError> withErrorInLinkedRef() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<ResourceWithDirectWithError> endpoint = mock(
                PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(ResourceWithDirectWithError.class);
        return endpoint;
    }

    private PageableResourceEndpoint<ResourceWithDirectCollection> withCollectionLinkedEndpoint() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<ResourceWithDirectCollection> endpoint = mock(
                PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(ResourceWithDirectCollection.class);
        return endpoint;
    }

    private PageableResourceEndpoint<ResourceWithIndirectCollection> withIndirectCollectionLinkedEndpoint() {
        @SuppressWarnings("unchecked")
        PageableResourceEndpoint<ResourceWithIndirectCollection> endpoint = mock(
                PageableResourceEndpoint.class);
        when(endpoint.getResourceKey()).thenReturn("someResources");
        when(endpoint.getEndpointType()).thenReturn(ResourceWithIndirectCollection.class);
        return endpoint;
    }

    public static class ResourceWithDirect extends Resource {

        @Key
        private String someLinkedTypeRef;

        @DirectRelationship(ref = "someLinkedTypeRef", relationship = "someLinkedType")
        private Optional<SomeLinkedType> someLinkedType;

    }

    public static class ResourceWithDirectWithError extends Resource {

        @Key
        private String someLinkedTypeRef;

        @DirectRelationship(ref = "nonExistingField", relationship = "someLinkedType")
        private Optional<SomeLinkedType> someLinkedType;

    }

    public static class ResourceWithDirectCollection extends Resource {

        @Key
        private List<String> someLinkedTypeRefs;

        @DirectRelationship(ref = "someLinkedTypeRefs", relationship = "someLinkedType")
        private List<Optional<SomeLinkedType>> someLinkedType;

    }

    public static class SomeLinkedType extends Resource {

        @Key
        String someField;
    }

    public static class ResourceWithIndirectCollection extends Resource {

        @IndirectRelationship(ref = "someResourceRef", relationship = "someLinkedType")
        private List<Optional<AnotherLinkedType>> anotherLinkedType;

    }

    public static class AnotherLinkedType extends Resource {

        @Key
        String someResourceRef;

        @DirectRelationship(ref = "someResourceRef", relationship = "someLinkedType")
        Optional<ResourceWithIndirectCollection> resourceWithIndirectCollection;
    }

}
