package com;

import java.util.AbstractMap;
import java.util.Set;

public class CustomHashTable<K, V> extends AbstractMap<K, V> {

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}