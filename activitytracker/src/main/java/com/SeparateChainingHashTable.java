package com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SeparateChainingHashTable<K, V> implements SimpleMap<K, V> {

    private static class Entry<K, V> implements SimpleMap.Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
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

    private static final double LOAD_FACTOR = 0.75;
    private List<Entry<K, V>>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public SeparateChainingHashTable(int capacity) {
        table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    public SeparateChainingHashTable() {
        this(16);
    }

    private int hash(K key, int capacity) {
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    private int hash(K key) {
        return hash(key, table.length);
    }

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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

    @Override
    public Collection<SimpleMap.Entry<K, V>> entries() {
        List<SimpleMap.Entry<K, V>> result = new ArrayList<>();
        for (List<Entry<K, V>> bucket : table) {
            result.addAll(bucket);
        }
        return result;
    }
}