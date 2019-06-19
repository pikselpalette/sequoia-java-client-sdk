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

import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForRegistryClientForNewOwner;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForRegistryGet;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryBrowseWithFault;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryDelete;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryDeleteWithFault;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryDynamicPost;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryGet;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryGetNotFoundResponse;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryGetQueryParams;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryGetWithFault;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryPost;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryPostWithFault;
import static com.piksel.sequoia.clientsdk.client.integration.MockResponses.stubForServiceRegistryUpdate;
import static com.piksel.sequoia.clientsdk.criteria.Inclusion.resource;
import static com.piksel.sequoia.clientsdk.criteria.StringExpressionFactory.field;
import static com.piksel.sequoia.clientsdk.resource.Resources.where;
import static com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings.OWNER;
import static com.piksel.sequoia.wiremock.helper.WiremockHelper.prepareServicesMappings;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.ExponentialBackOff;
import com.piksel.sequoia.clientsdk.RequestExecutionException;
import com.piksel.sequoia.clientsdk.ResourceResponse;
import com.piksel.sequoia.clientsdk.SequoiaClient;
import com.piksel.sequoia.clientsdk.client.integration.ClientIntegrationTestBase;
import com.piksel.sequoia.clientsdk.client.integration.MockResponses;
import com.piksel.sequoia.clientsdk.configuration.ClientConfiguration;
import com.piksel.sequoia.clientsdk.configuration.DefaultClientConfiguration;
import com.piksel.sequoia.clientsdk.recovery.RecoveryStrategy;
import com.piksel.sequoia.clientsdk.registry.RegisteredService;
import com.piksel.sequoia.clientsdk.registry.RegistryClient;
import com.piksel.sequoia.clientsdk.registry.service.ServiceProvider;
import com.piksel.sequoia.clientsdk.resource.DefaultResourceCriteria;
import com.piksel.sequoia.clientsdk.resource.Reference;
import com.piksel.sequoia.clientsdk.resource.ReferenceValidationException;
import com.piksel.sequoia.clientsdk.resource.ReferencesMismatchException;
import com.piksel.sequoia.clientsdk.resource.Resource;
import com.piksel.sequoia.clientsdk.resource.ResourceEndpointHandler;
import com.piksel.sequoia.clientsdk.resource.ResourceValidationException;
import com.piksel.sequoia.clientsdk.resource.ResourcefulEndpoint;
import com.piksel.sequoia.clientsdk.utils.TestResource;
import com.piksel.sequoia.clientsdk.utils.TestResourceRule;
import com.piksel.sequoia.wiremock.helper.ScenarioHttpMappings;
import com.piksel.sequoia.wiremock.helper.WiremockHelper;

public class SequoiaClientTest extends ClientIntegrationTestBase {

    private static final Reference REF_RESOURCE_TO_DELETE = Reference.fromReference("test:resourceToDelete");

    private static final int BATCH_RESOURCES_SIZE = 10;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TestResourceRule testResourceRule = new TestResourceRule(this);

    @TestResource("registry-response-one.json")
    private String responseOneResource;

    @TestResource("registry-get-response-multi.json")
    private String getResponseMultiResource;

    @TestResource("registry-get-response-multi-count.json")
    private String getResponseMultiResourceCount;
 
    @TestResource("registry-get-response-linked-resources.json")
    private String getResponseLinkedResources;

    @TestResource("registry-post-response-batch.json")
    private String postResponseBatchResource;

    @TestResource("service-to-update.json")
    private static String serviceToUpdate;

    @TestResource("registry-response-testmock.json")
    private String responseTestmockResource;

    @Test
    public void shouldProvideAccessToRegistryClient() {
        RegistryClient registryClient = client.registryClient();
        assertNotNull(registryClient);
    }

