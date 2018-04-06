package com.piksel.sequoia.clientsdk.resource;

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