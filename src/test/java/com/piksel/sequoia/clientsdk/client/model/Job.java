package com.piksel.sequoia.clientsdk.client.model;

import java.util.Collection;

import com.piksel.sequoia.clientsdk.resource.IndirectRelationship;
import com.piksel.sequoia.clientsdk.resource.Resource;

import lombok.Getter;

@Getter
public class Job extends Resource {

    @IndirectRelationship(ref = "jobRef", relationship = "events")
    private Collection<Event> events;

}
