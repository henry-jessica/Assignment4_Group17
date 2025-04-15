package com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A hash table implementation using separate chaining for collision resolution.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class SeparateChainingHashTable<K, V> implements SimpleMap<K, V> {

    /**
     * Represents a key-value pair stored in the hash table.
     */
    private static class Entry<K, V> implements SimpleMap.Entry<K, V> {
        final K key;
        V value;

        /**
         * Constructs an Entry with the given key and value.
         *
         * @param key   the key
         * @param value the value
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /** @return the key associated with this entry */
        @Override
        public K key() {
            return key;
        }

        /** @return the value associated with this entry */
        @Override
        public V value() {
            return value;
        }
    }

    private static final double LOAD_FACTOR = 0.75;
    private List<Entry<K, V>>[] table;
    private int size;

    /**
     * Constructs a hash table with the specified initial capacity.
     *
     * @param capacity the initial number of buckets
     */
    @SuppressWarnings("unchecked")
    public SeparateChainingHashTable(int capacity) {
        table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    /**
     * Computes the index in the table for the specified key and capacity.
     *
     * @param key      the key
     * @param capacity the number of buckets
     * @return the hash index
     */
    private int hash(K key, int capacity) {
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    /**
     * Computes the index in the table for the specified key.
     *
     * @param key the key
     * @return the hash index
     */
    private int hash(K key) {
        return hash(key, table.length);
    }

    /**
     * Resizes the table to double the current capacity and rehashes all entries.
     */
    private void resize() {
        List<Entry<K, V>>[] oldTable = table;
        int newCapacity = oldTable.length * 2;

        @SuppressWarnings("unchecked")
        List<Entry<K, V>>[] newTable = new List[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList<>();
        }

        table = newTable;
        size = 0;

        for (List<Entry<K, V>> bucket : oldTable) {
            for (Entry<K, V> entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }

    /**
     * Inserts or updates a key-value pair.
     *
     * @param key   the key
     * @param value the value
     * @return the old value if the key was already present, or null otherwise
     */
    @Override
    public V put(K key, V value) {
        if ((double) (size + 1) / table.length > LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
        return null;
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key the key
     * @return the value, or null if not found
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Removes the entry associated with a given key.
     *
     * @param key the key
     * @return the value removed, or null if the key was not present
     */
    @Override
    public V remove(K key) {
        int index = hash(key);
        Iterator<Entry<K, V>> iterator = table[index].iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.key.equals(key)) {
                V value = entry.value;
                iterator.remove();
                size--;
                return value;
            }
        }
        return null;
    }

    /** @return the number of key-value pairs in the hash table */
    @Override
    public int size() {
        return size;
    }

    /** @return true if the hash table is empty */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** @return a collection of all keys */
    @Override
    public Collection<K> keys() {
        List<K> result = new ArrayList<>();
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                result.add(entry.key);
            }
        }
        return result;
    }

    /** @return a collection of all values */
    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                result.add(entry.value);
            }
        }
        return result;
    }

    /** @return a collection of all key-value entries */
    @Override
    public Collection<SimpleMap.Entry<K, V>> entries() {
        List<SimpleMap.Entry<K, V>> result = new ArrayList<>();
        for (List<Entry<K, V>> bucket : table) {
            result.addAll(bucket);
        }
        return result;
    }
}