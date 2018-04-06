package com.piksel.sequoia.clientsdk.client;

import java.util.Optional;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.piksel.sequoia.clientsdk.DataServicesClientModule;
import com.piksel.sequoia.clientsdk.client.MockTransportResponseProvider.MockResponse;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.configuration.ComponentCredentials;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.registry.HostRegistry;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;

/**
 * Build a typical data service client module but with the ability to mock the
 * transport responses via a {@link MockTransportResponseProvider}. Individual
 * tests may provide an implementation which provides ad-hoc mocked responses.
 */
public abstract class DataServiceClientTestModule extends AbstractModule {

    private final ClientConfiguration.ClientConfigurationBuilder configurationBuilder = ClientConfiguration
            .builder();
    private final MockTransportResponseProvider mockResponseProvider;

    /**
     * Create a new data service test module. 
     * @param mockResponseProvider the provide of the mock responses.
     */
    public DataServiceClientTestModule(MockTransportResponseProvider mockResponseProvider) {
        this.mockResponseProvider = mockResponseProvider;
        this.configurationBuilder
                .httpTransport(mockTransport(mockResponseProvider))
                .registryServiceOwner("test")
                .identityComponentCredentials(
                        new ComponentCredentials("test-user", "test-password"))
                .identityHostConfiguration(new HostConfiguration(
                        "http://identity.local-test.piksel.com/"))
                .registryHostConfiguration(new HostConfiguration(
                        "http://registry.local-test.piksel.com/"));
    }

    protected void overrideConfiguration(
            ClientConfiguration.ClientConfigurationBuilder configurationBuilder) {
    }

    @Override
    protected void configure() {
        overrideConfiguration(configurationBuilder);
        install(new DataServicesClientModule(configurationBuilder.build()));
        bind(MockTransportResponseProvider.class)
                .toInstance(mockResponseProvider);
    }

    protected MockHttpTransport mockTransport(
            MockTransportResponseProvider mockResponseProvider) {
        return new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() {
                        MockResponse mockResponseContents = mockResponseProvider
                                .provideStringResponse();
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setStatusCode(mockResponseContents.status);
                        response.setContent(mockResponseContents.body);
                        return response;
                    }
                };
            }
        };
    }

    @Provides
    public HostRegistry providesHostRegistry() {
        return createHostRegistry();
    }

    protected HostRegistry createHostRegistry() {
        return new HostRegistry() {

            @Override
            public boolean isPopulated() {
                return true;
            }

            @Override
            public Optional<RegisteredService> getRegisteredService(
                    String service) {
                return Optional.of(new RegisteredService(
                        "http://service.local-test.piksel.com",
                        "test-service"));
            }

            @Override
            public Optional<RegisteredService> getRegisteredService(String serviceName,
                String owner) {
                return Optional.of(new RegisteredService(
                    "http://service.local-test.piksel.com",
                    "test-service"));
            }

        };
    }

}