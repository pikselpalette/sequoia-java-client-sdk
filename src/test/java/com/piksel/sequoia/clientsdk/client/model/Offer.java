package com.piksel.sequoia.clientsdk.client.model;

import java.util.Collection;
import java.util.Map;

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
}