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

import static com.piksel.sequoia.clientsdk.resource.MetadataLockValue.MetadataLockActionValue.LOCK;
import static com.piksel.sequoia.clientsdk.resource.MetadataLockValue.MetadataLockActionValue.OVERRIDE;
import static com.piksel.sequoia.clientsdk.resource.MetadataLockValue.MetadataLockActionValue.UNLOCK;
import static com.piksel.sequoia.clientsdk.resource.MetadataLockValue.MetadataLockStatusValue.LOCKED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.Map;
import org.hamcrest.collection.IsMapContaining;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import com.google.api.client.util.Maps;
import com.piksel.sequoia.clientsdk.model.ModelResourceTestBase;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.validation.PutValidation;

public class ResourceTest extends ModelResourceTestBase {

    @TestResource("resource-complete.json")
    String resource;

    @TestResource("resource-complete-with-float.json")
    String resourceWithFloat;

    @TestResource("resource-complete-with-extra-data.json")
    String resourceWithExtraData;

    @TestResource("resource-noowner.json")
    String resourceNoOwner;

    @TestResource("resource-noversion.json")
    String resourceNoVersion;

    @TestResource("resource-metadata-complete.json")
    String resourceMetadataComplete;

    @TestResource("resource-metadata-complete-with-extra-data.json")
    String resourceMetadataCompleteWithExtraData;

    @Test
    public void givenCompleteResource_shouldNotContainViolations() {
        Resource res = parse(resource, Resource.class);

        assertTrue(res.isValid().isEmpty());
    }

    @Test
    public void givenCompleteResourceWithFloat_shouldNotContainViolations() {
        Resource res = parse(resourceWithFloat, Resource.class);

        assertTrue(res.isValid().isEmpty());
        assertThat(res.getCustom().get("floatValue"), is(29.97));
        assertThat(res.getCustom().get("numberValue"), is(34));


    }

    @Test
    public void givenCompleteResource_shouldReadMetadataField() throws JSONException {
        Resource res = parse(resource, Resource.class);

        assertNotNull(res.getMetadata().getLockActions());
        assertNotNull(res.getMetadata().getLockStatuses());

        MetadataLockField actions = res.getMetadata().getLockActions();
        assertEquals(LOCK, actions.getLockValueForField("title"));
        assertEquals(LOCK, actions.getLockValueForField("custom.stringValue"));
        assertNull(actions.getLockValueForField("custom.boolValue"));

        MetadataLockField statuses = res.getMetadata().getLockStatuses();
        assertEquals(LOCKED, statuses.getLockValueForField("custom.numberValue"));
        assertNull(statuses.getLockValueForField("custom.boolValue"));

        String responseJson = serialize(res);
        JSONAssert.assertEquals(resourceWithExtraData, responseJson, JSONCompareMode.STRICT);
    }

    @Test
    public void givenJsonWithEmptyMetadata_shouldGiveEmptyMetadata() throws JSONException {
        final String originalResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\",\"metadata\":{}}";
        final String expectedResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\"}";
        Resource res = parse(originalResourceJson, Resource.class);

        assertNotNull(res.getMetadata());
        assertNotNull(res.getMetadata().getLockActions());
        assertNotNull(res.getMetadata().getLockStatuses());
        assertEquals(0, res.getMetadata().getLockActions().getLockValues().size());
        assertEquals(0, res.getMetadata().getLockStatuses().getLockValues().size());

        String responseJson = serialize(res);
        JSONAssert.assertEquals(expectedResourceJson, responseJson, JSONCompareMode.STRICT);
    }

    @Test
    public void givenJsonWithEmptyMetadataFields_shouldGiveEmptyMetadata() throws JSONException {
        final String originalResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\",\"metadata\":{\"fields\":{}}}";
        final String expectedResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\"}";
        Resource res = parse(originalResourceJson, Resource.class);

        assertNotNull(res.getMetadata());
        assertNull(res.getMetadata().getLockActions());
        assertNull(res.getMetadata().getLockStatuses());

        String responseJson = serialize(res);
        JSONAssert.assertEquals(expectedResourceJson, responseJson, JSONCompareMode.STRICT);
    }

