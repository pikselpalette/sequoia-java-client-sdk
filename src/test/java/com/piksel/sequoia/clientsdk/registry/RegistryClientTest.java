package com.piksel.sequoia.clientsdk.registry;

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
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForRegistryClientForNewOwner;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForRegistryClientWithServiceName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;

public class RegistryClientTest extends ClientIntegrationTestBase {

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @TestResource("registry-get-single-service.json")
    private String getSingleService;

    @TestResource("registry-response-testmock.json")
    private String registryTestmockResponse;

    @Test
    public void shouldProvideServiceList() {

        ResourceIterable<RegisteredService> services = client.registryClient().getServiceRegistryList();
        assertThat(services, not(nullValue()));
    }

    @Test
    public void shouldProvideServiceName() {
        stubForRegistryClientWithServiceName(scenarioMappings, getSingleService, 200, "identity");
        RegisteredService service = client.registryClient().getServiceRegistry("identity");
        assertThat(service.getLocation(), is("https://identity-reference.sequoia.piksel.com"));
        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/services/unitTestRoot/identity")));
    }

    @Test
    public void shouldRegisterAndProvideServiceForNewOwner() {
        stubForRegistryClientForNewOwner(scenarioMappings, registryTestmockResponse, 200, "testmock");
        ResourceIterable<RegisteredService> services = client.registryClient().getServiceRegistryListForOwner("testmock");
        assertThat("Collection of services retrieved should not be null", services, not(nullValue()));
        RegisteredService service = services.next();
        assertThat("Null registered service value", service, not(nullValue()));
        assertThat("Unexpected service location", service.getLocation(), equalTo("http://127.0.0.1:8119"));
    }
}
