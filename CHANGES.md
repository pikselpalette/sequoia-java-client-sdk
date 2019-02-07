# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## 2.1.5 - RELEASE - 07-FEB-2019

- Upgrade google http client version. This fixes some bugs related to not being able to do a browse using a Map<String, String> with certain headers as `accept` or `content-type`

## 2.1.4 - RELEASE - 29-JAN-2019

- Headers `accept` and `content-type` with value `application/json` added to GET operations

## 2.1.3 - RELEASE - 28-NOV-2018

- Fix NPE when some of the linked resources in an array have the field `statusCode` and some others don't

## 2.1.2 - RELEASE - 15-OCT-2018

- New serializer for `MetadataType` to avoid building the jsonObject when it's empty. It is empty on these cases:

```
"metadata": {}
```

```
"metadata": {
  "fields": {}
}
```

```
"metadata": {
  "fields": {
    "locks": {},
    "locking": {}
  }
}
```

## 2.1.1 - RELEASE - 25-SEP-2018

- The headers are kept in the possible pagination requests

## 2.1.0 - RELEASE - 19-SEP-2018

- Added the option to pass headers parameter to the endpoint

## 2.0.0 - RELEASE - 23-MAY-2018

- Add capability to call services for owners different from the owner which the client was first instantiated.
 
  `client.service("myService", "myOwner")` will use registry service to look for a service named `myService` among all services for owner `myOwner`.

- Extend current functionality to add the owner query param to the url when creating the resourceful endpoint even if the user  does not add when calling the method. 

  `client.service("myService", "myOwner").resourcefulEndpoint("myService", MyClass.class)` 
  
  will generate a URL kind of: `http://myHost:ports/myPath/myService?owner=myOwner`

- RequestClient moved to interface, and added the capability to define at configuration which implementation to be used when creating the client. So this means that
  the implementation of Http requests can be now delegated to the user. Regarding the old implementation it´s being hold in the distribution as the default RequestClient
  implementation, using Google Http libraries.

## 1.9.2 - RELEASE - 21-MAR-2018

- Add `@Key` annotation to `Resource#title` field 

## 1.9.1 - RELEASE - 09-MAR-2018

- Removed dependencies for:
    - WiremockHelper
    - Sequoia Annotation
- The client won't paginate by default to the next page if there isn't any content even though there are pagination links.

## 1.9.0 - RELEASE - 06-FEB-2018

