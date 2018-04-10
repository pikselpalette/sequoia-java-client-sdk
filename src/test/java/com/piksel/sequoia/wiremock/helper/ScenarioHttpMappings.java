package com.piksel.sequoia.wiremock.helper;

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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.collect.Lists.newArrayList;
import static com.piksel.sequoia.wiremock.helper.utils.ScenarioHttpUtils.createSuccessAuth;
import static com.piksel.sequoia.wiremock.helper.utils.ScenarioHttpUtils.encodedUserAuth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder;
import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.gson.Gson;
import com.piksel.sequoia.wiremock.helper.utils.ScenarioHttpUtils;

import lombok.NonNull;

/**
 * This class allows add {@link WireMock} mappings and verifications.
 *
 */
@SuppressWarnings("rawtypes")
public class ScenarioHttpMappings {

    private static final Gson GSON = new Gson();
    private static final Map<String, WireMockServer> SERVERS = new ConcurrentHashMap<>();

    private static final String REQUEST_BODY_FOR_CLIENT_AT_AUTH = "grant_type=client_credentials";
    public static final String OWNER = "unitTestRoot";
    public static final String SERVICEID_IDENTITY = "identity";
    public static final String SERVICEID_REGISTRY = "registry";

    public static final String USER = UUID.randomUUID().toString().replace("-", "");
    public static final String PASSWORD = UUID.randomUUID().toString().replace("-", "");
    private final String token = DigestUtils.sha1Hex(UUID.randomUUID().toString());

    private String scenarioId;
    private List<Object> availableServices = newArrayList();

    private Object client;

    private BiFunction<String, Integer, Object> serviceCreator;

    private Map<String, String> currentDefinitionStep = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <O> O getSequoiaClient() {
        return (O) client;
    }

    private ScenarioHttpMappings(
            String scenario,
            Collection<String> services,
            @NonNull BiFunction<String, Integer, Object> serviceCreator) {
        this.serviceCreator = serviceCreator;
        initScenario(scenario);
        createServicesServers(services);
        createMappingForStart(WireMock.containing(REQUEST_BODY_FOR_CLIENT_AT_AUTH), createSuccessAuth(token));
        createSuccessRegistryServices();
    }

    ScenarioHttpMappings(String scenario, Collection<String> services, Supplier<Object> clientInitializer,
            BiFunction<String, Integer, Object> serviceCreator) {
        this(scenario, services, serviceCreator);
        initClient(clientInitializer);
    }

    ScenarioHttpMappings(String scenario, ResponseDefinitionBuilder response, Collection<String> services,
            @NonNull Supplier<Object> sequoiaClientInitializer, @NonNull BiFunction<String, Integer, Object> serviceCreator) {
        this.serviceCreator = serviceCreator;
        initScenario(scenario);
        createServicesServers(services);
        createMappingForStart(equalTo(REQUEST_BODY_FOR_CLIENT_AT_AUTH), response);
        initClient(sequoiaClientInitializer);
    }

    ScenarioHttpMappings(String scenario, ValueMatchingStrategy requestBody, ResponseDefinitionBuilder response, Collection<String> services,
            @NonNull Supplier<Object> sequoiaClientInitializer, @NonNull BiFunction<String, Integer, Object> serviceCreator) {
        this.serviceCreator = serviceCreator;
        initScenario(scenario);
        createMappingForStart(requestBody, response);
        initClient(sequoiaClientInitializer);
    }

    public void addMapping(String serviceId, RemoteMappingBuilder requestMapping) {
        createScenarioBindedMapping(null, serviceId, requestMapping, equalTo("Bearer " + token));
    }

    public void addStepMapping(String serviceId, RemoteMappingBuilder requestMapping) {
        addStepMapping(UUID.randomUUID().toString(), serviceId, requestMapping);
    }

    private void addStepMapping(String step, String serviceId, RemoteMappingBuilder requestMapping) {
        createScenarioBindedMapping(step, serviceId, requestMapping, WireMock.equalTo("Bearer " + token));
    }

    public void addStepMappingWithCustomAuthorizationHeader(String step, String serviceId, RemoteMappingBuilder requestMapping) {
        createScenarioBindedMapping(step, serviceId, requestMapping, null);
    }

    public static String serviceUrl(String serviceId) {
        return ScenarioHttpUtils.serviceUrl(SERVERS.get(serviceId).port());
    }

    public void verify(String serviceId, RequestPatternBuilder requestPatternBuilder) {
        SERVERS.get(serviceId).verify(requestPatternBuilder);
    }

    public void verify(String serviceId, int count, RequestPatternBuilder requestPatternBuilder) {
        SERVERS.get(serviceId).verify(count, requestPatternBuilder);
    }

    public int servicePort(String serviceId) {
        return SERVERS.get(serviceId).port();
    }

    private void initScenario(String scenario) {
        if (scenario != null) {
            this.scenarioId = scenario;
        } else {
            this.scenarioId = UUID.randomUUID().toString();
        }
    }

    private void createServicesServers(Collection<String> serviceList) {
        serviceList.forEach((serviceId) -> {
            if (!SERVERS.containsKey(serviceId)) {
                createServerForService(serviceId);
            }
            synchronized (this) {
                availableServices.add(newService(serviceId, SERVERS.get(serviceId).port()));
            }
        });

    }

    private void createServerForService(String serviceId) {
        WireMockServer server = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        server.start();
        SERVERS.put(serviceId, server);
    }

    private void initClient(Supplier<Object> clientInitializer) {
        client = clientInitializer != null ? clientInitializer.get() : null;
    }

    private void createMappingForStart(ValueMatchingStrategy requestBody, ResponseDefinitionBuilder response) {
        createScenarioBindedMapping(null, SERVICEID_IDENTITY, post(urlEqualTo("/oauth/token")).withRequestBody(requestBody).willReturn(response),
                equalTo("Basic " + encodedUserAuth(USER, PASSWORD)));
    }

    private void createScenarioBindedMapping(String step, String serviceId, RemoteMappingBuilder mapping, ValueMatchingStrategy authHeader) {
        ScenarioMappingBuilder scenarioMapping = mapping.inScenario(scenarioId);
        if (step != null) {
            scenarioMapping.whenScenarioStateIs(currentStep(serviceId)).willSetStateTo(nextStep(serviceId, step));
        }

        if (authHeader != null) {
            scenarioMapping.withHeader("Authorization", authHeader);
        }

        WireMockServer server = getServer(serviceId);
        server.stubFor(scenarioMapping);
    }

    private WireMockServer getServer(String serviceId) {
        WireMockServer server = SERVERS.get(serviceId);
        if (Objects.isNull(server)) {
            server = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
            server.start();
        }
        return server;
    }

    private void createSuccessRegistryServices() {
        Map<String, Object> responseBody = new ConcurrentHashMap<>();
        responseBody.put("services", availableServices);

        addMapping(SERVICEID_REGISTRY,
                get(urlEqualTo("/services/" + OWNER)).willReturn(aResponse().withStatus(200).withBody(GSON.toJson(responseBody))));
    }

    private String currentStep(String serviceId) {
        return currentDefinitionStep.getOrDefault(serviceId, Scenario.STARTED);
    }

    private String nextStep(String serviceId, @NonNull String step) {
        currentDefinitionStep.put(serviceId, step);
        return currentDefinitionStep.get(serviceId);
    }

    private Object newService(String name, int port) {
        return serviceCreator.apply(name, port);
    }

}
