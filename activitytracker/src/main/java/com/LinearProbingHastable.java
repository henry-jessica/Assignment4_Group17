package com;

public class LinearProbingHastable<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        boolean isDeleted;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }

    private Entry<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public LinearProbingHastable(int capacity) {
        table = new Entry[capacity];
        size = 0;
    }

    public LinearProbingHastable() {
        this(16);
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    public void put(K key, V value) {
        int index = hash(key);
        int startIndex = index;

        do {
            Entry<K, V> entry = table[index];
            if (entry == null || entry.isDeleted || entry.key.equals(key)) {
                table[index] = new Entry<>(key, value);
                size++;
                return;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);

        throw new IllegalStateException("HashTable is full");
    }

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

    public int size() {
        return size;
    }

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
