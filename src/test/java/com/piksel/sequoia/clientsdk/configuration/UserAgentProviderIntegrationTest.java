package com.piksel.sequoia.clientsdk.configuration;

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

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.resource.Reference;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;
import com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings;
import com.piksel.sequoia.wiremock.helper.WiremockHelper;

public class UserAgentProviderIntegrationTest extends ClientIntegrationTestBase {

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
        scenarioMappings = WiremockHelper.prepareServicesMappings(testName.getMethodName());
        client = scenarioMappings.getSequoiaClient();
    }
    
    @Test
    public void givenModifiedUserAgentString_shouldAddHeaderToRequest() throws Exception {
        configureWiremock(this::clientSupplierWithConfiguration);
        getHostRegistryAndWaitForItToBePopulated(client);
        
        ResourcefulEndpoint<TestResourceClass> endpoint = client.service("metadata").resourcefulEndpoint("someResource", TestResourceClass.class);
        endpoint.read(Reference.fromOwnerAndName("someOwner", "someName"));
        
        scenarioMappings.verify("metadata", getRequestedFor(urlEqualTo("/data/someResource/someOwner:someName"))
                .withHeader("User-Agent", containing("custom-user-agent")));
    }
        
    private Object clientSupplierWithConfiguration() {
        ClientConfiguration clientConfig = ClientConfiguration.builder()
                .identityHostConfiguration(identityHostConfiguration())
                .identityComponentCredentials(identityComponentCredentials()).registryHostConfiguration(registryHostConfiguration())
                .registryServiceOwner(ScenarioHttpMappings.OWNER)
                .userAgentConfigurer(s -> "custom-user-agent").build();
        return SequoiaClient.client(clientConfig);
    }
    
    private static class TestResourceClass extends Resource {
    }
    
}
