[![Piksel Palette](https://pikselgroup.com/broadcast/wp-content/uploads/sites/3/2017/09/P-P.png)](https://piksel.com/product/piksel-palette/)


Sequoia Java Client SDK
-----------------------

This Client SDK provides convenient access to the Sequoia RESTful services
through a set of Java abstractions. It provides general access methods to
services and endpoints, but leaves the construction of endpoint-specific models
and criteria helpers to user code.

The central idea is that Client SDK allows Java application code to communicate
with the Sequoia RESTful services by exchanging plain Java objects (POJOs) that
inherit from a common type (`Resource`). Users can also search, filter and
select their response collections using a fluent Java API. For example:

```java
results = contentsEndpoint.browse(
                   where(field("title")
                        .equalTo("Game Of Thrones")));
```

This first version supports the _machine-to-machine_ OAuth authorisation mechanism
using a _client credentials authorisation grant_ - more authorisation types are
planned.

The Client SDK supports both business (plain HTTP REST) and resourceful endpoints
using the criteria and resourceful model structure.

## Creating a SequoiaClient
The Sequoia RESTful services have an OAuth token-based authorisation model,
meaning that the Client SDK must first acquire a time-limited access token
before making further requests. As such, users much provide their _username_
and _password_ as part of the `ClientConfiguration` class.

To use the API it is needed to instantiate a SequoiaClient object, passing a
configuration object which set the credentials and the urls for the services
'identity' and 'registry'.

```java

	ClientConfiguration configuration = ClientConfiguration.builder()
        .identityComponentCredentials(new ComponentCredentials("user", "password"))
        .identityHostConfiguration(new HostConfiguration("http://identity.piksel.com"))
        .registryHostConfiguration(new HostConfiguration("http://registry.piksel.com"))
        .build();

	SequoiaClient client = SequoiaClient.client( configuration );
```

### Using an Http Interceptor

You can add an HTTP interceptor to the client so you can perform some actions to every response. 
To do so you just need to specify the class name of your inspector this way:

```java
SequoiaClient client = SequoiaClient.client(configuration.httpResponseInterceptorName(LogResponseInterceptor.class.getName()).build());
```

An example of interceptor could be:

```java
import com.google.api.client.http.HttpResponseInterceptor;

@Slf4j
public class LogResponseInterceptor implements HttpResponseInterceptor {

    private final long startTime = System.nanoTime();

    @Override
    public void interceptResponse(HttpResponse httpResponse) {
        long elapsedNanos = System.nanoTime() - startTime;

        logResponse(httpResponse.getRequest(),
                httpResponse.getStatusCode(),
                TimeUnit.NANOSECONDS.toMillis(elapsedNanos));
    }

    private void logResponse(HttpRequest httpRequest,
                             int statusCode,
                             long elapsedTime) {
        log.info("HTTP {} {} status code: {} time: {} ms",
                httpRequest.getRequestMethod(),
                httpRequest.getUrl().toString(),
                statusCode,
                elapsedTime);
    }
}
```

## Initializing
Once instantiated the Client, it is needed to wait for the initializing to ensure
that a token has been taken, the service list has been retrieved and the Client
SDK is ready to be used.  

```java
	client.awaitInitialised(2, TimeUnit.MINUTES));
```

This is a blocking call and so can be wrapped into a future, such as `CompletableFuture`.

## Creating Resourceful endpoint
A Resourceful endpoint defines the resource on which to perform the operations.

```java
	ResourcefulEndpoint<Brand> brands = client.service("cars").resourcefulEndpoint("brands", Brand.class);
```

## API methods
Following are the list of operations or methods which can be performed on a ResourcefulEndpoint.

### Read
Retrieves one or more resources given their reference and returns the response retrieved.

- ResourceResponse<T> read(Reference ref)
- ResourceResponse<T> read(Collection<Reference> ref)

```java
	ResourceResponse<Brand> response = brands.read(Reference.fromOwnerAndName("test", "porsche"));
```

### Browse
Retrieves the list of resources that matches with the criteria and returns the response.

- ResourceResponse<T> browse(ResourceCriteria criteria)

```java
	ResourceResponse<Brand> response = brands.browse(where(field("country").equalTo("Germany"))
        											 		 .and(field("year").greaterThan("1930"))
        													 .orderByCreatedAt());
```

### Store
Creates one or more resources and returns the response retrieved.

- ResourceResponse<T> store(T resource)
- ResourceResponse<T> store(Collection<T> resources)

```java
	ResourceResponse<Brand> response = brands.store(newBrand);
```

### Update
Updates one resource given its reference and returns the response retrieved.

- ResourceResponse<T> update(T resource, Reference reference)

```java
	ResourceResponse<Brand> response = brands.update(updatedBrand, Reference.fromOwnerAndName("test", "porsche"));
```

### Delete
Deletes one or more resources given their references and returns the response retrieved.

- ResourceResponse<T> delete(Reference reference)
- ResourceResponse<T> delete(Collection<Reference> references)

```java
	ResourceResponse<Brand> response = brands.delete(Reference.fromOwnerAndName("test", "porsche"));
```

## Criteria-Based Fluent API for Requesting Data

The SDK supports a fluent criteria API to abstract client code from
the details of the Sequoia query syntax:

```java
   carsEndpoint.browse(where(field("engine").equalTo("diesel"))
   					.and(field("distribution").equalTo("mechanical"))
   					.orderByCreatedAt());
```

Filter over included resources is also supported:

```java
   carsEndpoint.browse(where(field("engine").equalTo("diesel"))
   					.and(field("distribution").equalTo("mechanical"))
   					.and(include(resource("model")))
   					.and(field("model.type").equalTo("SUV"))
   					.orderByCreatedAt());
```


The following filtering criteria are supported:

#### equalTo

```java
where(field("engine").equalTo("diesel"));
```

Will generate the url encoded criteria expression equivalent to: field=diesel

#### notEqualTo

```java
where(field("engine").notEqualTo("diesel"));
```

Will generate the url encoded criteria expression equivalent to: engine=!diesel

#### oneOrMoreOf

```java
String[] arguments = new String [] { "diesel", "gasoline" };
where(field("engine").oneOrMoreOf(arguments));
```

or

```java
where(field("engine").oneOrMoreOf("diesel", "gasoline"));
```

Will generate the url encoded criteria expression equivalent to: engine=diesel||gasoline

Logical and can be achieved by chaing multiple field and equalTo methods like the following:

```java
where(field("engine").equalsTo("diesel")).and(field("engine").equalsTo("gasoline"))
```

would generate an expression equivalent to: engine=diesel&engine=gasoline

#### startsWith

```java
where(field("engine").startsWith("diesel"));
```

Will generate the url encoded criteria expression equivalent to: engine=diesel*

#### exist

```java
where(field("engine").exist());
```

Will generate the url encoded criteria expression equivalent to: engine=* (any value)

#### notExist

```java
where(field("engine").notExist());
```

Will generate the url encoded criteria expression equivalent to: engine=!*

#### textSearch

```java
where(textSearch("text search"));
```

Will generate the url encoded criteria expression equivalent to: q=text search

#### language

```java
where(...).lang("es");
```

Will generate the url encoded criteria expression equivalent to: lang=es

#### between

```java
where(field("startedAt").between("2014", "2015"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=2014/2015  (between 2014 and 2015 inclusive)

#### notBetween

```java
where(field("startedAt").notBetween("2014", "2015"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=!2014/2015

#### lessThan

```java
where(field("startedAt").lessThan("2014"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=!2014/

#### lessThanOrEqualTo

```java
where(field("startedAt").lessThanOrEqualTo("2014"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=/2014

#### greaterThan

```java
where(field("startedAt").greaterThan("2014"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=!/2014

#### greaterThanOrEqualTo

```java
where(field("startedAt").greaterThanOrEqualTo("2014"));
```

Will generate the url encoded criteria expression equivalent to: startedAt=2014/

### Inclusion of related documents

The SDK support inclusion of related documents up to 1 level (direct relationships).

```java
where(...).include(resource("offers"), resource("channels"));
```

Both, direct and indirect relationships, are allowed. In each case resource's `reference` are needed to perform the mapping.

#### Direct relationship

`@DirectRelationship` MUST de used with a `? extends Resource` or a `Collection<? extends Resource>` field.
`ref` param is the field name with the reference for the linked resource.
`relationship` param is the name of the json block inside `linked` block which groups the linked resources.

##### Example

```java
public class Foo extends Resource {
	@Key
	String barRef;
	
	@DirectRelationship(ref= "barRef", relationship = "bars")
	Bar bar;
}
```

```
{
	"meta": {
		"perPage": 1
	},
	"foos": [
		{
			"ref": "test:foo1",
			"owner": "test",
			"name": "foo1",
			"createdAt": "2016-04-29T14:12:34.734Z",
			"createdBy": "root:sysadmin",
			"updatedAt": "2016-05-23T15:27:16.665Z",
			"updatedBy": "root:system-piksel-task-management",
			"version": "fa9a2db0f5f1dcb8cf0a6166ef2a3a2c4e9fb254",
			"barRef": "owner:bar1"				
		}
	],
	"linked": {
		"bars": [
			{
				"ref": "owner:bar1",
				"someField": "someValue1"
			}
		]
	}
}
```

#### Indirect relationship

`@IndirectRelationship` MUST be used with a `LinkedResourceIterable<? extends Resource>` field where 
`ref` param is the field name with the reference in the linked resource.
`relationship` param is the name of the json block inside `linked` block which groups the linked resources.

##### Example

```java
public class Bar extends Resource {
	
	@IndirectRelationship(ref= "barRef", relationship = "foos")
	LinkedResourceIterable<Foo> foos;
}
```

```
{
	"meta": {
		"perPage": 1,
	},
	"bars": [
		{
			"ref": "test:bar1",
			"owner": "test",
			"name": "bar1",
			"createdAt": "2016-04-29T14:12:34.734Z",
			"createdBy": "root:sysadmin",
			"updatedAt": "2016-05-23T15:27:16.665Z",
			"updatedBy": "root:system-piksel-task-management",
			"version": "fa9a2db0f5f1dcb8cf0a6166ef2a3a2c4e9fb254",
		}
	],
	"linked": {
		"foos": [
			{
				"ref": "owner:foo1",
				"barRef": "owner:bar1",
				"someField": "someValue1"
			}
		]
	}
}
```
* Note that with the introduction of `@IndirectRelationship` `Collection<? extends Resource>` and `List<Optional<? extends Resource>>` are both **deprecated** because these do not support pagination over linked resources

### Selecting fields

The SDK allows to specify which fields will be present in the response, discarding the rest of them.

```java
where(...).fields("name", "title");
```

It is also possible to specify the needed fields in the related documents linked by inclusion.

```java
where(...).include(resource("offers").fields("name", "title"));
```

### Sort Criteria

The SDK supports sort criteria by exposing fluent orderBy query parameters.

Generic fields can be sorted using the orderBy method.

```java
where(...).orderBy("field-name");
```

Common Resourceful sortable fields are mapped to their corresponding fluent method.

```java
where(...).orderByOwner();
where(...).orderByName();
where(...).orderByCreatedAt();
where(...).orderByCreatedBy();
where(...).orderByUpdatedAt();
where(...).orderByUpdatedBy();
```

By default the orderBy is in ascending order. Is it possible to change the order to descending/ascending by using the fluent methods asc() and desc().

```java
where(...).orderBy("field-name").asc();
where(...).orderBy("field-name").desc();
```

It is also possible to specify the needed sort criteria in the related documents linked by inclusion.

```java
where(...).include(resource("offers").orderBy("field-name").asc().orderBy("field-name-1").desc());
```

## Pagination Support

For collection responses that span multiple pages, the returned `ResourceIterable` will transparently load subsequent pages on demand.  This will be performed automatically calling `next()`

```java
ResourceResponse<Brand> response = brands.browse(where(field("country").equalTo("Germany"))
        											 		 .and(field("year").greaterThan("1930"))
        													 .orderByCreatedAt());
response.getPayload().get().next();
```
### PerPage
The SDK supports defining the number of resources prefetched.

```java
ResourceResponse<Brand> response = brands.browse(where(field("year").greaterThan("1930"))
        													 .orderByCreatedAt().perPage(10));
```

### Count

```java
ResourceResponse<Brand> response = brands.browse(where(field("year").greaterThan("1930"))
        													 .orderByCreatedAt().count());
response.getPayload().ifPresent(payload -> {
      payload.totalCount().ifPresent(totalCount -> {
        log.debug("Total brands retrieved [{}]", totalCount);
      });
});        													 
```
Will return `totalCount` on the payload


### Faceted Count

The cardinality of every value of a given field as provided via the `count` query parameter

```java
ResourceResponse<Brand> response = brands.browse(where(field("year").greaterThan("1930"))
        													 .orderByCreatedAt().count("type"));
response.getPayload().ifPresent(payload -> {
      payload.totalCount().ifPresent(totalCount -> {
        log.debug("Total brands retrieved [{}]", totalCount);
      });
      payload.facetCount().ifPresent(facetCount -> {
        log.debug("Total brands retrieved with type "other" [{}]", facetCount.get("type").get("other"));
      });
});        													 
```
Will return `facetCount` on the payload as `Map<String, Map<String, Integer>>` furthermore it will return `totalCount` on the payload

### skipResources

```java
ResourceResponse<Brand> response = brands.browse(where(field("year").greaterThan("1930"))
        													 .orderByCreatedAt().skipResources());
response.getPayload().get().next(); //throws NoSuchElementException     													 
```
Will skip the resources on the payload.

### Active

```java
ResourceResponse<Brand> response = brands.browse(where(active(true)).orderByCreatedAt());        													 
```
Will return a list of actives brands, a list of brands that fulfill the filer active=true

### Available

```java
ResourceResponse<Brand> response = brands.browse(where(available(true)).orderByCreatedAt());        													 
```
Will return a list of available brands, a list of brands that fulfill the filer available=true

### Intersect
```java
ResourceResponse<Content> response = contents.browse(where(field("releaseYear").greaterThan("2015"))
    .intersect("offer", where(field("type").equalTo("linear")).fields("ref"))
    .orderByCreatedAt());        													 
```
Will return a list of contents released after 2015 and with linear offers and it will retrieve only offer's ref

*Important*

FacetCount and Intersections are incompatible. If you add intersections, facet count option will be disabled


## Creating Endpoint
A Endpoint allows to perform operations over non-resources endpoints without Resourceful restrictions.

It defines the response class, request class and the endpoint on which to perform the operations.

```java
	Endpoint<Brand, Model> brands = client.service("cars").endpoint("brands", Brand.class);
```

### Get
Performs a get request over the endpoint.

- Response<T> get()

```java
	Response<Brand> response = brands.get();
```

### Get with headers
Performs a get request over the endpoint using the headers added as parameters.

- Response<T> get(RequestHeaders headers)

```java
	Response<Brand> response = brands.get(withHeaders("header1", "value1").add("header2", 2));
```

### Get with request query params
Performs a get request over the endpoint with parameters added.

- Response<T> get(RequestParams params);

```java
	Response<Brand> response = brands.get(withParams("engine", "diesel").add("color", "red"));
```

### Get with params and headers
Performs a get request over the endpoint with parameters and headers added.

- Response<T> get(RequestParams params, RequestHeaders headers);

```java
	Response<Brand> response = brands.get(withParams("engine", "diesel").add("color", "red"), withHeaders("header1", "value1").add("header2", 2));
```

### Post with headers
Performs a post request over the endpoint.

- Response<T> post(K payload, RequestHeaders headers);

```java
	Response<Brand> response = brands.post(Model.ModelBuilder.name("carrera").build(), withHeaders("header1", "value1").add("header2", 2));
```
### Post
Performs a post request over the endpoint using the headers added.

- Response<T> post(K payload);

```java
	Response<Brand> response = brands.post(Model.ModelBuilder.name("carrera").build());
```

## Built Upon Google OAuth Client Library

The client library builds upon the Google OAuth Client Library to
support transparent _credential management_ such as acquiring access
tokens and refreshing them when needed. The library provides a
pluggable architecture for supplying custom JSON implementations and
transport layer implementations.
