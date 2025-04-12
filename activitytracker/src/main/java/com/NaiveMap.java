package com;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NaiveMap<K, V> extends AbstractMap<K, V> {
    private final List<Map.Entry<K, V>> entries = new ArrayList<>();

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new HashSet<>(entries);
    }

    @Override
    public V put(K key, V value) {
        for (Map.Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                V old = entry.getValue();
                entry.setValue(value);
                return old;
            }
        }
        entries.add(new SimpleEntry<>(key, value));
        return null;
    }
}