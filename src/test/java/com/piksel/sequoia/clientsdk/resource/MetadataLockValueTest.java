package com.piksel.sequoia.clientsdk.resource;

import org.junit.Test;

import static org.junit.Assert.*;

public class MetadataLockValueTest {

    @Test
    public void status_valueOf() {
        MetadataLockValue actual = MetadataLockValue.valueOf(MetadataLockValue.MetadataLockStatusValue.LOCKED.toString());
        assertEquals(MetadataLockValue.MetadataLockStatusValue.LOCKED, actual);
    }

    @Test
    public void action_valueOf() {
        MetadataLockValue actual = MetadataLockValue.valueOf(MetadataLockValue.MetadataLockActionValue.OVERRIDE.toString());
        assertEquals(MetadataLockValue.MetadataLockActionValue.OVERRIDE, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_valueOf() {
        MetadataLockValue.valueOf("invalid value");
    }
}