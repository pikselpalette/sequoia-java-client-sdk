package com.piksel.sequoia.clientsdk.resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.piksel.sequoia.clientsdk.resource.ResourceDeserializer.DeserializationException;

class LinkedDirectRelationshipDeserializer<T extends Resource> extends LinkedDeserializer<T> {

    private LinkedDirectRelationshipDeserializer(PageableResourceEndpoint<T> endpoint, Gson gson, JsonElement payload) {
        super(endpoint, gson, payload);
    }

    protected static <T extends Resource> LinkedDirectRelationshipDeserializer<T> linkedDirectDeserializer(PageableResourceEndpoint<T> endpoint,
            Gson gson, JsonElement payload) {
        return new LinkedDirectRelationshipDeserializer<>(endpoint, gson, payload);
    }

    protected void includeLinkedFieldDirectRelationship(T resource, Field field, JsonElement linkedResources) {
        if (isCollection(field)) {
            List<String> refs = getDirectRefs(resource, field);
            populateCollectionFieldWithDirectRefs(resource, field, linkedResources, refs);
        } else {
            String ref = getRef(resource, field);
            populateSingleFieldWithDirectRef(resource, field, linkedResources, ref);
        }
    }

    private List<String> getDirectRefs(T resource, Field field) {
        try {
            Optional<Field> refField = getRefField(field, resource);
            if (refField.isPresent()) {
                @SuppressWarnings("unchecked")
                List<String> refs = (List<String>) getField(refField.get(), resource);
                return refs;
            } else {
                return Collections.emptyList();
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new DeserializationException("Error deserializing " + resource, ex);
        }
    }

    private void populateSingleFieldWithDirectRef(T resource, Field field, JsonElement linkedResources, String ref) {
        if (isOptional(field)) {
            buildOptionalDirectRelationship(resource, field, linkedResources, ref);
        } else {
            buildNonOptionalDirectRelationship(resource, field, linkedResources, ref);
        }
    }

    private void buildOptionalDirectRelationship(T resource, Field field, JsonElement linkedResources, String ref) {
        try {
            if (ref != null) {
                Collection<T> directLinkedResources = getLinkedResourcesByRef(linkedResources, field, ref);
                @SuppressWarnings("unchecked")
                T linkedResource = (T) getRelatedResource(field, directLinkedResources);
                if (linkedResource != null) {
                    setField(field, resource, Optional.of(linkedResource));
                } else {
                    setField(field, resource, Optional.empty());
                }
            } else {
                setField(field, resource, Optional.empty());
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new DeserializationException("Error deserializing " + resource, ex);
        }
    }

    private void populateCollectionFieldWithDirectRefs(T resource, Field field, JsonElement linkedResources, List<String> refs) {
        if (isCollectionOfOptionals(field)) {
            buildOptionalsDirectRelationships(resource, field, linkedResources, refs);
        } else {
            buildNonOptionalsDirectRelationships(resource, field, linkedResources, refs);
        }
    }

    private void buildOptionalsDirectRelationships(T resource, Field field, JsonElement linkedResources, List<String> refs) {
        try {
            List<Optional<T>> listOptionalLinked = new ArrayList<>();
            if (refs != null) {
                for (String ref : refs) {
                    Collection<T> directLinkedResources = getLinkedResourcesByRef(linkedResources, field, ref);
                    @SuppressWarnings("unchecked")
                    Collection<T> linkedResource = (Collection<T>) getRelatedResource(field, directLinkedResources);
                    if (linkedResource != null && linkedResource.size() == 1) {
                        listOptionalLinked.add(Optional.of(linkedResource.iterator().next()));
                    } else {
                        listOptionalLinked.add(Optional.empty());
                    }
                }
            }
            setField(field, resource, listOptionalLinked);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new DeserializationException("Error deserializing " + resource, ex);
        }
    }

    private void buildNonOptionalsDirectRelationships(T resource, Field field, JsonElement linkedResources, List<String> refs) {
        try {
            setField(field, resource, getLinkedResources(field, linkedResources, refs));
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new DeserializationException("Error deserializing " + resource, ex);
        }
    }

    Collection<T> getLinkedResources(Field field, JsonElement linkedResources, List<String> refs) {
        Collection<T> collectionLinked = new ArrayList<>();
        if (refs != null) {
            for (String ref : refs) {
                Collection<T> directLinkedResource = getLinkedResourcesByRef(linkedResources, field, ref);
                @SuppressWarnings("unchecked")
                Collection<T> linkedResource = (Collection<T>) getRelatedResource(field, directLinkedResource);
                if (linkedResource != null && linkedResource.size() == 1) {
                    collectionLinked.add(linkedResource.iterator().next());
                }
            }
        }
        return collectionLinked;
    }

    private void buildNonOptionalDirectRelationship(T resource, Field field, JsonElement linkedResources, String ref) {
        try {
            if (ref != null) {
                Collection<T> directLinkedResource = getLinkedResourcesByRef(linkedResources, field, ref);
                @SuppressWarnings("unchecked")
                T linkedResource = (T) getRelatedResource(field, directLinkedResource);
                if (linkedResource != null) {
                    setField(field, resource, linkedResource);
                } else {
                    setField(field, resource, null);
                }
            } else {
                setField(field, resource, null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new DeserializationException("Error deserializing " + resource, ex);
        }

    }

    @SuppressWarnings("unchecked")
    private Collection<T> getLinkedResourcesByRef(JsonElement linkedResources, Field field, String ref) {
        JsonElement jsonElement = linkedResources.getAsJsonObject().get(field.getAnnotation(DirectRelationship.class).relationship());
        Collection<T> resourcesLinked = new ArrayList<>();
        if (Objects.nonNull(jsonElement)) {
            JsonArray arrayResources = jsonElement.getAsJsonArray();
            for (int i = 0; i < arrayResources.size(); i++) {
                Object value = arrayResources.get(i).getAsJsonObject().get("ref").getAsString();
                if (Objects.nonNull(value) && ref.equals(value.toString())) {
                    resourcesLinked.add((T) gson.fromJson(arrayResources.get(i), getType(field)));
                    return resourcesLinked;
                }
            }

        }
        return resourcesLinked;
    }

}
