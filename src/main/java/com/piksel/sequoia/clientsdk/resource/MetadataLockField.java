package com.piksel.sequoia.clientsdk.resource;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class MetadataLockField {

    /**
     * Entry example: <"path.to.field", LOCK>
     */
    private Map<String, MetadataLockValue> fieldPathToStatusOrAction = new HashMap<>();


    public void addLockValue(String pathToField, MetadataLockValue lockValue) {
        fieldPathToStatusOrAction.put(pathToField, lockValue);
    }

    public MetadataLockValue getLockValueForField(String pathToField) {
        return fieldPathToStatusOrAction.get(pathToField);
    }

    public Map<String, MetadataLockValue> getLockValues() {
        return fieldPathToStatusOrAction;
    }

}
