package com.piksel.sequoia.clientsdk.client.integration;

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

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder;
import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.resource.ResourceCollection;
import com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.google.common.collect.Lists.newArrayList;
import static java.time.OffsetDateTime.now;

/**
 * A utility class providing stubbed mock responses for WireMock tests.
 */
public class MockResponses {

    private static final Gson gson = new Gson();

    private MockResponses() {
    }

    public static void stubForTokenResponseSuccessful(WireMockServer wireMockServer) throws IOException {
        wireMockServer.stubFor(post(urlEqualTo("/oauth/token")).willReturn(aResponse().withStatus(200).withBody(validToken())));
    }

    public static void stubForTokenResponseUnauthorised(WireMockServer wireMockServer) {
        wireMockServer.stubFor(post(urlEqualTo("/oauth/token")).willReturn(aResponse().withStatus(HttpStatus.SC_UNAUTHORIZED)));
    }

    public static String validToken() throws IOException {
        return new TokenResponse().setAccessToken("valid-token").setExpiresInSeconds(theDistantFuture()).toPrettyString();
    }

    private static long theDistantFuture() {
        return now().plusYears(100).toEpochSecond();
    }

    public static void stubForGet(ScenarioHttpMappings scenarioHttpMappings, String response, int statusCode, String url) {
        scenarioHttpMappings.addMapping("identity", get(urlEqualTo(url)).willReturn(aResponse().withStatus(statusCode).withBody(response)));
    }

    public static void stubForGet(ScenarioHttpMappings scenarioHttpMappings, String response, int statusCode, String url,
            Map<? extends String, ?> headers) {
        @SuppressWarnings("rawtypes")
        RemoteMappingBuilder remoteMappingBuilder = get(urlEqualTo(url));

        headers.forEach((key, value) -> remoteMappingBuilder.withHeader(key, setHeader(value.toString())));

        scenarioHttpMappings.addMapping("identity", remoteMappingBuilder.willReturn(aResponse().withStatus(statusCode).withBody(response)));
    }

    private static ValueMatchingStrategy setHeader(String value) {
        ValueMatchingStrategy headerStrategy = new ValueMatchingStrategy();
        headerStrategy.setEqualTo(value);
        return headerStrategy;
    }

    public static void stubForPost(ScenarioHttpMappings scenarioHttpMappings, int statusCode, String url) {
        scenarioHttpMappings.addMapping("identity", post(urlEqualTo(url)).willReturn(aResponse().withStatus(statusCode)));
    }

    public static void stubForPost(ScenarioHttpMappings scenarioHttpMappings, int statusCode, String url, Map<? extends String, ?> headers) {
        @SuppressWarnings("rawtypes")
        RemoteMappingBuilder remoteMappingBuilder = post(urlEqualTo(url));
        headers.forEach((key, value) -> remoteMappingBuilder.withHeader(key, setHeader(value.toString())));
        scenarioHttpMappings.addMapping("identity", remoteMappingBuilder.willReturn(aResponse().withStatus(statusCode)));
    }

    public static void stubForServiceRegistry(WireMockServer wireMockServer) {
        wireMockServer.stubFor(
                get(urlEqualTo("/services/test")).willReturn(aResponse().withStatus(200).withBody(gson.toJson(serviceList(wireMockServer.port())))));
    }

