package activitytracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A hash table implementation using linear probing for collision resolution.
 * This class implements the SimpleMap interface.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of values
 */
public class linearProbingHashTable<K, V> implements simpleMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;

    /**
     * Returns a collection view of the keys in the hash table.
     *
     * @return a collection of keys
     */
    @Override
    public Collection<K> keys() {
        return Arrays.stream(table)
                .filter(e -> e != null && !e.isDeleted)
                .map(e -> e.key)
                .toList();
    }

    /**
     * Returns a collection view of the values in the hash table.
     *
     * @return a collection of values
     */
    @Override
    public Collection<V> values() {
        return Arrays.stream(table)
                .filter(e -> e != null && !e.isDeleted)
                .map(entry -> entry.value)
                .toList();
    }

    /**
     * Returns a collection view of the entries in the hash table.
     *
     * @return a collection of key-value entries
     */
    @Override
    public Collection<simpleMap.Entry<K, V>> entries() {
        List<simpleMap.Entry<K, V>> result = new ArrayList<>();
        for (Entry<K, V> e : table) {
            if (e != null && !e.isDeleted) {
                result.add(new Entry<>(e.key, e.value));
            }
        }
        return result;
    }

    /**
     * Represents a key-value pair in the hash table.
     */
    private static class Entry<K, V> implements simpleMap.Entry<K, V> {
        K key;
        V value;
        boolean isDeleted;

        /**
         * Constructs a new Entry with the given key and value.
         *
         * @param key   the key
         * @param value the value
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }

        /** @return the key of the entry */
        @Override
        public K key() {
            return key;
        }

        /** @return the value of the entry */
        @Override
        public V value() {
            return value;
        }
    }

    private Entry<K, V>[] table;
    private int size;

    /**
     * Constructs a hash table with a specified initial capacity.
     *
     * @param capacity initial number of slots
     */
    @SuppressWarnings("unchecked")
    public linearProbingHashTable(int capacity) {
        table = new Entry[capacity];
        size = 0;
    }

    /**
     * Hash function using given capacity.
     *
     * @param key      the key
     * @param capacity capacity to mod with
     * @return hashed index
     */
    private int hash(K key, int capacity) {
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    /**
     * Hash function using current table length.
     *
     * @param key the key
     * @return hashed index
     */
    private int hash(K key) {
        return hash(key, table.length);
    }

    /**
     * Inserts or updates a key-value mapping in the table.
     *
     * @param key   the key
     * @param value the value
     * @return the previous value or null if new
     */
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

    /**
     * Doubles the table capacity and reinserts all entries.
     */
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

    /**
     * Reinserts an entry into the table during resizing.
     *
     * @param key   the key
     * @param value the value
     */
    private void reinsert(K key, V value) {
        int index = hash(key, table.length);
        while (table[index] != null) {
            index = (index + 1) % table.length;
        }
        table[index] = new Entry<>(key, value);
        size++;
    }

    /**
     * Retrieves a value by its key.
     *
     * @param key the key
     * @return the value or null if not found
     */
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

    /**
     * Removes a key-value mapping.
     *
     * @param key the key to remove
     * @return the value removed, or null if not found
     */
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

    /**
     * Checks if the table contains the specified key.
     *
     * @param key the key to check
     * @return true if present
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the number of key-value pairs in the table.
     *
     * @return size of map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the map is empty.
     *
     * @return true if no entries
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}