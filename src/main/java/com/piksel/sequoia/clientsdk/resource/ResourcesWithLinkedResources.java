package com.piksel.sequoia.clientsdk.resource;

import java.util.List;

import com.google.gson.JsonElement;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResourcesWithLinkedResources<T extends Resource> {

    List<T> resources;

    JsonElement linkedResources;

    JsonElement meta;

}
