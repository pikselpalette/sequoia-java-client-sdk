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

import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.OWNER;
import static com.piksel.sequoia.wiremock.helper.WiremockHelper.prepareServicesMappings;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.ServiceFactory;
import com.piksel.sequoia.clientsdk.ServiceFactoryProvider;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.wiremock.helper.WiremockHelper;

public class ServiceFactoryProviderTest extends ClientIntegrationTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Override
    @Before
    public void setup() {
        // Override default wiremock helper init so we can customise client 
        // configuration
    }
    
    private void configureWiremock(Supplier<Object> clientSupplier) {
        WiremockHelper.setServicesList(Arrays.asList("identity", "registry", "metadata"));
        WiremockHelper.setClientInitialiazer(clientSupplier);
        WiremockHelper.setServiceCreator(registeredServiceCreationFunction);
        scenarioMappings = prepareServicesMappings(testName.getMethodName());
        client = scenarioMappings.getSequoiaClient();
    }
    
    @Test
    public void givenCustomerServiceFactoryProvider_shouldUseThatToCreateServices() throws Exception {
        thrown.expect(TestServiceProviderFactory.FooException.class);
        configureWiremock(this::clientSupplierWithConfiguration);
    
        getHostRegistryAndWaitForItToBePopulated(client);
        client.service("identity");
    }

    private static class TestServiceProviderFactory implements ServiceFactoryProvider {

        @SuppressWarnings("serial")
        private static class FooException extends RuntimeException {}
        
        @Override
        public ServiceFactory from(RegisteredService service) {
            return name -> {
                throw new FooException();
            };
        }
        
    }
    
    private Object clientSupplierWithConfiguration() {
        ClientConfiguration clientConfig = ClientConfiguration.builder()
                .identityHostConfiguration(identityHostConfiguration())
                .identityComponentCredentials(identityComponentCredentials()).registryHostConfiguration(registryHostConfiguration())
                .registryServiceOwner(OWNER)
                .serviceFactoryProviderClass(TestServiceProviderFactory.class).build();
        return SequoiaClient.client(clientConfig);
    }
    
}
