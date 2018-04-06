package com.piksel.sequoia.clientsdk.utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.w3c.dom.Document;

import com.google.common.collect.Sets;

/**
 * JUnit rule to simplify the loading of test resources from the classpath.
 * 
 * <p>Using the test class's classloader, the resource path specified in
 * the @TestResource annotation will be located and loaded before test execution
 * begins. The strategy used when loading the test resource depends upon the
 * type of the annotated field. Some examples:
 * 
 * <p>Loading a class-relative resource into a {@link String}: <blockquote>
 * 
 * <pre>
 * &#064;TestResource(&quot;test-file.txt&quot;)
 * private String testFile;
 * </pre>
 * 
 * </blockquote> where <tt>test-file.txt</tt> is located within the same package
 * as the test class.
 * Absolute paths may also be specified:
 * <blockquote>
 * 
 * <pre>
 * &#064;TestResource(&quot;/test-file.txt&quot;)
 * private String testFile;
 * </pre>
 * 
 * </blockquote> Here the resource is located relative from the classpath of the
 * test class's classloader.
 * 
 * <p>The rule is capable of inferring the target type of the annotated field and
 * performing type conversion as appropriate. A number of target types are
 * supported by default:
 * 
 * <p>Loading a classpath resource into a String and XML {@link Document}:
 * <blockquote>
 * 
 * <pre>
 * &#064;TestResource(&quot;test-file.txt&quot;)
 * private File testFile;
 * 
 * &#064;TestResource(&quot;test-file.xml&quot;)
 * private Document xmlDocument;
 * </pre>
 * </blockquote>
 * 
 * <p>Finally, the rule supports the registration of additional
 * {@link TestResourceLoader loaders} for loading types not covered by the
 * default set.
 */
public class TestResourceRule implements TestRule {

    private final Set<TestResourceLoader> extensionHandlers;
    private final Set<TestResourceLoader> defaultTestResourceLoaders = defaultLoaders(
            new StringTestResourceLoader(), new FileTestResourceLoader(),
            new XmlDocumentTestResourceLoader(),
            new SchemaTestResourceLoader());
    private final Object testClass;

    private TestResourceUrlLocator testResourceLocator = new TestResourceUrlLocator();
    private TestResourceLoaderResolver testResourceLoaderResolver = new TestResourceLoaderResolver();

    /**
     * Create the test resource rule.
     */
    public TestResourceRule(Object testObject, TestResourceLoader... extensionHandlers) {
        this.extensionHandlers = Sets.union(setFrom(extensionHandlers),
                defaultTestResourceLoaders);
        this.testClass = testObject;
    }

    /**
     * Create the test resource rule.
     */
    public TestResourceRule(Object testClass, Set<TestResourceLoader> extensionHandlers) {
        this.extensionHandlers = Sets.union(extensionHandlers,
                defaultTestResourceLoaders);
        this.testClass = testClass;
    }

    private Set<TestResourceLoader> setFrom(TestResourceLoader... loaders) {
        return new HashSet<>(Arrays.asList(loaders));
    }

    @Override
    public Statement apply(final Statement base,
            final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                applyTestResources(description);
                base.evaluate();
            }
        };
    }

    private void applyTestResources(Description description) {
        Class<?> testClass = description.getTestClass();
        processFields(testClass.getDeclaredFields(), description);
    }

    private void processFields(Field[] fields, Description description) {
        for (Field field : fields) {
            if (isTestResourceAnnotated(field)) {
                processField(field, description);
            }
        }
    }

    private boolean isTestResourceAnnotated(Field field) {
        return field.isAnnotationPresent(TestResource.class);
    }

    private void processField(Field field, Description description) {
        URL testResourceLocation = loadUrlFromField(field);
        TestResourceLoader handler = locateResourceLoader(field);
        TestResourceContext testResourceContext = createTestResourceContext(
                field, description);
        loadResourceAndApplyToField(field, testResourceLocation, handler,
                testResourceContext);
    }

    private void loadResourceAndApplyToField(Field field,
            URL testResourceLocation, TestResourceLoader handler,
            TestResourceContext testResourceContext) {
        Object fieldObject = handler.load(testResourceLocation,
                testResourceContext);
        updateField(field, fieldObject);
    }

    private TestResourceLoader locateResourceLoader(Field field)
            throws AssertionError {
        TestResourceLoader handler = testResourceLoaderResolver
                .resolve(extensionHandlers, field);
        if (handler == null) {
            throw new AssertionError(
                    "Unable to find test resource loader to satisfy field: "
                            + field.getName() + ". "
                            + "Consider registering a custom handler for field type: "
                            + field.getType());
        }
        return handler;
    }

    private URL loadUrlFromField(Field field) {
        return testResourceLocator.locate(field);
    }

    private void updateField(Field field, Object fieldObject) {
        field.setAccessible(true);
        try {
            field.set(testClass, fieldObject);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Set<TestResourceLoader> defaultLoaders(
            TestResourceLoader... resourceLoaders) {
        return new HashSet<>(Arrays.asList(resourceLoaders));
    }

    private TestResourceContext createTestResourceContext(Field field,
            Description description) {
        return new TestResourceContext(field, description, testClass);
    }
}