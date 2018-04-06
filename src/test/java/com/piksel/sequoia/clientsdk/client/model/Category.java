package com.piksel.sequoia.clientsdk.client.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.google.api.client.util.Key;
import com.piksel.sequoia.clientsdk.resource.IndirectRelationship;
import com.piksel.sequoia.clientsdk.resource.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends Resource {
    
    @Key
    private String value;
    
    @Key
    private String scheme;

    @IndirectRelationship(ref = "categoryRefs", relationship = "contents")
    private List<Optional<Content>> contents;
    
    @IndirectRelationship(ref = "categoryRefs", relationship = "contents")
    private Collection<Content> contentsCollection;
}
