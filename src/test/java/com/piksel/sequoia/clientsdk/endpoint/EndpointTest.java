package com.piksel.sequoia.clientsdk.endpoint;

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

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForGet;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForPost;
import static com.piksel.sequoia.clientsdk.endpoint.Endpoints.withHeaders;
import static com.piksel.sequoia.clientsdk.endpoint.Endpoints.withParams;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.piksel.sequoia.clientsdk.RequestExecutionException;
import com.piksel.sequoia.clientsdk.Response;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.endpoint.headers.RequestHeaders;
import com.piksel.sequoia.clientsdk.registry.RegistryClient;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;

public class EndpointTest extends ClientIntegrationTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @TestResource("permission-response.json")
    private String permission;

    @TestResource("permission-response-filtered.json")
    private String permissionFiltered;

    @TestResource("password.json")
    private String password;

    @Test
    public void shouldProvideAccessToRegistryClient() {
        RegistryClient registryClient = client.registryClient();
        assertNotNull(registryClient);
    }

    @Test
    public void endpoint_shouldProvideOneBussinessEndpoint() {
        Endpoint<PauthAccess, Password> resourceEndpoint = client.service("identity").endpoint("pauth/access", PauthAccess.class);
        assertNotNull(resourceEndpoint);
    }

    @Test
    public void whenPerformingGet_andReturn200() {
        stubForGet(scenarioMappings, permission, 200, "/pauth/access");

        Response<PauthAccess> response = client.service("identity").endpoint("pauth/access", PauthAccess.class).get();

        commonVerifications();
        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(200, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(true));
        assertThat(response.getPayload().get().getUserRef(), is("test:test-admin"));

    }

    @Test
    public void whenPerformingGet_withHeaders_andReturn200() {
        RequestHeaders headers = withHeaders("header1", "value1").add("header2", 2);
        stubForGet(scenarioMappings, permission, 200, "/pauth/access", headers.getHeaders());

        Response<PauthAccess> response = client.service("identity").endpoint("pauth/access", PauthAccess.class).get(headers);

        scenarioMappings.verify("identity", getRequestedFor(urlEqualTo("/pauth/access")).withHeader("header1", equalTo("value1")));
        scenarioMappings.verify("identity", getRequestedFor(urlEqualTo("/pauth/access")).withHeader("header2", equalTo("2")));
        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(200, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(true));
        assertThat(response.getPayload().get().getUserRef(), is("test:test-admin"));

    }

    @Test
    public void whenPerformingGet_andReturn404() {
        stubForGet(scenarioMappings, "", 404, "/pauth/access");

        Response<PauthAccess> response = client.service("identity").endpoint("pauth/access", PauthAccess.class).get();

        assertThat(response.isSuccessStatusCode(), is(false));
        assertEquals(404, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(false));
        assertThat(response.getPayload(), is(Optional.empty()));
    }

    @Test
    public void whenPerformingGet_addingParams_andReturn200() {
        stubForGet(scenarioMappings, permissionFiltered, 200, "/pauth/access?actions=identity:tenants:read,identity:tenants:write&tenants=root,test");

        Response<PauthAccess> response = client.service("identity").endpoint("pauth/access", PauthAccess.class)
                .get(withParams("tenants", "root,test").add("actions", "identity:tenants:read,identity:tenants:write"));

        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(200, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(true));
        assertThat(response.getPayload().get().getPermissions().size(), is(2));
        assertThat(response.getPayload().get().getPermissions().iterator().next().getTenant(), is("root"));

    }

    @Test
    public void whenPerformingGet_addingParamsAndHeaders_andReturn200() {
        RequestHeaders headers = withHeaders("header1", "value1").add("header2", 2);
        stubForGet(scenarioMappings, permission, 200, "/pauth/access", headers.getHeaders());
        stubForGet(scenarioMappings, permissionFiltered, 200, "/pauth/access?actions=identity:tenants:read,identity:tenants:write&tenants=root,test");

        Response<PauthAccess> response = client.service("identity").endpoint("pauth/access", PauthAccess.class)
                .get(withParams("tenants", "root,test").add("actions", "identity:tenants:read,identity:tenants:write"), headers);

        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(200, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(true));
        assertThat(response.getPayload().get().getPermissions().size(), is(2));
        assertThat(response.getPayload().get().getPermissions().iterator().next().getTenant(), is("root"));
        scenarioMappings.verify("identity",
                getRequestedFor(urlEqualTo("/pauth/access?actions=identity:tenants:read,identity:tenants:write&tenants=root,test"))
                        .withHeader("header1", equalTo("value1")));
        scenarioMappings.verify("identity",
                getRequestedFor(urlEqualTo("/pauth/access?actions=identity:tenants:read,identity:tenants:write&tenants=root,test"))
                        .withHeader("header2", equalTo("2")));

    }

    @Test
    public void whenPerformingPost_andReturn204() {
        stubForPost(scenarioMappings, 204, "/pauth/passwords");

        Response<Password> response = client.service("identity").endpoint("pauth/passwords", Password.class).post(Password.builder()
                .newPassword("Password2#!").resetToken("test-password-reset-token").username("test-end-user\\resource-owner").build());

        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(204, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(false));
    }

    @Test
    public void whenPerformingPost_addingHeaders_andReturn204() {
        RequestHeaders headers = withHeaders("header1", "value1").add("header2", 2);
        stubForPost(scenarioMappings, 204, "/pauth/passwords", headers.getHeaders());

        Response<Password> response = client.service("identity").endpoint("pauth/passwords", Password.class).post(Password.builder()
                .newPassword("Password2#!").resetToken("test-password-reset-token").username("test-end-user\\resource-owner").build(), headers);

        assertThat(response.isSuccessStatusCode(), is(true));
        assertEquals(204, response.getStatusCode());
        assertThat(response.getPayload().isPresent(), is(false));
        scenarioMappings.verify("identity", postRequestedFor(urlEqualTo("/pauth/passwords")).withHeader("header1", equalTo("value1")));
        scenarioMappings.verify("identity", postRequestedFor(urlEqualTo("/pauth/passwords")).withHeader("header2", equalTo("2")));
    }

    @Test
    public void whenPerformingPost_withInvalidPayload() {
        thrown.expect(PayloadValidationException.class);
        client.service("identity").endpoint("pauth/passwords", Password.class)
                .post(Password.builder().resetToken("test-password-reset-token").username("test-end-user\\resource-owner").build());

    }

    @Test
    public void whenPerformingPost_andReturn400() {
        thrown.expect(RequestExecutionException.class);
        stubForPost(scenarioMappings, 400, "/pauth/passwords");

        client.service("identity").endpoint("pauth/passwords", Password.class)
                .post(Password.builder().newPassword("Pa").resetToken("test-password-reset-token").username("test-end-user\\resource-owner").build());
    }
}
