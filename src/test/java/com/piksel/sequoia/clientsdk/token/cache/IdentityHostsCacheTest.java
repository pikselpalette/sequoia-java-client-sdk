package com.piksel.sequoia.clientsdk.token.cache;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 - 2019 Piksel
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

import com.piksel.sequoia.clientsdk.configuration.HostConfiguration;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.RegistryClient;
import com.piksel.sequoia.clientsdk.resource.ResourceIterable;
import lombok.Builder;
import lombok.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdentityHostsCacheTest {

    @Mock
    private RegistryClient registryClient;

    private HostConfiguration identityHost;

    private IdentityHostsCache underTest;

    @Before
    public void setUp() {
        identityHost = new HostConfiguration("http://localhost:8081");
        underTest = IdentityHostsCache.builder()
                .defaultIdentityHost(identityHost)
                .registryClient(registryClient)
                .build();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(IdentityHostsCache.class)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS,
                        Warning.ALL_NONFINAL_FIELDS_SHOULD_BE_USED).withRedefinedSuperclass().verify();
    }

    @Test
    public void givenRegistryClientReturnsNothingThenItShouldReturnDefault() throws Exception {
        String owner = "test";
        when(registryClient.getServiceRegistryListForOwner(owner)).thenReturn(new StubEmptyResourceIterable());

        HostConfiguration host = underTest.load(owner);

        assertThat("Unexpected host configuration from cache", host, equalTo(identityHost));
    }

    @Test
    public void givenRegistryClientReturnsListOfServicesThenItShouldReturnIdentity() throws Exception {
        String owner = "test";
        RegisteredService service = new RegisteredService("http://localhost:8080/identity", "identity");
        HostConfiguration expectedHost = new HostConfiguration(service.getLocation());
        when(registryClient.getServiceRegistryListForOwner(owner)).thenReturn(StubOneElementResourceIterable.builder().singleElement(service).hasNext(true).build());

        HostConfiguration host = underTest.load(owner);

        assertThat("Unexpected host configuration from cache", host, equalTo(expectedHost));
    }

    @Test
    public void givenRegistryClientReturnsListOfServicesWithoutIdentityThenItShouldReturnIdentity() throws Exception {
        String owner = "test";
        RegisteredService service = new RegisteredService("http://localhost:8080/identity", "registry");
        when(registryClient.getServiceRegistryListForOwner(owner)).thenReturn(StubOneElementResourceIterable.builder().singleElement(service).hasNext(true).build());

        HostConfiguration host = underTest.load(owner);

        assertThat("Unexpected host configuration from cache", host, equalTo(identityHost));
    }

    private static final class StubEmptyResourceIterable implements ResourceIterable<RegisteredService> {

        @Override
        public RegisteredService single() {
            return null;
        }

        @Override
        public Optional<Integer> totalCount() {
            return Optional.empty();
        }

        @Override
        public Optional<Map<String, Map<String, Integer>>> facetCount() {
            return Optional.empty();
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public RegisteredService next() {
            return null;
        }
    }

    @Builder
    @Data
    private static final class StubOneElementResourceIterable implements ResourceIterable<RegisteredService> {

        private RegisteredService singleElement;
        private boolean hasNext;

        @Override
        public RegisteredService single() {
            return singleElement;
        }

        @Override
        public Optional<Integer> totalCount() {
            return Optional.of(1);
        }

        @Override
        public Optional<Map<String, Map<String, Integer>>> facetCount() {
            return Optional.empty();
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public RegisteredService next() {
            hasNext = false;
            return singleElement;
        }
    }
}
