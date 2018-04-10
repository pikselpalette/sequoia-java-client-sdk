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

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ClientException;

@PublicEvolving
public class CriteriaException extends ClientException {

    private static final long serialVersionUID = 1L;

    public CriteriaException(Exception e) {
        super(e);
    }

    public CriteriaException(String message, Exception cause) {
        super(message, cause);
    }

    public CriteriaException(String message) {
        super(message);
    }

}
