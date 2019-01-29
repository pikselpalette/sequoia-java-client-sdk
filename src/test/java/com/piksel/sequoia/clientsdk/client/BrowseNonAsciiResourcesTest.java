package com.piksel.sequoia.clientsdk.client;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 - 2019 Piksel
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

import com.piksel.sequoia.clientsdk.ResourceResponse;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.client.model.Content;
import com.piksel.sequoia.clientsdk.resource.DefaultResourceCriteria;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubGetMetadataResponse;
import static com.piksel.sequoia.clientsdk.criteria.Inclusion.resource;
import static com.piksel.sequoia.clientsdk.criteria.StringExpressionFactory.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrowseNonAsciiResourcesTest extends ClientIntegrationTestBase {

    @TestResource("content-with-non-ascii-chars.json")
    private String getContentWithNonAsciiChars;

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @Test
    public void whenBrowsingWithCriteria_returnedResourceWithNonAsciiCharsIsProperlyRead() {
        stubGetMetadataResponse(scenarioMappings, "contents", getContentWithNonAsciiChars, 200, "include=categories&withType=episode%7C%7Cmovie");

        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("type").equalTo("episode||movie"));
        criteria.include(resource("categories"));

        ResourceResponse<Content> response = client.service("metadata").resourcefulEndpoint("contents", Content.class).browse(criteria);
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/contents?include=categories&withType=episode%7C%7Cmovie")));

        verifyResponseStatusAndPayload(response);

        Content content = response.getPayload().get().next();

        assertEquals("owner:content-non-ascii", content.getRef().toString());
        assertEquals("Avrupa Haber Türkçe", content.getTitle());
        assertEquals("Programda, Avrupa'da yaşanan son gelişmeler, haberler ve değerlendirmeler ele alınıyor.", content.getLocalisedShortSynopsis().get("tr"));
    }

    private void verifyResponseStatusAndPayload(ResourceResponse<?> response) {
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getPayload().isPresent());
    }
}
