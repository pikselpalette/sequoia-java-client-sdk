package com.piksel.sequoia.clientsdk.criteria;

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

import java.util.regex.Pattern;

import com.google.api.client.util.Preconditions;
import com.piksel.sequoia.annotations.Internal;

@Internal
final class ArgumentValidation {
    
    private ArgumentValidation() {}

    private static final Pattern FIELDNAMEMATCHER = Pattern
            .compile("[a-zA-Z0-9]+[\\.]*[a-zA-Z0-9]*");

    private static final Pattern RESOURCENAMEMATCHER = Pattern
            .compile("[a-zA-Z]+");

    /**
     * Check that the field name passes the validation rules of field names.
     */
    public static void checkFieldName(String fieldName) {
        String errorMsg = "The field name must match the pattern: "
                + FIELDNAMEMATCHER.toString();
        try {
            Preconditions.checkArgument(
                    FIELDNAMEMATCHER.matcher(fieldName).matches(), errorMsg);
        } catch (IllegalArgumentException e) {
            throw new UnsafeFieldNameException(errorMsg, e);
        } catch (Exception e) {
            throw new CriteriaException(e);
        }
    }

    /**
     * Check that the resource name passes the validation rules of resource names.
     */
    public static void checkResourceName(String fieldName) {
        String errorMsg = "The resource name must match the pattern: "
                + RESOURCENAMEMATCHER.toString();
        try {
            Preconditions.checkArgument(
                    RESOURCENAMEMATCHER.matcher(fieldName).matches(), errorMsg);
        } catch (IllegalArgumentException e) {
            throw new InvalidResourceNameException(errorMsg, e);
        } catch (Exception e) {
            throw new CriteriaException(e);
        }
    }
}