    public static void stubForServiceRegistryWithTestOwner(WireMockServer wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/services/testowner"))
                .willReturn(aResponse().withStatus(200).withBody(gson.toJson(serviceList(wireMockServer.port())))));
    }

    public static void stubForServiceRegistryWithFault(WireMockServer wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/services/test")).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
    }

    public static void stubForServiceRegistryGet(ScenarioHttpMappings scenarioHttpMappings, String response, int status, String ref) {
        scenarioHttpMappings.addMapping("registry",
                get(urlEqualTo(String.join("/", "/data/services", ref))).willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForServiceRegistryGetQueryParams(ScenarioHttpMappings scenarioHttpMappings, String response, int status, String params) {
        scenarioHttpMappings.addMapping("registry",
                get(urlEqualTo(String.join("?", "/data/services", params))).willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubGetMetadataResponse(ScenarioHttpMappings scenarioHttpMappings, String resource, String response, int status, String params) {
        scenarioHttpMappings.addMapping("metadata",
                get(urlEqualTo(String.join("?", String.join("/", "/data", resource), params))).willReturn(aResponse().withStatus(status).withBody(response).withHeader("content-type", "application/json;charset=UTF-8")));
    }

    public static void stubForRegistryClientWithServiceName(ScenarioHttpMappings scenarioHttpMappings, String response, int status,
            String serviceName) {
        scenarioHttpMappings.addMapping("registry", get(urlEqualTo(String.join("/", "/services/unitTestRoot", serviceName)))
                .willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForRegistryClientForNewOwner(ScenarioHttpMappings scenarioHttpMappings, String response, int status, String owner) {
        scenarioHttpMappings.addMapping("registry", get(urlEqualTo(String.join("/", "/services", owner)))
            .willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForRegistryClientWithServiceName(ScenarioHttpMappings scenarioHttpMappings, String response, int status,
        String serviceName, String owner) {
        scenarioHttpMappings.addMapping("registry", get(urlEqualTo(String.join("/", "/services", owner, serviceName)))
            .willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForServiceRegistryGetWithFault(ScenarioHttpMappings scenarioHttpMappings, String response, String ref) {
        scenarioHttpMappings.addMapping("registry",
                get(urlEqualTo(String.join("/", "/data/services", ref))).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
    }

    public static void stubForServiceRegistryBrowseWithFault(ScenarioHttpMappings scenarioHttpMappings, String queryString) {
        scenarioHttpMappings.addMapping("registry",
                get(urlEqualTo(String.join("?", "/data/services", queryString))).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
    }

    public static void stubForServiceRegistryGetNotFoundResponse(ScenarioHttpMappings scenarioHttpMappings, String ref) {
        scenarioHttpMappings.addMapping("registry", get(urlEqualTo(String.join("/", "/data/services", ref)))
                .willReturn(aResponse().withStatus(HttpStatus.SC_NOT_FOUND).withBody("{\"statusCode\": 404,\"error\": \"Not Found\"}")));
    }

    public static void stubForServiceRegistryPost(ScenarioHttpMappings scenarioHttpMappings, String response, int status) {
        scenarioHttpMappings.addMapping("registry", post(urlEqualTo("/data/services")).willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForServiceRegistryDynamicPost(ScenarioHttpMappings scenarioHttpMappings, String response, int status) {
        scenarioHttpMappings.addMapping("registry",
                post(urlEqualTo("/data/services?owner=test")).willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForServiceRegistryDelete(ScenarioHttpMappings scenarioHttpMappings, String response, int status, String ref) {
        scenarioHttpMappings.addMapping("registry",
                delete(urlEqualTo(String.join("/", "/data/services", ref))).willReturn(aResponse().withStatus(status).withBody(response)));
    }

    public static void stubForServiceRegistryDeleteWithFault(ScenarioHttpMappings scenarioHttpMappings, String response, String ref) {
        scenarioHttpMappings.addMapping("registry",
                delete(urlEqualTo(String.join("/", "/data/services", ref))).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
    }

    public static void stubForServiceRegistryPostConflict(WireMockServer wireMockServer, String response) {
        wireMockServer.stubFor(post(urlEqualTo("/data/services")).willReturn(aResponse().withStatus(201).withBody(response)));
    }

    public static void stubForServiceRegistryUpdate(ScenarioHttpMappings scenarioHttpMappings, String response, int status, String ref) {
        scenarioHttpMappings.addMapping("registry", put(urlEqualTo(String.join("/", "/data/services", ref)))
                .willReturn(aResponse().withHeader("x-correlation-id", "mycorrelationId").withStatus(status).withBody(response)));
    }

    public static void stubForRegistryGet(ScenarioHttpMappings scenarioMappings, String owner, String response) {
        scenarioMappings.addMapping("registry", get(urlEqualTo("/services/" + owner)).willReturn(aResponse().withStatus(200)
                .withBody(response)));
    }

    public static void stubForServiceRegistryPostWithFault(ScenarioHttpMappings scenarioHttpMappings) {
        scenarioHttpMappings.addMapping("registry",
                post(urlEqualTo("/data/services")).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
    }

    private static Map<String, ResourceCollection<RegisteredService>> serviceList(int port) {
        ResourceCollection<RegisteredService> list = new ResourceCollection<>();
        list.addAll(newArrayList(newService("services", "services", port), newService("registry", "registry", port),
                newService("identity", "identity", port)));
        Map<String, ResourceCollection<RegisteredService>> resourceResponse = new HashMap<>();
        resourceResponse.put("services", list);
        return resourceResponse;
    }

    public static RegisteredService newService(String name, String title, int port) {
        RegisteredService registeredService = new RegisteredService();
        registeredService.setName(name);
        registeredService.setTitle(title);
        registeredService.setLocation("http://127.0.0.1:" + port);
        return registeredService;
    }

}
