package com.piksel.sequoia.clientsdk.client.model;

import com.google.api.client.util.Key;
import com.piksel.sequoia.clientsdk.resource.DirectRelationship;
import com.piksel.sequoia.clientsdk.resource.Resource;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Asset extends Resource {

    @Key
    private String contentRef;
    
    @Key
    private String mediaType;
    
    @Key
    private String url;    

    @DirectRelationship(ref = "contentRef", relationship = "content")
    private Optional<Content> content;

    @DirectRelationship(ref = "contentRef", relationship = "content")
    private Content directContent;
}
