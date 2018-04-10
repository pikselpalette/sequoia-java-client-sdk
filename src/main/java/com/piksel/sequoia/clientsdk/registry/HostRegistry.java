package com.piksel.sequoia.clientsdk.registry;

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
