package com.piksel.sequoia.clientsdk.utils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Resolves the {@link TestResourceLoader} from the provided list, and provides
 * an instance.
 * 
 * <p>Simply iterates through the resource loader list finding the first to claim
 * the ability to satisfy the test resource loading.
 */
public class TestResourceLoaderResolver {

    /**
     * Returns a suitable test resource loader or <em>null</em> if none found.
     * 
     * @param extensionLoaders extensions to load
     * @param field applied field
     * @return the test resource loader
     */
    public TestResourceLoader resolve(Set<TestResourceLoader> extensionLoaders,
            Field field) {
        for (TestResourceLoader handler : extensionLoaders) {
            if (handler.canSatisfy(field)) {
                return handler;
            }
        }
        return null;
    }

}