- Resource [common fields](https://gitlab.piksel.com/sequoia-core/sequoia-lib-resourceful#common-fields) added to `Resource` class:
    - title
    - createdBy
    - deletedAt
    - deletedBy
    - metadata
    - reapAt
    - scores
- The field `metadata` is retrieved only when the resource is got directly using its reference, for instance:

```java
ResourceResponse<Content> content = endpoint.read(Reference.fromReference("owner:xxxxxxxxxxxx"));
```

Be careful if you have some classes using any of the above fields, that might cause issues. 

## 1.8.0 - RELEASE - 30-JAN-2018

- The exception `RequestExecutionException` has more details in the message: http verb and URL.
- New possibility of adding an HTTP Interceptor to the client so you can hook your actions to every response, such as logging. Check README file to get more information about how to use it. For more info see [PROCESSING-2464](https://pikselpalette.atlassian.net/browse/PROCESSING-2464)
-  When any exception other then HttpResponseException will occur while executeRequest process then RequestClient rise `RequestExecutionException` with this exception in cause parameter

## 1.7.0 - RELEASE - 08-JAN-2018

- Expose initializeRequest method in RequestClient so that it can be overriden from classes which are extending it, for more info see [PROCESSING-2392](https://pikselpalette.atlassian.net/browse/PROCESSING-2392)
- Credentials are optional instead of mandatory in ClientConfiguration to create SequoiaClient, this allows to call services which don't require authentication using SequoiaClient, for more info see [PROCESSING-2399](https://pikselpalette.atlassian.net/browse/PROCESSING-2399)

## 1.6.1 - RELEASE - 18-DEC-2017

- Fix NPE when linked resources don't contain ref to join with main resources because this reference is not properly requested in the query, WARN message is logged when this situation happens.
For more info see [PROCESSING-2386](https://pikselpalette.atlassian.net/browse/PROCESSING-2386)

## 1.6.0 - RELEASE - 14-DEC-2017

- Performance improvements deserializing and mapping resources. 
- Removed usage of JsonPath.
For more info see [PROCESSING-2365](https://pikselpalette.atlassian.net/browse/PROCESSING-2365)

## 1.5.4 - RELEASE - 12-DEC-2017

- Added support for filtering included resources, for more info see [PROCESSING-2370](https://pikselpalette.atlassian.net/browse/PROCESSING-2370)

## 1.5.3 - RELEASE - 08-DEC-2017

- Performance improvements using caching to avoid deserializing same entities more than once
- Avoid usage of PathNotFoundException, for more info see [PROCESSING-2345](https://pikselpalette.atlassian.net/browse/PROCESSING-2345)

## 1.5.2 - RELEASE - 20-NOV-2017

- Performance improvement handling LinkedResourceIterable, for more info see [PROCESSING-2309](https://pikselpalette.atlassian.net/browse/PROCESSING-2309)

## 1.5.1 - RELEASE - 10-NOV-2017

- Adapt pagination to 5 pages returned by metadata services, for more info see [FOUNDATION-2620](https://pikselpalette.atlassian.net/browse/FOUNDATION-2620)

## 1.5.0 - RELEASE - 18-OCT-2017

- Pagination over linked resources supported
- Added support for intersect https://productdocs.piksel.com/components/foundations/concepts/aggregate-resources.html
- Added support for active and available filters in criteria 

## 1.4.0 - RELEASE - 29-AUG-2017

- Bug fixed when 0 resources are returned in the page NoSuchElementException is not thrown.
- correlationId logged and held in field in RequestExecutionException.
- Improved method *equals* in ClientConfiguration.

## 1.3.0 - RELEASE - 24-AUG-2017

- *reset* method added to LifeCycle interface. This will allow reinitialisation of the SequoiaClient in case of failure and it will be automatically called when awaitInitialised fails.

## 1.2.4 - RELEASE - 17-JUL-2017

- SequoiaClientInitalisationException is thrown when an error occurs during initialization
- Apply toString method in RequestClient logs 

## 1.2.3 - RELEASE - 24-MAR-2017

- Using http://www.mojohaus.org/flatten-maven-plugin/usage.html in order to remove dependencies with the parent.

## 1.2.2 - RELEASE - 5-JAN-2017

- Ignore missing references when `linked.[relationship]` is returning a 404 rather than throwing IncludeResourceException.
- Fix NPE when the returned linked resources don't include ref
- Performance improvement on parsing when response contains linked resources

## 1.2.1 - RELEASE - 15-NOV-2016

- Added support for using multiple query parameters if [filtering lists](https://docs.sequoia.piksel.com/concepts/api/spec.html#filtering-lists)
- Added support for setting RecoveryStrategy: back off and number of retries, when 5xx errors occur
- Related Document Sort and Fields as query parameters passed to the secondary query as prefixes with the relationship

## 1.2.0 - RELEASE - 02-NOV-2016

- Support the ServiceFactoryProvider extension point to allow for the customization 
  of service and endpoint creation by usercode.
- Number support in ResourceList to manage all the numbers as Number instead of as long.
- Allow User-Agent header to be configured through ClientConfiguration
- Fix a bug whereby if the 'next' page returned an empty result, an index out of 
  bounds exception was triggered

## 1.1.1 - RELEASE - 10-OCT-2016

- Specify UTF-8 as the desired JSON encoding rather than relying on the platform default charset
- Fix the resources management with endpoints with hyphen "-" in the url.

## 1.1.0 - RELEASE - 12-SEP-2016

- Add a shaded jar as part of the release for users needing to control the
  dependency (especially Guava)
- All fields (except derived) settable on the Resource.
- Usage of [Wiremock Helper](https://gitlab.piksel.com/sequoia-java-core/wiremock-helper)
- Added support for faceted counts 
- Added support for non-Optional collections for @DirectRelationships and @IndirectRelationships

## 1.0.0 - RELEASE - 12-JUL-2016

First release of the Client SDK including:
- Transparent acquisition and refreshing of access tokens
- Transparent acquisition and management of registered services
- Support for both business and resourceful endpoints
- A fluent criteria-based API for selecting, filtering, expanding/including,
  paging and searching resourceful resources
- Automatically mapped linked resources into Java models
- Pluggable JSON serialization and HTTP transports
- Transparent and lazy pagination of resources
- Built-in validation support via Hibernate Validator
