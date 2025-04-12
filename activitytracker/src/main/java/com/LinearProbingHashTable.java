package com;

import java.util.Arrays;
import java.util.Collection;

public class LinearProbingHashTable<K, V> implements SimpleMap<K, V> {

    @Override
    public Collection<K> keys() {
        return Arrays.stream(table)
                .filter(e -> e != null && !e.isDeleted)
                .map(e -> e.key)
                .toList();
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(table)
                .filter(e -> e != null && !e.isDeleted)
                .map(entry -> entry.value)
                .toList();

    }

    @Override
    public Collection<SimpleMap.Entry<K, V>> entries() {
        return Arrays.asList(table);
    }

    private static class Entry<K, V> implements SimpleMap.Entry<K, V> {
        K key;
        V value;
        boolean isDeleted;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }
    }

    private Entry<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public LinearProbingHashTable(int capacity) {
        table = new Entry[capacity];
        size = 0;
    }

    public LinearProbingHashTable() {
        this(16);
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        int startIndex = index;

        do {
            Entry<K, V> entry = table[index];
            if (entry == null || entry.isDeleted || entry.key.equals(key)) {
                table[index] = new Entry<>(key, value);
                size++;
                return null;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);

        throw new IllegalStateException("HashTable is full");
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        int startIndex = index;

        do {
            Entry<K, V> entry = table[index];
            if (entry == null)
                return null;
            if (!entry.isDeleted && entry.key.equals(key))
                return entry.value;
            index = (index + 1) % table.length;
        } while (index != startIndex);

        return null;
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        int startIndex = index;

        do {
            Entry<K, V> entry = table[index];
            if (entry == null)
                return null;
            if (!entry.isDeleted && entry.key.equals(key)) {
                entry.isDeleted = true;
                size--;
                return entry.value;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);

        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public String valuesConcatenated() {
        StringBuilder sb = new StringBuilder();
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.isDeleted) {
                sb.append(entry.value);
            }
        }
        return sb.toString();
    }
}
