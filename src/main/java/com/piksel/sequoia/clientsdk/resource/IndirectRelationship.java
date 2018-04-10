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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Marks a field should hold indirect (where the relationship is from
 * the target resource to this resource) in the response resource 
 * object.
 * 
 * <p>In the below diagram you can see that the linked resource has 
 * a reference on the field <code>fooRef</code> which refers back to 
 * the source resource.
 * <pre>
 * +--|this|------+                  +--|linked|----+
 * |ref:123       |                  |              |
 * |              |     Indirect     |              |
 * |              &lt;------------------+ fooRef:123   |
 * |              |                  |              |
 * |              |                  |              |
 * |              |                  |              |
 * +--------------+                  +--------------+
 * </pre>
 * 
 * @see DirectRelationship
 */
@PublicEvolving
@Retention(RetentionPolicy.RUNTIME)
public @interface IndirectRelationship {
    
    /**
     * The name of the related resource field name that contains the back 
     * reference to this resource.
     */
    String ref();

    /**
     * 
     */
    String relationship();
}