    @Test
    public void service_shouldProvideAService() throws Exception {
        getHostRegistryAndWaitForItToBePopulated(client);
        ServiceProvider serviceProvider = client.service("registry");
        assertNotNull(serviceProvider);
        commonVerifications();
    }

    @Test
    public void shouldProvideServiceForNewOwner() throws Exception {
        stubForRegistryClientForNewOwner(scenarioMappings, responseTestmockResource, 201, "testmock");
        getHostRegistryAndWaitForItToBePopulated(client);
        ServiceProvider serviceProvider = client.service("workflow", "testmock");
        assertNotNull(serviceProvider);
        commonVerifications();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void whenInstantiateServiceForNewOwnerAndGenerateResourcefullEndpointShouldAddOwner()
        throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
        stubForRegistryClientForNewOwner(scenarioMappings, responseTestmockResource, 201,
            "testmock");

        ResourceEndpointHandler<RegisteredService> response = (ResourceEndpointHandler) client
            .service("workflow", "testmock")
            .resourcefulEndpoint("services", RegisteredService.class);

        Field genericUrlField = response.getClass().getSuperclass().getDeclaredField("endpointUrl");
        genericUrlField.setAccessible(true);
        GenericUrl genericUrl = (GenericUrl) genericUrlField.get(response);

        assertThat("Endpoint url should contain owner as query param", genericUrl.build(),
            equalTo("http://127.0.0.1:8119/data/services?owner=testmock"));
    }

    @Test
    public void endpoint_shouldProvideOneEndpoint() {
        ResourcefulEndpoint<Resource> resourceEndpoint = client.service("registry").resourcefulEndpoint("services", Resource.class);
        assertNotNull(resourceEndpoint);
    }

    @Test
    public void endpoint_shouldProvideOneDynamicEndpoint() {
        ResourcefulEndpoint<Resource> resourceEndpoint = client.service("registry").resourcefulEndpoint("test", "services", Resource.class);
        assertNotNull(resourceEndpoint);
    }

    @Test
    public void whenStoringWithInvalidResource_andThrowResourceValidationException() {
        thrown.expect(ResourceValidationException.class);
        client.service("registry").resourcefulEndpoint("services", Resource.class).store(new Resource());
    }

    @Test
    public void whenStoring_shouldPostToDataServicesEndpoint_andReturn_201() {
        stubForServiceRegistryPost(scenarioMappings, responseOneResource, 201);

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .store(getRegisteredServiceToCreate());

        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services")));
        commonSingleResponseVerifications(response);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenStoring_shouldPostToDataServicesDynamicEndpoint_andReturn_201() {
        stubForRegistryGet(scenarioMappings, "test", responseOneResource);
        stubForServiceRegistryDynamicPost(scenarioMappings, responseOneResource, 201);

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("test", "services", RegisteredService.class)
                .store(getRegisteredServiceToCreate());

        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services?owner=test")));
        commonSingleResponseVerifications(response);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenReadingWithOwnerAndName_addReferenceToUrl_andReturn200() {
        stubForServiceRegistryGet(scenarioMappings, responseOneResource, 200, "owner:registry");

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .read(Reference.fromOwnerAndName("owner", "registry"));

        commonVerifications();
        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/data/services/owner:registry")));
        commonSingleResponseVerifications(response);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void whenGetA500_performing_an_operation_should_retry_3_times_using_BackOff_configured() {
        stubForServiceRegistryGet(scenarioMappings, responseOneResource, 500, "owner:registry500");
        boolean fail = false;
        try {
            client.service("registry").resourcefulEndpoint("services", RegisteredService.class).read(Reference.fromOwnerAndName("owner", "registry500"));
        } catch (Exception e) {
            fail = true;
            // Expected.
            scenarioMappings.verify("registry", 4, getRequestedFor(urlEqualTo("/data/services/owner:registry500")));
        }
        assertThat(fail, is(true));

    }
    
    @Test
    public void whenGetA400_performing_an_operation_should_not_retry() {
        stubForServiceRegistryGet(scenarioMappings, responseOneResource, 400, "owner:registry400");
        boolean fail = false;
        try {
            client.service("registry").resourcefulEndpoint("services", RegisteredService.class).read(Reference.fromOwnerAndName("owner", "registry400"));
        } catch (Exception e) {
            fail = true;
            // Expected.
            scenarioMappings.verify("registry", 1, getRequestedFor(urlEqualTo("/data/services/owner:registry400")));
        }
        assertThat(fail, is(true));

    }

    @Test
    public void whenReadingWithMultipleReferences_addReferencesToUrl_andReturn200() {
        stubForServiceRegistryGet(scenarioMappings, getResponseMultiResource, 200, "test:registry,test:services");
        Collection<Reference> references = new ArrayList<>();
        references.add(Reference.fromOwnerAndName("test", "registry"));
        references.add(Reference.fromOwnerAndName("test", "services"));

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .read(references);

        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/data/services/test:registry,test:services")));
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getPayload());
    }

    @Test
    public void whenReadingWithNonExistentResource_andThrowRequestExecutionException() {

        stubForServiceRegistryGetNotFoundResponse(scenarioMappings, "owner:registry");

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .read(Reference.fromOwnerAndName("owner", "registry"));

        assertFalse(response.isSuccessStatusCode());
        assertEquals(404, response.getStatusCode());
        assertThat(response.getPayload(), is(Optional.empty()));
    }

    @Test
    public void whenReadingWithInvalidReference_andThrowIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).read(Reference.fromReference("owner"));
    }

