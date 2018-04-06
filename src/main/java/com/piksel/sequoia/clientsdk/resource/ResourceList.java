package com.piksel.sequoia.clientsdk.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.piksel.sequoia.annotations.PublicEvolving;

import lombok.Getter;
import lombok.ToString;

@PublicEvolving
@Getter
@ToString
public class ResourceList {

    private final Collection<Object> collection = new ArrayList<>();

    public int size() {
        return collection.size();
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public boolean contains(Object o) {
        return collection.contains(o);
    }

    public Iterator<Object> iterator() {
        return collection.iterator();
    }

    public Object[] toArray() {
        return collection.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return collection.toArray(a);
    }

    public boolean add(String e) {
        return collection.add(e);
    }

    public Boolean add(Boolean e) {
        return collection.add(e);
    }

    public Boolean add(Number e) {
        return collection.add(e);
    }

    public boolean remove(Object o) {
        return collection.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    public boolean addAll(Collection<?> c) {
        return collection.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return collection.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return collection.retainAll(c);
    }

    public void clear() {
        collection.clear();
    }

}
