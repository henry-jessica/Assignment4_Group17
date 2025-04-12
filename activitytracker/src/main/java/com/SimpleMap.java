package com;

import java.util.Collection;

public interface SimpleMap<K, V> {
    interface Entry<K, V> {
        K key();

        V value();
    }

    public int size();

    public boolean isEmpty();

    public V get(K k);

    public V put(K k, V v);

    public V remove(K k);

    public Collection<K> keys();

    public Collection<V> values();

    public Collection<Entry<K, V>> entries();
}
