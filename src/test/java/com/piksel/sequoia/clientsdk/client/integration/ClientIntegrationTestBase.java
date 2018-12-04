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

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.OWNER;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.PASSWORD;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.SERVICEID_IDENTITY;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.SERVICEID_REGISTRY;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.USER;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.serviceUrl;
import static com.piksel.sequoia.wiremock.helper.WiremockHelper.prepareServicesMappings;
import static com.piksel.sequoia.wiremock.helper.utils.ScenarioHttpUtils.serviceUrl;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.logging.LogManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.api.client.util.ExponentialBackOff;
import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.recovery.RecoveryStrategy;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.service.RegisteredServicesService;
import com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings;
import com.piksel.sequoia.wiremock.helper.WiremockHelper;

/**
 * Base class that sets up an integration test suite using a locally configured
 * {@link WireMock} instance and configures a {@link SequoiaClient client}
 * against a locally running port.
 */
public abstract class ClientIntegrationTestBase {

    protected SequoiaClient client;

    @Rule
    public TestName testName = new TestName();

    protected ScenarioHttpMappings scenarioMappings;

    @Before
    public void setup() {
        LogManager.getLogManager().reset();
        WiremockHelper.setClientInitialiazer(this::getClient);
        WiremockHelper.setServiceCreator(registeredServiceCreationFunction);
        WiremockHelper.setServicesList(Arrays.asList("identity", "registry", "metadata"));
        scenarioMappings = prepareServicesMappings(testName.getMethodName());
        client = scenarioMappings.getSequoiaClient();
    }

    private SequoiaClient getClient() {
        SequoiaClient sequoiaClient = SequoiaClient.client(withClientConfiguration());
        sequoiaClient.awaitInitialised(10000, TimeUnit.SECONDS);
        return sequoiaClient;
    }

    protected ClientConfiguration withClientConfiguration() {
        return ClientConfiguration.builder().identityHostConfiguration(identityHostConfiguration())
                .recoveryStrategy(RecoveryStrategy.builder()
                        .backOff(new ExponentialBackOff.Builder().setInitialIntervalMillis(1)
                                .setMaxIntervalMillis(5).build())
                        .numberOfRetries(3).build())
                .identityComponentCredentials(identityComponentCredentials())
                .registryHostConfiguration(registryHostConfiguration()).registryServiceOwner(OWNER)
                .build();
    }

    protected HostConfiguration identityHostConfiguration() {
        return new HostConfiguration(serviceUrl(SERVICEID_IDENTITY));
    }

    protected ComponentCredentials identityComponentCredentials() {
        return new ComponentCredentials(USER, PASSWORD);
    }

    protected HostConfiguration registryHostConfiguration() {
        return new HostConfiguration(serviceUrl(SERVICEID_REGISTRY));
    }

    protected void getHostRegistryAndWaitForItToBePopulated()
            throws InterruptedException, ExecutionException, TimeoutException {
        Future<Void> populationFuture = ((RegisteredServicesService) client.getHostRegistry())
                .awaitPopulation();
        waitForCompletion(populationFuture);
    }

    protected void getHostRegistryAndWaitForItToBePopulated(SequoiaClient client)
            throws InterruptedException, ExecutionException, TimeoutException {
        Future<Void> populationFuture = ((RegisteredServicesService) client.getHostRegistry())
                .awaitPopulation();
        waitForCompletion(populationFuture);
    }

    void waitForCompletion(Future<Void> populationFuture)
            throws InterruptedException, ExecutionException, TimeoutException {
        populationFuture.get(5, SECONDS);
    }

    protected void commonVerifications() {
        scenarioMappings.verify("identity", postRequestedFor(urlEqualTo("/oauth/token")));
        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/services/unitTestRoot"))
                .withHeader("authorization", matching(".+")));
    }

    protected BiFunction<String,Integer,Object> registeredServiceCreationFunction = (String name,
            Integer port) -> {
        RegisteredService registeredService = new RegisteredService();
        registeredService.setName(name);
        registeredService.setTitle(name);
        registeredService.setLocation(serviceUrl(port));
        return registeredService;
    };

}
