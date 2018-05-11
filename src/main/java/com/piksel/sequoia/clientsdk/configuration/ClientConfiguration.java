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

import com.piksel.sequoia.clientsdk.DefaultServiceFactoryProviderWithOwner;
import com.piksel.sequoia.clientsdk.ServiceFactoryProviderWithOwner;
import com.piksel.sequoia.clientsdk.request.DefaultRequestClient;
import com.piksel.sequoia.clientsdk.request.RequestClient;
import java.util.Collection;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.BackOff;
import com.google.gson.GsonBuilder;
import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.DefaultServiceFactoryProvider;
import com.piksel.sequoia.clientsdk.MessageConfiguration;
import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.ServiceFactoryProvider;
import com.piksel.sequoia.clientsdk.recovery.RecoveryStrategy;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Provides configuration for the {@link SequoiaClient} and establishes a set of
 * required and default values.
 */
@Value
@Builder
@EqualsAndHashCode(exclude = { "httpTransport", "httpResponseInterceptorName", "serviceFactoryProviderClass", "gsonBuilder", "typeAdapters"})
@PublicEvolving
public class ClientConfiguration {

    @NonNull
    private final Class<? extends ServiceFactoryProvider> serviceFactoryProviderClass;

    @NonNull
    private final Class<? extends ServiceFactoryProviderWithOwner> serviceFactoryProviderWithOwnerClass;

    @NotNull
    private final Class<? extends RequestClient> requestClientClass;

    /**
     * Specifies the host configuration for the identity service.
     */
    @NonNull
    private final HostConfiguration identityHostConfiguration;

    /**
     * Specifies the component credentials used to access the identity
     * service and retrieve a token.
     */
    private final ComponentCredentials identityComponentCredentials;

    /**
     * Specifies the host configuration for the registry service.
     */
    @NonNull
    private final HostConfiguration registryHostConfiguration;

    /**
     * Allows the low-level HTTP transport to be provided for request/
     * response interception or mocking.
     */
    @NonNull
    private HttpTransport httpTransport;

    /**
     * If set, allows each HTTP response to be intercepted for additional
     * processing, such as logging.
     */
    private String httpResponseInterceptorName;

    /**
     * Provide the back off strategy to use when recovering from
     * communication issues.
     *
     * @deprecated {@link #recoveryStrategy} will be used for this functionality.
     */
    @Deprecated
    private BackOff backOffStrategy;

    /**
     * Provide the {@link RecoveryStrategy} to use when recovering from
     * communication issues.
     *
     */
    @NonNull
    private RecoveryStrategy recoveryStrategy;


    /**
     * The owner to use when using the registry service.
     */
    @NonNull
    private String registryServiceOwner;

    @NonNull
    private UserAgentConfigurer userAgentConfigurer;

    private int serviceRefreshIntervalSeconds;

    private int connectTimeOut;

    private int readTimeOut;

    private Collection<TypeAdapter> typeAdapters;

    @NonNull
    private GsonBuilder gsonBuilder;

    public MessageConfiguration getMessageConfiguration() {
        return new MessageConfiguration(httpTransport,
                GsonFactory.getDefaultInstance());
    }

    /**
     * Provides a builder with default values configured ready to be overridden if needed.
     */
    public static ClientConfigurationBuilder builder() {
        return new ClientConfigurationBuilder()
            .recoveryStrategy(RecoveryStrategy.builder().backOff(BackOff.ZERO_BACKOFF)
                .numberOfRetries(10).build())
            .gsonBuilder(DefaultClientConfiguration.getDefaultGsonBuilder())
            .serviceRefreshIntervalSeconds(3600)
            .userAgentConfigurer(noOperationConfigurer())
            .typeAdapters(
                DefaultClientConfiguration.getDefaultTypeAdapters())
            .serviceFactoryProviderClass(DefaultServiceFactoryProvider.class)
            .serviceFactoryProviderWithOwnerClass(DefaultServiceFactoryProviderWithOwner.class)
            .requestClientClass(DefaultRequestClient.class)
            .httpTransport(new NetHttpTransport());
    }

    private static UserAgentConfigurer noOperationConfigurer() {
        return s -> s;
    }

}
