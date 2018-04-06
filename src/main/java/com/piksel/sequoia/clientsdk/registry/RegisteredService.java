package com.piksel.sequoia.clientsdk.registry;

import com.google.api.client.util.Key;
import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.resource.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PublicEvolving
public class RegisteredService extends Resource {

    public RegisteredService(String location, String name) {
        this.location = location;
        this.setName(name);
    }

    @Key
    private String location;

}