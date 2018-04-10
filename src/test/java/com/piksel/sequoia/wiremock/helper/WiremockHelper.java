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

import static com.google.common.collect.Lists.newArrayList;
import static com.piksel.sequoia.wiremock.helper.utils.ScenarioHttpUtils.serviceUrl;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy;

import lombok.Setter;

/**
 * This class allows to prepare the mappings for every scenario. Furthermore holds a client initializer, service creator a services lists to ease the
 * usage of third parties.
 *
 */
public class WiremockHelper {

    @Setter
    private static Supplier<Object> clientInitialiazer;

    @Setter
    private static BiFunction<String, Integer, Object> serviceCreator =
            (String serviceName, Integer port) -> new AbstractMap.SimpleEntry<>(serviceName, serviceUrl(port));

    @Setter
    private static Collection<String> servicesList;

    public static ScenarioConfigurerBuilder withServices(String... service) {
        return new ScenarioConfigurerBuilder(service);
    }

    public static ScenarioHttpMappings prepareServicesMappings(ScenarioConfigurerBuilder builder, String scenario) {
        return builder.prepareServicesMappings(scenario);
    }

    public static ScenarioHttpMappings prepareServicesMappings(ScenarioConfigurerBuilder builder) {
        return builder.prepareServicesMappings(null);
    }

    public static ScenarioHttpMappings prepareServicesMappings() {
        return new ScenarioConfigurerBuilder().prepareServicesMappings(null);
    }

    public static ScenarioHttpMappings prepareServicesMappings(String scenario) {
        return new ScenarioConfigurerBuilder().prepareServicesMappings(scenario);
    }

    public static class ScenarioConfigurerBuilder {
        private ValueMatchingStrategy authRequestBody;
        private ResponseDefinitionBuilder authResponse;

        private ScenarioConfigurerBuilder(String... service) {
            if (Objects.isNull(servicesList)) {
                servicesList = newArrayList();
            }
            Collection<String> serviceCloned = new ArrayList<>(servicesList);
            serviceCloned.addAll(Arrays.stream(service).collect(Collectors.toSet()));
            servicesList = serviceCloned;
        }

        public ScenarioConfigurerBuilder andBodyForAuthRequest(ValueMatchingStrategy authRequestBody) {
            this.authRequestBody = authRequestBody;
            return this;
        }

        public ScenarioConfigurerBuilder andResponseForAuthRequest(ResponseDefinitionBuilder authResponse) {
            this.authResponse = authResponse;
            return this;
        }

        /**
         * Create the mapping for the given scenario id.
         * 
         * @param scenarioId
         *            identifier for the scenario.
         * @return {@link ScenarioHttpMappings} created
         */
        ScenarioHttpMappings prepareServicesMappings(String scenarioId) {
            ScenarioHttpMappings result;
            if (authRequestBody == null) {
                result = createScenarioHttpMappingsWithoutAuthRequest(scenarioId);
            } else {
                result = createScenarioHttpMappingsWithAuthRequest(scenarioId);
            }
            return result;
        }

        private ScenarioHttpMappings createScenarioHttpMappingsWithAuthRequest(String scenario) {
            if (authResponse == null) {
                throw new IllegalArgumentException("authResponse is required");
            } else {
                return new ScenarioHttpMappings(scenario, authRequestBody, authResponse, servicesList, clientInitialiazer, serviceCreator);
            }
        }

        private ScenarioHttpMappings createScenarioHttpMappingsWithoutAuthRequest(String scenario) {
            ScenarioHttpMappings result;
            if (authResponse == null) {
                result = new ScenarioHttpMappings(scenario, servicesList, clientInitialiazer, serviceCreator);
            } else {
                result = new ScenarioHttpMappings(scenario, authResponse, servicesList, clientInitialiazer, serviceCreator);
            }
            return result;
        }
    }

}
