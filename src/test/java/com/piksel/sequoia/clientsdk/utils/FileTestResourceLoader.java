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

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Satisfies @TestResource annotated fields with a File.
 */
public class FileTestResourceLoader implements TestResourceLoader {

    @Override
    public boolean canSatisfy(Field field) {
        return isFile(field);
    }

    private boolean isFile(Field field) {
        return field.getType().isAssignableFrom(File.class);
    }

    @Override
    public Object load(URL testResourceLocation,
            TestResourceContext testResourceContext) {
        return new File(testResourceLocation.getFile());
    }

}