    @Test
    public void givenJsonWithInvalidMetadataFields_shouldGiveEmptyMetadata() throws JSONException {
        final String originalResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\",\"metadata\":{\"fields\":{\"invalidField\":\"invalidVAlue\"}}}";
        final String expectedResourceJson = "{\"ref\":\"test:56b9bb7d8e4aa6e54b874844\",\"owner\":\"test\"}";
        Resource res = parse(originalResourceJson, Resource.class);

        assertNotNull(res.getMetadata());
        assertNull(res.getMetadata().getLockActions());
        assertNull(res.getMetadata().getLockStatuses());

        String responseJson = serialize(res);
        JSONAssert.assertEquals(expectedResourceJson, responseJson, JSONCompareMode.STRICT);
    }
    @Test
    public void givenJsonWithValidMetadata_shouldGiveValidMetadataObject() throws JSONException {
        Resource res = parse(resourceMetadataCompleteWithExtraData, Resource.class);

        assertNotNull(res.getMetadata());
        assertNotNull(res.getMetadata().getLockActions());
        assertNotNull(res.getMetadata().getLockStatuses());
        assertEquals(2, res.getMetadata().getLockStatuses().getLockValues().size());
        assertEquals(3, res.getMetadata().getLockActions().getLockValues().size());

        Map<String, MetadataLockValue> statuses = res.getMetadata().getLockStatuses().getLockValues();
        assertThat(statuses, IsMapContaining.hasEntry("path.to.field.a", LOCKED));
        assertThat(statuses, IsMapContaining.hasEntry("path.to.field.b", LOCKED));
        Map<String, MetadataLockValue> actions = res.getMetadata().getLockActions().getLockValues();
        assertThat(actions, IsMapContaining.hasEntry("path.to.field.a", LOCK));
        assertThat(actions, IsMapContaining.hasEntry("path.to.field.b", UNLOCK));
        assertThat(actions, IsMapContaining.hasEntry("path.to.field.c", OVERRIDE));

        String responseJson = serialize(res);
        JSONAssert.assertEquals(resourceMetadataComplete, responseJson, JSONCompareMode.STRICT);
    }

    @Test
    public void givenNoOwnerResource_shouldContainViolationForMissingOwner() {
        Resource res = parse(resourceNoOwner, Resource.class);

        assertFalse(res.isValid().isEmpty());
        assertThat(res.isValid().size(), is(1));
    }

    @Test
    public void givenNoVersionResource_shouldContainViolationForMissingVersionForPutAndNotWithStandardValidation() {
        Resource res = parse(resourceNoVersion, Resource.class);

        assertFalse(res.isValid(PutValidation.class).isEmpty());
        assertThat(res.isValid(PutValidation.class).size(), is(1));
        assertTrue(res.isValid().isEmpty());
    }

    @Test
    public void whenCreatingResource_shouldBePossibleToAddCommonsFields() {
        Resource resource = new Resource();
        resource.setTags(Collections.singletonList("tag1"));
        Map<String, String> alternativeIdentifiers = Maps.newHashMap();
        alternativeIdentifiers.put("alternative", "identifiers");
        resource.setAlternativeIdentifiers(alternativeIdentifiers);
        resource.setCustom(createCustomMap());
        assertThat(resource.getTags().iterator().next(), is("tag1"));
        assertThat(resource.getAlternativeIdentifiers().get("alternative"),
                is("identifiers"));
        assertThat(resource.getCustom().get("string"), is("String"));
        assertThat(
                ((ResourceList) resource.getCustom().get("collection")).size(),
                is(3));

    }

    private ResourceMap createCustomMap() {
        ResourceList resourceList = new ResourceList();
        resourceList.add(true);
        resourceList.add("String");
        // resourceList.add(Instant.now());
        resourceList.add(1);
        ResourceMap customMap = new ResourceMap();
        customMap.put("string", "String");
        customMap.put("integer", 1);
        customMap.put("boolean", true);
        // customMap.put("datetime", Instant.now());
        customMap.put("collection", resourceList);
        return customMap;
    }

}
