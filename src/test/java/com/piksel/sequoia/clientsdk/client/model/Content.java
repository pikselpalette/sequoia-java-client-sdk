package com.piksel.sequoia.clientsdk.client.model;

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

import com.google.api.client.util.Key;
import com.piksel.sequoia.clientsdk.resource.DirectRelationship;
import com.piksel.sequoia.clientsdk.resource.IndirectRelationship;
import com.piksel.sequoia.clientsdk.resource.LinkedResourceIterable;
import com.piksel.sequoia.clientsdk.resource.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Content extends Resource {

    @Key
    private Collection<String> categoryRefs;

    @Key
    private Map<String, String> localisedTitle;

    @Key
    private Map<String, String> localisedShortSynopsis;

    @Key
    private String type;

    @DirectRelationship(ref = "categoryRefs", relationship = "categories")
    private List<Optional<Category>> categories;

    @DirectRelationship(ref = "categoryRefs", relationship = "categories")
    private Collection<Category> categoriesCollection;

    @IndirectRelationship(ref = "contentRef", relationship = "assets")
    private List<Optional<Asset>> assets;

    @IndirectRelationship(ref = "contentRef", relationship = "assets")
    private LinkedResourceIterable<Asset> assetsPagination;

    @IndirectRelationship(ref = "contentRef", relationship = "assets")
    private Collection<Asset> assetsCollection;

    @IndirectRelationship(ref = "contentRefs", relationship = "offers")
    private LinkedResourceIterable<Offer> offers;
    
    @IndirectRelationship(ref = "contentRefs", relationship = "offers")
    private Collection<Offer> offersCollection;

}
