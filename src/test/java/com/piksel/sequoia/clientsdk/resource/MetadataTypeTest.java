package com.piksel.sequoia.clientsdk.resource;

import org.junit.Test;

import static com.piksel.sequoia.clientsdk.resource.MetadataLockValue.MetadataLockActionValue.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetadataTypeTest {

    @Test
    public void givenNewMetadata_shouldNotHaveData() {
        MetadataType metadata = new MetadataType();
        assertNotNull(metadata.getFields());
        assertNotNull(metadata.getLockStatuses());
        assertNotNull(metadata.getLockActions());
    }

    @Test
    public void addLockAction() {
        MetadataType metadata = new MetadataType();
        metadata.addLockAction("path.to.field.a", OVERRIDE);
        assertEquals(OVERRIDE, metadata.getLockActions().getLockValueForField("path.to.field.a"));

        metadata.addLockAction("path.to.field.b", LOCK);
        assertEquals(LOCK, metadata.getLockActions().getLockValueForField("path.to.field.b"));

        metadata.addLockAction("path.to.field.c", UNLOCK);
        assertEquals(UNLOCK, metadata.getLockActions().getLockValueForField("path.to.field.c"));

        assertEquals(3, metadata.getLockActions().getLockValues().size());
        assertEquals(0, metadata.getLockStatuses().getLockValues().size());
    }
}