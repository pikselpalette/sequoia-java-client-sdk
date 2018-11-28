package com.piksel.sequoia.clientsdk.client;

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

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.collect.Lists.newArrayList;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubGetMetadataResponse;
import static com.piksel.sequoia.clientsdk.criteria.Inclusion.resource;
import static com.piksel.sequoia.clientsdk.criteria.StringExpressionFactory.field;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import com.piksel.sequoia.clientsdk.criteria.Inclusion;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.api.client.util.Lists;
import com.piksel.sequoia.clientsdk.ResourceResponse;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.client.model.Asset;
import com.piksel.sequoia.clientsdk.client.model.Category;
import com.piksel.sequoia.clientsdk.client.model.Content;
import com.piksel.sequoia.clientsdk.client.model.Job;
import com.piksel.sequoia.clientsdk.client.model.Offer;
import com.piksel.sequoia.clientsdk.resource.DefaultResourceCriteria;
import com.piksel.sequoia.clientsdk.resource.IncludeResourceException;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for include (direct and indirect).
 */
@Slf4j
public class SequoiaClientIncludeTest extends ClientIntegrationTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @TestResource("content-with-multiple-linked-assets.json")
    private String getContentWithMultipleAssets;

    @TestResource("content-with-more-than-1page-linked-assets.json")
    private String getContentWithMultiplePageAssets;

    @TestResource("linked-assets-secondpage.json")
    private String secondPageAssets;

    @TestResource("linked-offers-secondpage.json")
    private String secondPageOffers;

    @TestResource("content-with-multiple-linked-assets-withoutref.json")
    private String getContentWithMultipleAssetsWithoutRef;

    @TestResource("content-with-multiple-linked-assets-withoutrefinlinked.json")
    private String getContentWithMultipleAssetsWithoutRefInLinked;

    @TestResource("asset-with-single-linked-content.json")
    private String getAssetWithSingleContent;

    @TestResource("asset-with-missing-linked-content.json")
    private String getAssetWithMissingContent;

    @TestResource("content-with-linked-categories-error.json")
    private String getContentWithCategoriesError;

    @TestResource("content-with-linked-categories-notfound.json")
    private String getContentWithCategoriesNotFound;

    @TestResource("offers-with-linked-locations-some-without-statusCode.json")
    private String getOffersWithLinkedNotFound;

    @TestResource("content-with-multiple-linked-categories.json")
    private String getContentWithMultipleCategories;

    @TestResource("jobs-with-events.json")
    private String bigPayload;

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnSingleDirectLinkedItemAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "assets", getAssetWithSingleContent, 200, "include=contents&withReference=owner:asset1");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("owner:asset1"));
        criteria.include(resource("contents"));

        ResourceResponse<Asset> response = client.service("metadata").resourcefulEndpoint("assets", Asset.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/assets?include=contents&withReference=owner:asset1")));

        verifyResponseStatusAndPayload(response);

        Asset asset = response.getPayload().get().next();

        assertEquals("owner:asset1", asset.getRef().toString());

        Content content = asset.getContent().get();
        assertEquals("owner:content1", content.getRef().toString());
        assertEquals("content 1", content.getTitle());
        assertEquals("movie", content.getType());

        Content directContent = asset.getDirectContent();
        assertEquals("owner:content1", directContent.getRef().toString());
        assertEquals("content 1", directContent.getTitle());
        assertEquals("movie", directContent.getType());
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnMultipleDirectLinkedItemAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithMultipleCategories, 200, "include=categories");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("categories"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/contents?include=categories")));

        verifyResponseStatusAndPayload(response);

        // 2 existing categories
        Content content = response.getPayload().get().next();
        assertEquals("owner:content1", content.getRef().toString());
        assertEquals(2, content.getCategoriesCollection().size());
        assertEquals(2, content.getCategories().size());
        assertEquals(0, content.getAssets().size());
        assertEquals(0, content.getAssetsCollection().size());

        Category category;
        Iterator<Optional<Category>> categoryList = content.getCategories().iterator();

        category = categoryList.next().get();
        assertEquals("owner:category1", category.getRef().toString());
        assertEquals("Category 1", category.getTitle());
        assertEquals("Category 1", category.getValue());
        assertEquals("genre", category.getScheme());

        category = categoryList.next().get();
        assertEquals("owner:category2", category.getRef().toString());
        assertEquals("Category 2", category.getTitle());
        assertEquals("Category 2", category.getValue());
        assertEquals("genre", category.getScheme());

        Iterator<Category> categoryListCollection = content.getCategoriesCollection().iterator();

        category = categoryListCollection.next();
        assertEquals("owner:category1", category.getRef().toString());
        assertEquals("Category 1", category.getTitle());
        assertEquals("Category 1", category.getValue());
        assertEquals("genre", category.getScheme());

        category = categoryListCollection.next();
        assertEquals("owner:category2", category.getRef().toString());
        assertEquals("Category 2", category.getTitle());
        assertEquals("Category 2", category.getValue());
        assertEquals("genre", category.getScheme());

        // 1 existing + 1 missing category
        content = response.getPayload().get().next();
        assertEquals("owner:content2", content.getRef().toString());
        assertEquals(2, content.getCategories().size());
        Iterator<Optional<Category>> categListIt = content.getCategories().iterator();

        Optional<Category> categ = categListIt.next();
        assertEquals(false, categ.isPresent());

        categ = categListIt.next();
        assertEquals("Category 1", categ.get().getTitle());

        assertEquals(1, content.getCategoriesCollection().size());
        assertEquals("Category 1", content.getCategoriesCollection().iterator().next().getTitle());

        // empty ref. list
        content = response.getPayload().get().next();
        assertEquals("owner:content3", content.getRef().toString());
        assertEquals(0, content.getCategories().size());
        assertEquals(0, content.getCategoriesCollection().size());

        // 1 missing category
        content = response.getPayload().get().next();
        assertEquals("owner:content4", content.getRef().toString());
        assertEquals(1, content.getCategories().size());
        assertEquals(0, content.getCategoriesCollection().size());

    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnMissingDirectLinkedItemAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "assets", getAssetWithMissingContent, 200, "include=contents&withReference=owner:asset1");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("owner:asset1"));
        criteria.include(resource("contents"));

        ResourceResponse<Asset> response = client.service("metadata").resourcefulEndpoint("assets", Asset.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/assets?include=contents&withReference=owner:asset1")));

        verifyResponseStatusAndPayload(response);

        Asset asset = response.getPayload().get().single();
        assertEquals("owner:asset1", asset.getRef().toString());

        assertEquals(false, asset.getContent().isPresent());
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnBigPayload() {

        stubGetMetadataResponse(scenarioMappings, "jobs", bigPayload, 200, "include=events");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("events"));
        ResourceResponse<Job> response = client.service("metadata").resourcefulEndpoint("jobs", Job.class).browse(criteria);
        log.debug("Big payload {} ", getJobsEvents(response));

        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/jobs?include=events")));
        verifyResponseStatusAndPayload(response);
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnMultipleIndirectLinkedItemsAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithMultiplePageAssets, 200,
                "include=assets,offers&offers.fields=ref,name,contentRefs&withReference=test:contentsToChecktesting1");
        stubGetMetadataResponse(scenarioMappings, "assets", secondPageAssets, 200,
                "fields=ref,name,contentRef,type,mediaType,url,fileFormat,title,fileSize,tags&count=true&withContentRef=test:contentsToChecktesting1&page=2&perPage=100");
        stubGetMetadataResponse(scenarioMappings, "offers", secondPageOffers, 200,
                "fields=ref,name,title,contentRefs&count=true&withContentRefs=test:contentsToChecktesting1&page=2&perPage=100");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("test:contentsToChecktesting1"));
        criteria.include(resource("assets"), resource("offers").fields("ref", "name", "contentRefs"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(
                urlEqualTo("/data/contents?include=assets,offers&offers.fields=ref,name,contentRefs&withReference=test:contentsToChecktesting1")));

        verifyResponseStatusAndPayload(response);

        Content content = response.getPayload().get().next();
        Collection<Asset> assets = newArrayList(content.getAssetsPagination());
        Collection<Offer> offers = newArrayList(content.getOffers());

        int numAssets = 101;
        int numOffers = 101;
        assertThat(assets.size(), is(numAssets));
        assertAssetNames(assets, numAssets);
        assertOfferNames(offers, numOffers);

        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo(
                "/data/assets?fields=ref,name,contentRef,type,mediaType,url,fileFormat,title,fileSize,tags&count=true&withContentRef=test:contentsToChecktesting1&page=2&perPage=100")));
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo(
                "/data/offers?fields=ref,name,title,contentRefs&count=true&withContentRefs=test:contentsToChecktesting1&page=2&perPage=100")));

    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnMultipleIndirectLinkedItemsAndIterateThemAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithMultipleAssets, 200, "include=assets&withReference=owner:content1");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("owner:content1"));
        criteria.include(resource("assets"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/contents?include=assets&withReference=owner:content1")));

        verifyResponseStatusAndPayload(response);

        // 2 existing categories
        Content content = response.getPayload().get().next();
        assertEquals("owner:content1", content.getRef().toString());
        assertEquals(2, content.getAssets().size());
        assertEquals(2, content.getAssetsCollection().size());
        assertEquals(2, content.getCategories().size());
        Iterator<Optional<Category>> categ = content.getCategories().iterator();
        assertEquals(false, categ.next().isPresent());
        assertEquals(false, categ.next().isPresent());
        assertEquals(0, content.getCategoriesCollection().size());

        Asset asset;
        Iterator<Optional<Asset>> assetList = content.getAssets().iterator();

        asset = assetList.next().get();
        assertEquals("owner:asset1", asset.getRef().toString());
        assertEquals("video", asset.getMediaType());
        assertEquals("url1", asset.getUrl());
        assertEquals("owner:content1", asset.getContentRef());

        asset = assetList.next().get();
        assertEquals("owner:asset2", asset.getRef().toString());
        assertEquals("image", asset.getMediaType());
        assertEquals("url2", asset.getUrl());
        assertEquals("owner:content1", asset.getContentRef());

        Iterator<Asset> assetListCollection = content.getAssetsCollection().iterator();

        asset = assetListCollection.next();
        assertEquals("owner:asset1", asset.getRef().toString());
        assertEquals("video", asset.getMediaType());
        assertEquals("url1", asset.getUrl());
        assertEquals("owner:content1", asset.getContentRef());

        asset = assetListCollection.next();
        assertEquals("owner:asset2", asset.getRef().toString());
        assertEquals("image", asset.getMediaType());
        assertEquals("url2", asset.getUrl());
        assertEquals("owner:content1", asset.getContentRef());

        // content without linked assets
        content = response.getPayload().get().next();
        assertEquals("owner:content2", content.getRef().toString());
        assertEquals(0, content.getAssets().size());
        assertEquals(0, content.getAssetsCollection().size());
        assertEquals(0, content.getCategories().size());
        assertEquals(0, content.getCategoriesCollection().size());

    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_withoutRef_returnEmptyCollectionOfItemsAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithMultipleAssetsWithoutRef, 200,
                "include=assets&withReference=owner:content1");
        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("owner:content1"));
        criteria.include(resource("assets"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);

        Content content = response.getPayload().get().single();
        assertTrue(content.getAssetsCollection().isEmpty());
        assertTrue(content.getAssets().isEmpty());

    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_withoutRefInsideOfLinked_returnEmptyCollectionOfItemsAndReturn200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithMultipleAssetsWithoutRefInLinked, 200,
                "include=assets,offers&withReference=owner:content1");
        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("reference").equalTo("owner:content1"));
        criteria.include(resource("assets"), resource("offers"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);

        Content content = response.getPayload().get().single();
        assertTrue(content.getAssetsCollection().isEmpty());
        assertTrue(content.getAssets().isEmpty());
        assertTrue(content.getOffersCollection().isEmpty());
        assertTrue(Lists.newArrayList(content.getOffers()).isEmpty());

    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnEmptyListOfCategoriesAndErrorsInMetaAndReturns200() {
        thrown.expect(IncludeResourceException.class);
        thrown.expectMessage("fields is invalid - channelRef is not allowed");

        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithCategoriesError, 200, "include=categories");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("categories"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/contents?include=categories")));

        verifyResponseStatusAndPayload(response);
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnEmptyListOfCategoriesAnd404ErrorsInMetaAndReturns200() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithCategoriesNotFound, 200, "include=categories");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("categories"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/contents?include=categories")));

        Content content = response.getPayload().get().single();
        assertTrue(content.getCategoriesCollection().isEmpty());
        assertFalse(content.getCategories().get(0).isPresent());

        verifyResponseStatusAndPayload(response);
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_returnLinkedLocationsSomeWithStatusCodeAndSomeWithout() {
        stubGetMetadataResponse(scenarioMappings, "offers", getOffersWithLinkedNotFound, 200, "include=scopeContents,descriptiveContent,locations");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("scopeContents"), resource("descriptiveContent"), resource("locations"));

        ResourceResponse<Offer> response = client.service("metadata").resourcefulEndpoint("offers", Offer.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/offers?include=scopeContents,descriptiveContent,locations")));

        Offer offer = response.getPayload().get().single();
        assertThat(offer.getLocations().size(), is(1));

        verifyResponseStatusAndPayload(response);
    }


    private void verifyResponseStatusAndPayload(ResourceResponse<?> response) {
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getPayload().isPresent());
    }

    private JobsEvents getJobsEvents(ResourceResponse<Job> response) {
        JobsEvents jobsEvents = new JobsEvents();
        response.getPayload().get().forEachRemaining(job -> {
            jobsEvents.addNumJobs(1);
            jobsEvents.addNumEvents(job.getEvents().size());
        });
        return jobsEvents;
    }

    private void assertAssetNames(Collection<Asset> assets, int numAssets) {
        Collection<String> assetsName = newArrayList();
        for (Asset asset : assets) {
            assertThat(asset.getContentRef(), is("test:contentsToChecktesting1"));
            assetsName.add(asset.getName());
        }
        assertTrue(assetsName.stream().distinct().limit(numAssets).count() == numAssets);
    }

    private void assertOfferNames(Collection<Offer> offers, int numOffers) {
        Collection<String> offersName = newArrayList();
        for (Offer offer : offers) {
            assertThat(offer.getContentRefs(), hasItem("test:contentsToChecktesting1"));
            offersName.add(offer.getName());
        }
        assertTrue(offersName.stream().distinct().limit(numOffers).count() == numOffers);
    }

    @ToString
    @NoArgsConstructor
    static class JobsEvents {

        int numJobs = 0;
        int numEvents = 0;

        public void addNumJobs(int jobs) {
            numJobs += jobs;
        }

        public void addNumEvents(int events) {
            numEvents += events;
        }

    }

}
