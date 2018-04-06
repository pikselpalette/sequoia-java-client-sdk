package com.piksel.sequoia.clientsdk;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.recovery.RecoveryStrategy;
import com.piksel.sequoia.clientsdk.token.DataServicesCredentialProvider;

public class DefaultDataServicesRequestConfiguratorTest {

    private ClientConfiguration clientConfiguration;

    @Mock
    private JsonObjectParser jsonObjectParser;
    @Mock
    private DataServicesCredentialProvider dataServicesCredentialProvider;
    @Mock
    private UserAgentStringSupplier userAgentStringSupplier;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        RecoveryStrategy recoveryStrategy = RecoveryStrategy.builder().numberOfRetries(1).build();
        clientConfiguration = ClientConfiguration.builder().identityHostConfiguration(new HostConfiguration(""))
                                                 .registryHostConfiguration(new HostConfiguration(""))
                                                 .registryServiceOwner("").recoveryStrategy(recoveryStrategy)
                                                 .httpResponseInterceptorName(MockHttpResponseInterceptor.class.getName())
                                                 .build();
    }

    @Test
    public void shouldConfigureHttpInterceptor() throws IOException {

        HttpRequest httpRequest = aHttpRequest();
        DefaultDataServicesRequestConfigurator configurator =
                new DefaultDataServicesRequestConfigurator(jsonObjectParser, dataServicesCredentialProvider,
                        clientConfiguration, userAgentStringSupplier);

        configurator.configure(httpRequest);

        assertThat(httpRequest.getResponseInterceptor().getClass().getName(), is(MockHttpResponseInterceptor.class.getName()));
    }

    private HttpRequest aHttpRequest() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();

        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(request -> {});

        return requestFactory.buildGetRequest(new GenericUrl("http://example.com"));

    }

}
