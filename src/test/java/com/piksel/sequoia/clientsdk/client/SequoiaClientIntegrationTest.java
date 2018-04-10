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

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryWithFault;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForTokenResponseSuccessful;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForTokenResponseUnauthorised;
import static com.piksel.sequoia.clientsdk.configuration.ClientConfiguration.builder;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.piksel.sequoia.clientsdk.SequoiaClientInitialisationException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration.ClientConfigurationBuilder;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;

public class SequoiaClientIntegrationTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            wireMockConfig().dynamicPort().notifier(new Slf4jNotifier(true)));

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    @Test
    public void testAwaitInitialised_withUnauthorized_shouldThrowException()
            throws Exception {
        stubForTokenResponseUnauthorised(wireMockRule);

        try {
            awaitInitialized("test-user-1");
        } catch (SequoiaClientInitialisationException e) {
            wireMockRule.verify(postRequestedFor(urlEqualTo("/oauth/token")));
            return;
        }

        fail("Exception was not thrown");

    }

    @Test
    public void testAwaitInitialised_withWrongResponseOfServices_shouldThrowException()
            throws Exception {
        stubForTokenResponseSuccessful(wireMockRule);
        stubForServiceRegistryWithFault(wireMockRule);
        try {
            awaitInitialized("test-user");
        } catch (SequoiaClientInitialisationException e) {
            wireMockRule.verify(postRequestedFor(urlEqualTo("/oauth/token")));
            return;
        }

        fail("Exception was not thrown");

    }

    protected void awaitInitialized(String user) throws SequoiaClientInitialisationException,
            InterruptedException, ExecutionException, TimeoutException {
        ClientConfigurationBuilder configuration = builder()
                .identityComponentCredentials(
                        new ComponentCredentials(user, "test-password"))
                .identityHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()))
                .registryServiceOwner("test")
                .registryHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()));
        SequoiaClient sequoiaClientTest = SequoiaClient
                .client(configuration.build());
        CompletableFuture<Void> future = CompletableFuture.runAsync(
            () -> sequoiaClientTest.awaitInitialised(2, TimeUnit.MINUTES));
        waitForInitailised(future);
    }

    protected void waitForInitailised(Future<Void> future) throws SequoiaClientInitialisationException,
            InterruptedException, ExecutionException, TimeoutException {
        try {
            future.get(2, TimeUnit.SECONDS);
        } catch(InterruptedException | ExecutionException | TimeoutException ex) {
            if(ex.getCause() instanceof SequoiaClientInitialisationException) {
                throw (SequoiaClientInitialisationException) ex.getCause();
            } else {
                throw ex;
            }
        }
    }

}
