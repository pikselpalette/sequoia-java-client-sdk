package com.piksel.sequoia.clientsdk.resource;

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

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ClientException;

/**
 * Indicates that an update operation was attempted with a reference that 
 * does not match the payload.
 */
@PublicEvolving
public class ReferencesMismatchException extends ClientException {

    private static final long serialVersionUID = 2790753074429742887L;

    private ReferencesMismatchException(String message) {
        super(message);
    }

    /**
     * Create the exception providing the reference used for the update, and the 
     * reference found on the resource.
     */
    public static ReferencesMismatchException thrown(String referenceToUpdate, String resourceReference) {
        return new ReferencesMismatchException(String.format(
                "Reference to update %s doesn't match with the resource reference %s.",
                referenceToUpdate, resourceReference));
    }

}
