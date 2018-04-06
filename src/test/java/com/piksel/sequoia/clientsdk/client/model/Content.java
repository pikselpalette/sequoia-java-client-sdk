package com.piksel.sequoia.clientsdk.client.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.api.client.util.Key;
import com.piksel.sequoia.clientsdk.resource.DirectRelationship;
import com.piksel.sequoia.clientsdk.resource.IndirectRelationship;
import com.piksel.sequoia.clientsdk.resource.LinkedResourceIterable;
import com.piksel.sequoia.clientsdk.resource.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
