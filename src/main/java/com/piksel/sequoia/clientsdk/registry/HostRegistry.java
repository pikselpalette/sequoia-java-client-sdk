package com.piksel.sequoia.clientsdk.registry;

import java.util.Optional;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.SequoiaClient;

/**
 * A maintained collection of host services that are available to the client.
 * These clients are retrieved from the <em>service</em> endpoint and kept
 * up-to-date during the course of the {@link SequoiaClient} life time.
 */
@PublicEvolving
public interface HostRegistry {

    /**
     * The host registry has a lifecycle and may not be immediately populated.
     * This method can be used to determine if this {@link HostRegistry host
     * registry} is ready.
     */
    boolean isPopulated();

    /**
     * Supplies a registered service record from a serviceName.
     */
    Optional<RegisteredService> getRegisteredService(String serviceName);

    /**
     * Supplies a registered service record from a serviceName and an owner.
     */
    Optional<RegisteredService> getRegisteredService(String serviceName, String owner);

}