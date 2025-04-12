package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LinearProbingHashTable<K, V> implements SimpleMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;

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
        List<SimpleMap.Entry<K, V>> result = new ArrayList<>();
        for (Entry<K, V> e : table) {
            if (e != null && !e.isDeleted) {
                result.add(new Entry<>(e.key, e.value));
            }
        }
        return result;
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

    private int hash(K key, int capacity) {
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    private int hash(K key) {
        return hash(key, table.length);
    }

    @Override
    public V put(K key, V value) {
        if ((double) (size + 1) / table.length > LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);
        int startIndex = index;

        do {
            Entry<K, V> entry = table[index];
            if (entry == null || entry.isDeleted) {
                table[index] = new Entry<>(key, value);
                size++;
                return null;
            }
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);

        throw new IllegalStateException("HashTable is full");
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;

        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTable = new Entry[newCapacity];
        table = newTable;
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                reinsert(entry.key, entry.value);
            }
        }
    }

    private void reinsert(K key, V value) {
        int index = hash(key, table.length);
        while (table[index] != null) {
            index = (index + 1) % table.length;
        }
        table[index] = new Entry<>(key, value);
        size++;
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