package com.piksel.sequoia.clientsdk.resource;

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

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class MetadataLockFieldValueTest {

    @Test
    public void whenInvalidValueShouldReturnNull() {
        MetadataLockFieldValue metadataLockField = MetadataLockFieldValue.fromString("anything");
        assertNull(metadataLockField);
    }

    @Test
    public void whenValidValueShouldReturnLockAction() {
        final String locks = "locks";
        MetadataLockFieldValue metadataLockField = MetadataLockFieldValue.fromString(locks);
        assertEquals(Objects.toString(metadataLockField), locks);

        metadataLockField = MetadataLockFieldValue.fromString("locking");
        assertEquals(MetadataLockFieldValue.LOCKING, metadataLockField);
    }
}