    @Test
    public void whenReadingWithGettingFault_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage("Unable to complete request");
        stubForServiceRegistryGetWithFault(scenarioMappings, null, "owner:registry");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).read(Reference.fromOwnerAndName("owner", "registry"));
    }

    @Test
    public void whenBrowsingWithCriteria_addCriteriaToUrl_andReturn200() {
        stubForServiceRegistryGetQueryParams(scenarioMappings, getResponseMultiResource, 200, "withFieldName=field%20value");
        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.add(field("fieldName").equalTo("field value"));

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .browse(criteria);

        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/data/services?withFieldName=field%20value")));
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getPayload());
    }

    @Test
    public void whenBrowsingWithCount_addCriteriaToUrl_andReturn200() {
        stubForServiceRegistryGetQueryParams(scenarioMappings, getResponseMultiResourceCount, 200, "count=type");
        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.count("type");

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .browse(criteria);

        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/data/services?count=type")));
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getPayload());
        assertEquals(2, response.getPayload().get().facetCount().get().get("type").get("core").intValue());
        assertEquals(2, response.getPayload().get().totalCount().get().intValue());
    }

    @Test
    public void whenBrowsingWithCriteria_includeRelatedDocument_andReturn200() {
        stubForServiceRegistryGetQueryParams(scenarioMappings, getResponseLinkedResources, 200, "include=jobs,tasks,events");
        DefaultResourceCriteria criteria = new DefaultResourceCriteria();
        criteria.include(resource("jobs"), resource("tasks"), resource("events"));

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .browse(criteria);

        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/data/services?include=jobs,tasks,events")));
        assertTrue(response.isSuccessStatusCode());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getPayload());
    }

    @Test
    public void whenBrowsingWithGettingFault_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage("Unable to complete request");
        stubForServiceRegistryBrowseWithFault(scenarioMappings, "withFieldName=value");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).browse(where(field("fieldName").equalTo("value")));
    }

    @Test
    public void whenStoring_shouldPostToDataServicesEndpoint_andReturn_200() {
        stubForServiceRegistryPost(scenarioMappings, responseOneResource, 200);

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .store(getRegisteredServiceToCreate());

        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services")));
        commonSingleResponseVerifications(response);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void whenStoringWithGettingFault_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage("Unable to complete request");
        stubForServiceRegistryPostWithFault(scenarioMappings);

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).store(getRegisteredServiceToCreate());

        commonVerifications();
        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services")));
    }

    @Test
    public void whenStoringCollection_shouldPostToDataServicesEndpoint_andReturn_201() {
        stubForServiceRegistryPost(scenarioMappings, postResponseBatchResource, 201);

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .store(getRegisteredServices());

        commonBatchVerifications(response);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenStoringCollection_shouldPostToDataServicesEndpoint_andReturn_200() {
        stubForServiceRegistryPost(scenarioMappings, postResponseBatchResource, 200);

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .store(getRegisteredServices());

        commonBatchVerifications(response);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void whenStoringCollectionWithGettingFault_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage("Unable to complete request");
        stubForServiceRegistryPostWithFault(scenarioMappings);

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).store(getRegisteredServices());

        commonVerifications();
        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services")));
    }

    @Test
    public void whenStoringCollectionWithInvaliCollectionOfResources_andThrowResourceValidationException() {
        Collection<Resource> resources = new ArrayList<>();
        resources.add(getRegisteredServiceToCreate());
        resources.add(new Resource());
        thrown.expect(ResourceValidationException.class);
        thrown.expectMessage("owner may not be empty - resource number 1 in the collection");

        client.service("registry").resourcefulEndpoint("services", Resource.class).store(resources);
    }

    @Test
    public void whenDeleting_addReferenceToTheUrl_andReturn_204() {
        stubForServiceRegistryDelete(scenarioMappings, null, 204, REF_RESOURCE_TO_DELETE.toString());

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .delete(REF_RESOURCE_TO_DELETE);

        assertEquals(response.getStatusCode(), 204);
        assertTrue(response.isSuccessStatusCode());
        assertThat(response.getPayload(), is(Optional.empty()));
        scenarioMappings.verify("registry", deleteRequestedFor(urlEqualTo("/data/services/test:resourceToDelete")));
    }

    @Test
    public void whenDeletingWithInvalidReference_andThrowIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).delete(Reference.fromReference("owner"));
    }

    @Test
    public void whenDeletingWithInvalidPatternReference_andThrowReferenceValidationException() {
        thrown.expect(ReferenceValidationException.class);

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .delete(Reference.fromReference("owner: name with spaces"));
    }

    @Test
    public void whenDeletingWithGettingBadResponse_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage(startsWith("Unable to complete request [DELETE http://127.0.0.1:"));
        thrown.expectMessage(endsWith("/data/services/test:resourceToDelete] - exception [com.google.api.client.http.HttpResponseException: ] - correlationId [null]"));
        stubForServiceRegistryDeleteWithFault(scenarioMappings, null, REF_RESOURCE_TO_DELETE.toString());

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).delete(REF_RESOURCE_TO_DELETE);
    }

    @Test
    public void whenDeletingCollection_addReferencesToTheUrl_andReturn_204() {
        stubForServiceRegistryDelete(scenarioMappings, null, 204, getRefs(getReferencesToDelete()));

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .delete(getReferencesToDelete());

        assertEquals(response.getStatusCode(), 204);
        assertTrue(response.isSuccessStatusCode());
        scenarioMappings.verify("registry",
                deleteRequestedFor(urlEqualTo("/data/services/test:resourceToDelete,test:resourceToDelete,"
                        + "test:resourceToDelete,test:resourceToDelete," + "test:resourceToDelete,test:resourceToDelete,"
                        + "test:resourceToDelete,test:resourceToDelete," + "test:resourceToDelete,test:resourceToDelete")));
    }

    @Test
    public void whenUpdating_addReferencesToTheUrl_performPut_andReturn_200() {
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 200, "owner:services");

        ResourceResponse<RegisteredService> response = client.service("registry").resourcefulEndpoint("services", RegisteredService.class)
                .update(getRegisteredServiceToUpdate(), Reference.fromOwnerAndName("owner", "services"));

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.isSuccessStatusCode());
        scenarioMappings.verify("registry", putRequestedFor(urlEqualTo("/data/services/owner:services")));
        assertEquals("356b54eb90cd04566159dfb9c95a0426998a2f7b", response.getPayload().get().single().getVersion());
    }

    @Test
    public void whenUpdatingWithGetting412_addReferencesToTheUrl_performPut_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 412, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(getRegisteredServiceToUpdate(),
                Reference.fromOwnerAndName("owner", "services"));

        scenarioMappings.verify("registry", putRequestedFor(urlEqualTo("/data/services/owner:services")));
    }

    @Test
    public void whenUpdatingWithGetting412_addReferencesToTheUrl_performPut_andThrowRequestExecutionExceptionWithStatusCodeOnThis() {
        try {
            stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 412, "owner:services");

            client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(getRegisteredServiceToUpdate(),
                    Reference.fromOwnerAndName("owner", "services"));
        } catch (RequestExecutionException e) {
            assertThat(e.getStatusCode(), is(412));
        }

        scenarioMappings.verify("registry", putRequestedFor(urlEqualTo("/data/services/owner:services")));
    }

    @Test
    public void whenUpdatingWithInvalidPatternOwner_andThrowReferenceValidationException() {
        thrown.expect(ResourceValidationException.class);
        RegisteredService registeredService = getRegisteredServiceToUpdate();
        registeredService.setOwner("ow ner");
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 200, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(registeredService,
                Reference.fromOwnerAndName("owner", "services"));

    }

    @Test
    public void whenUpdatingWithDifferentReferenceInResourceAndAsParameterInTheMethod_andThrowReferenceValidationException() {
        thrown.expect(ReferencesMismatchException.class);
        thrown.expectMessage("Reference to update owner:services doesn't match with the resource reference owner:servicas");
        RegisteredService registeredService = getRegisteredServiceToUpdate();
        registeredService.setOwner("owner");
        registeredService.setName("servicas");
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 200, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(registeredService,
                Reference.fromOwnerAndName("owner", "services"));

    }

    @Test
    public void whenUpdatingWithDifferentReferenceOwnerNameInResourceAndAsParameterInTheMethod_andThrowReferenceValidationException() {
        thrown.expect(ReferencesMismatchException.class);
        thrown.expectMessage("Reference to update owner:services doesn't match with the resource reference owner:servicas");
        RegisteredService registeredService = getRegisteredServiceToUpdate();
        registeredService.setOwner("owner");
        registeredService.setName("servicas");
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 200, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(registeredService,
                Reference.fromOwnerAndName("owner", "services"));

    }

    @Test
    public void whenUpdatingWithResouceWithoutVersion_andThrowResourceValidationException() {
        thrown.expect(ResourceValidationException.class);
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 200, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(getRegisteredServiceToCreate(),
                Reference.fromOwnerAndName("owner", "services"));

    }

    @Test
    public void whenUpdatingGettingBadRequest_andThrowRequestExecutionException() {
        thrown.expect(RequestExecutionException.class);
        thrown.expectMessage(containsString("mycorrelationId"));
        stubForServiceRegistryUpdate(scenarioMappings, responseOneResource, 400, "owner:services");

        client.service("registry").resourcefulEndpoint("services", RegisteredService.class).update(getRegisteredServiceToUpdate(),
                Reference.fromOwnerAndName("owner", "services"));

        scenarioMappings.verify("registry", putRequestedFor(urlEqualTo("/data/services/owner:services")));
    }

    @Test
    public void whenCreatingSequoiaClient_andInstanceIsAlreadyGenerated_andConfigurationIsTheSame_shouldReturnSameInstance() {
        ClientConfiguration configuration = withClientConfiguration();
        SequoiaClient sequoiaClient = SequoiaClient.client(configuration);
        SequoiaClient sequoiaClient1 = SequoiaClient.client(configuration);
        assertThat("Instances should be just equal", sequoiaClient==sequoiaClient1, is(true));
    }

    @Test
    public void whenResetSequoiaClient_andCreateSequoiaClient_shouldReturnNewInstance() {
        ClientConfiguration configuration = withClientConfiguration();
        SequoiaClient sequoiaClient = SequoiaClient.client(configuration);
        sequoiaClient.reset();
        SequoiaClient sequoiaClient1 = SequoiaClient.client(configuration);
        assertThat("It should have generated new instance", sequoiaClient==sequoiaClient1, is(false));
    }
    
    @Test
    public void whenSequoiaClientIsCreatedWithoutCredentials_shouldNotCallIdentity_callToRegistryShouldNotHaveAuthHeaders() {
        ClientConfiguration configuration = withClientConfigurationWithoutCredentiasl();
        SequoiaClient sequoiaClient = SequoiaClient.client(configuration);
        WiremockHelper.setClientInitialiazer(() -> sequoiaClient);
        WiremockHelper.setServiceCreator(registeredServiceCreationFunction);
        WiremockHelper.setServicesList(Collections.singletonList("registry"));
        ScenarioHttpMappings scenarioMappings = prepareServicesMappings(testName.getMethodName());
        scenarioMappings.verify("registry", getRequestedFor(urlEqualTo("/services/unitTestRoot")).withoutHeader("authorization"));
    }

    private ClientConfiguration withClientConfigurationWithoutCredentiasl() {
        return ClientConfiguration.builder().identityHostConfiguration(identityHostConfiguration())
                .registryHostConfiguration(registryHostConfiguration())
                .recoveryStrategy(RecoveryStrategy.builder()
                        .backOff(new ExponentialBackOff.Builder().setInitialIntervalMillis(1).setMaxIntervalMillis(5).build()).numberOfRetries(3)
                        .build())
                .registryServiceOwner(OWNER).build();
    }

    private void commonSingleResponseVerifications(ResourceResponse<RegisteredService> response) {
        assertTrue(response.isSuccessStatusCode());
        RegisteredService returnedElement = response.getPayload().get().single();
        assertEquals("356b54eb90cd04566159dfb9c95a0426998a2f7b", returnedElement.getVersion());
        assertNotNull(returnedElement);
    }

    private void commonBatchVerifications(ResourceResponse<RegisteredService> response) {
        scenarioMappings.verify("registry", postRequestedFor(urlEqualTo("/data/services")));
        assertTrue(response.isSuccessStatusCode());
        assertEquals("356b54eb90cd04566159dfb9c95a0426998a2f7b", response.getPayload().get().next().getVersion());
        assertTrue(response.getPayload().get().hasNext());
    }

    private static RegisteredService getRegisteredServiceToCreate() {
        RegisteredService registeredService = MockResponses.newService("services", "services", 1111);
        registeredService.setOwner("owner");
        return registeredService;
    }

    private static RegisteredService getRegisteredServiceToUpdate() {
        return DefaultClientConfiguration.getDefaultGson().fromJson(serviceToUpdate, RegisteredService.class);
    }

    private static Collection<RegisteredService> getRegisteredServices() {
        Collection<RegisteredService> registeredServices = new ArrayList<>();
        for (int i = 0; i < BATCH_RESOURCES_SIZE; i++) {
            registeredServices.add(getRegisteredServiceToCreate());
        }
        return registeredServices;
    }

    private static Collection<Reference> getReferencesToDelete() {
        Collection<Reference> references = new ArrayList<>();
        for (int i = 0; i < BATCH_RESOURCES_SIZE; i++) {
            references.add(REF_RESOURCE_TO_DELETE);
        }
        return references;
    }

    private String getRefs(Collection<Reference> references) {
        return references.stream().map(Reference::toString).collect(Collectors.joining(","));
    }

}
