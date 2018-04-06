package com.piksel.sequoia.clientsdk.client.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryWithTestOwner;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForTokenResponseSuccessful;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForTokenResponseUnauthorised;
import static com.piksel.sequoia.clientsdk.configuration.ClientConfiguration.builder;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.ClassRule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration.ClientConfigurationBuilder;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.registry.service.RegisteredServicesService;

public class RegistryServiceIntegrationTest extends ClientIntegrationTestBase {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            wireMockConfig().dynamicPort().notifier(new Slf4jNotifier(true)));

    @Test
    public void whenStarted_shouldAttemptToRetrieveServiceRegistry()
            throws Exception {

        stubForTokenResponseSuccessful(wireMockRule);
        stubForServiceRegistryWithTestOwner(wireMockRule);

        ClientConfigurationBuilder configuration = builder()
                .identityComponentCredentials(
                        new ComponentCredentials("test-user", "test-password"))
                .registryServiceOwner("testowner")
                .identityHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()))
                .registryHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()));
        client = SequoiaClient.client(configuration.build());

        getHostRegistryAndWaitForItToBePopulated();

        assertTrue(registryServiceLocation().startsWith("http://127.0.0.1"));
        wireMockRule.verify(postRequestedFor(urlEqualTo("/oauth/token")));

        wireMockRule.verify(getRequestedFor(urlEqualTo("/services/testowner")));
    }

    @Test
    public void whenStarted_ifTokenInvalidShowThrowExceptionThroughFuture()
            throws Exception {
        stubForTokenResponseUnauthorised(wireMockRule);

        try {
            getHostRegistryAndWaitForItToBePopulatedTestErrors("test-user-2");
        } catch (Exception e) {
            wireMockRule.verify(postRequestedFor(urlEqualTo("/oauth/token")));
            return;
        }

        fail("Exception was not thrown");
    }

    private String registryServiceLocation() {
        if (client.getHostRegistry().getRegisteredService("registry")
                .isPresent()) {
            return client.getHostRegistry().getRegisteredService("registry")
                    .get().getLocation();
        }
        throw new NoSuchElementException("Registry service not found");
    }

    protected void getHostRegistryAndWaitForItToBePopulatedTestErrors(
            String user)
            throws InterruptedException, ExecutionException, TimeoutException {
        ClientConfigurationBuilder configuration = builder()
                .identityComponentCredentials(
                        new ComponentCredentials(user, "test-password"))
                .registryServiceOwner("test")
                .identityHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()))
                .registryHostConfiguration(new HostConfiguration(
                        "http://127.0.0.1:" + wireMockRule.port()));
        SequoiaClient client = SequoiaClient.client(configuration.build());
        Future<Void> populationFuture = ((RegisteredServicesService) client
                .getHostRegistry()).awaitPopulation();
        waitForCompletion(populationFuture);
    }

}
