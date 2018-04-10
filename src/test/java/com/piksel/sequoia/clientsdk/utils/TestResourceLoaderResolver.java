package com.piksel.sequoia.clientsdk.utils;

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
