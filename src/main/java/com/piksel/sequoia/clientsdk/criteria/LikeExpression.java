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

import com.piksel.sequoia.annotations.Internal;

import lombok.EqualsAndHashCode;

/**
 * Used for selecting values from a partial string.
 * 
 * @see MatchLocation
 */
@Internal
@EqualsAndHashCode(callSuper = true)
public class LikeExpression extends SimpleExpression {

    private final MatchLocation matchLocation;

    public LikeExpression(String propertyName, String value) {
        this(propertyName, value, MatchLocation.ANYWHERE);
    }

    public LikeExpression(String propertyName, String value,
            MatchLocation matchLocation) {
        super(propertyName, value, Operator.LIKE);
        this.matchLocation = matchLocation;
    }

    public void applyExpression(DocumentName documentName, QueryString qs) {
        qs.put(documentName.get().concat(this.getPropertyName()), this.matchLocation
                .toMatchString(String.valueOf(this.getValue())));
    }

}
