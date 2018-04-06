package com.piksel.sequoia.clientsdk;

import static com.piksel.sequoia.clientsdk.registry.service.RegisteredServicesServiceConfiguration.builder;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import com.google.api.client.repackaged.com.google.common.base.Objects;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.endpoint.Endpoint;
import com.piksel.sequoia.clientsdk.registry.HostRegistry;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.RegistryClient;
import com.piksel.sequoia.clientsdk.registry.service.RegisteredServicesService;
import com.piksel.sequoia.clientsdk.registry.service.RegisteredServicesServiceConfiguration;
import com.piksel.sequoia.clientsdk.registry.service.ServiceProvider;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides API access to the client service through service clients.
 * 
 * <p>Users should first create an instance of the client using the static
 * {@link #client(ClientConfiguration)} method passing in the
 * {@link ClientConfiguration} instance. This will begin the bootstrap process
 * of the client which involves:
 * <ul>
 * <li>Acquiring an access token using the supplied credentials
 * <li>Getting the list of available services from the service registry
 * </ul>
 * The client should not be used until this bootstrap process has terminated.
 * Users can await the completion of the bootstrap process using the
 * {@link #awaitInitialised(int, TimeUnit)} method:
 * <pre>
 * client.awaitInitialised(30, SECONDS);
 * </pre>
 * Either directly or wrapping this call in a {@link CompletableFuture} for
 * asynchronous boostrapping.
 * 
 * <p>Conceptually, the Sequoia services are structured primarily into
 * <em>services</em> which group resources of a similar types, and then
 * secondarily into <em>endpoints</em> which provides access to data using a
 * REST-style resource model.
 * 
 * <p>The client provides access to services using the {@link #service(String)}
 * method which allows the client to construct the appropriate URL for the
 * desired service. The Sequoia services use a convention for constructing URLs
 * for services:
 * 
 * <pre>
 *     http://serviceName.host.path/...
 * </pre>
 * 
 * <p>This method returns a {@link ServiceProvider} which can then be used to
 * access endpoints. Services offer two styles of endpoint: <em>endpoint</em>
 * and <em>resourcefulEndpoint</em>. An <em>endpoint</em> allows lower-level
 * HTTP access to REST resources, such as for <em>Business Endpoint</em> where
 * no specific convention for resource selection or request/response format is
 * prescribed. A <em>resourcefulEndpoint</em> allows access to endpoints that
 * follow the <em>Resourceful</em> convention.
 * 
 * <p>The {@link ServiceProvider} allows access to endpoints using the
 * {@link ServiceProvider#endpoint(String, Class)} method or
 * {@link ServiceProvider#resourcefulEndpoint(String, Class)} method, returning
 * either an {@link Endpoint} or a {@link ResourcefulEndpoint} respectively. See
 * their Javadoc documentation for more information.
 * 
 * @see ClientConfiguration
 * @see ServiceProvider
 * @see Endpoint
 * @see ResourcefulEndpoint
 */
@PublicEvolving
@Slf4j
public final class SequoiaClient implements ServiceFactory, ServiceForOwnerFactory, Lifecycle {

    private final RegistryClient registryClient;

    private final RegisteredServicesService registeredServicesService;

    private final ServiceFactoryProvider serviceFactoryProvider;

    private final ServiceFactoryProviderWithOwner serviceFactoryProviderWithOwner;

    private static SequoiaClient instance;

    private final ServiceManager serviceManager;

    private final ClientConfiguration clientConfiguration;

    private SequoiaClient(ClientConfiguration config) {
        clientConfiguration = config;
        Injector injector = Guice.createInjector(
                new DataServicesClientModule(clientConfiguration));
        registryClient = injector.getInstance(RegistryClient.class);
        registeredServicesService = new RegisteredServicesService(
                registeredServiceConfig(clientConfiguration), registryClient);
        serviceFactoryProvider = injector.getInstance(clientConfiguration.getServiceFactoryProviderClass());
        serviceFactoryProviderWithOwner = injector.getInstance(clientConfiguration.getServiceFactoryProviderWithOwnerClass());

        serviceManager = new ServiceManager(Collections.singletonList(registeredServicesService));
        serviceManager.startAsync();
    }

    private RegisteredServicesServiceConfiguration registeredServiceConfig(
        ClientConfiguration clientConfig) {
        return builder()
            .refreshIntervalSeconds(
                clientConfig.getServiceRefreshIntervalSeconds())
            .registryServiceOwner(clientConfig.getRegistryServiceOwner())
            .build();
    }

    /**
     * Provides a {@link SequoiaClient} instance.
     */
    public static synchronized SequoiaClient client(
            ClientConfiguration clientConfiguration) {
        if (instance == null) {
            log.debug("Starting sequoia client with configuration [{}]",
                    clientConfiguration);
            instance = new SequoiaClient(clientConfiguration);
        }
        if (!Objects.equal(instance.clientConfiguration, clientConfiguration)) {
            log.debug(
                    "Updating and generating new sequoia client instance with new configuration [{}]",
                    clientConfiguration);
            instance = new SequoiaClient(clientConfiguration);
        }
        return instance;
    }

    public RegistryClient registryClient() {
        return registryClient;
    }

    public HostRegistry getHostRegistry() {
        return registeredServicesService;
    }

    /**
     * Retrieve a {@link ServiceProvider} given the service name.
     */
    @Override
    public ServiceProvider service(String serviceName) {
        return registeredServicesService.getRegisteredService(serviceName)
            .map(getRegisteredService(serviceName))
            .orElseThrow(() -> new NoSuchServiceException(serviceName));
    }

    @Override
    public ServiceProvider service(String serviceName, String owner) {
        return registeredServicesService.getRegisteredService(serviceName, owner)
            .map(getRegisteredServiceForOwner(serviceName, owner))
            .orElseThrow(() -> new NoSuchServiceException(serviceName));
    }

    /**
     * Block awaiting the initialisation of the client. In case of failure it resets the instance.
     */
    @Override
    public void awaitInitialised(int time, TimeUnit timeunit) {
        try {
            serviceManager.awaitHealthy(time, timeunit);
        } catch (TimeoutException | IllegalStateException e) {
            reset();
            throw new SequoiaClientInitialisationException(e);
        }
    }
    
    /**
     * Block awaiting the shutdown of the client.
     */
    @Override
    public void shutdown(int time, TimeUnit timeunit) {
        serviceManager.stopAsync();
        try {
            serviceManager.awaitStopped(time, timeunit);
        } catch (TimeoutException e) {
            throw new SequoiaClientShutdownException(e);
        }
              
    }

    /**
     * Resets instance. In case of failure it allows to reinitialise the client.
     */
    @Override
    public void reset() {
        initInstance();
    }
    
    private static void initInstance() {
        instance = null;
    }

    private Function<RegisteredService, ServiceProvider> getRegisteredService(String serviceName) {
        return srv -> serviceFactoryProvider.from(srv).service(serviceName);
    }

    private Function<RegisteredService, ServiceProvider> getRegisteredServiceForOwner(
        String serviceName, String owner) {
        return srv -> serviceFactoryProviderWithOwner.from(srv, owner).service(serviceName, owner);
    }
    
    
}