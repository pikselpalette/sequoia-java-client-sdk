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

import java.util.Collection;
import java.util.Map;

import com.piksel.sequoia.clientsdk.resource.DirectRelationship;
import com.piksel.sequoia.clientsdk.resource.Resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Offer extends Resource {

    private String type;

    private Map<String, String> localisedTitle;

    private String availabilityStartAt;

    private String availabilityEndAt;

    private Map<String, Double> price;

    private Boolean live;

    private Boolean repeat;

    private Boolean premiere;

    private Boolean active;

    private Collection<String> subscriptionCodes;

    private Collection<String> contentRefs;

    private String termsRef;

    private String offerTemplateRef;

    private String channelRef;

    private Collection<String> locationRefs;

    private String descriptiveContentRef;

    private Collection<String> scopeContentRefs;

    @DirectRelationship(ref = "locationRefs", relationship = "locations")
    private Collection<Location> locations;

    @DirectRelationship(ref = "descriptiveContentRef", relationship = "descriptiveContent")
    private Content descriptiveContent;

    @DirectRelationship(ref = "scopeContentsRefs", relationship = "scopeContents")
    private Collection<Content> scopeContents;

}
