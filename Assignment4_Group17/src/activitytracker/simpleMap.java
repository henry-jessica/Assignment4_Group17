package activitytracker;

import java.util.Collection;

/**
 * A simplified Map interface defining core operations for a key-value data
 * structure, as described in the lecture notes.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface simpleMap<K, V> {
    /**
     * Represents a key-value pair (entry) in the map.
     *
     * @param <K> the type of key
     * @param <V> the type of value
     */
    interface Entry<K, V> {
        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key
         */
        K key();

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value
         */
        V value();
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of entries
     */
    int size();

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map is empty
     */
    boolean isEmpty();

    /**
     * Returns the value associated with the specified key, or {@code null} if not
     * found.
     *
     * @param k the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null}
     */
    V get(K k);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is
     * replaced.
     *
     * @param k the key with which the specified value is to be associated
     * @param v the value to be associated with the specified key
     * @return the previous value associated with key, or {@code null} if there was
     *         no mapping
     */
    V put(K k, V v);

    /**
     * Removes the mapping for a key from this map if present.
     *
     * @param k the key whose mapping is to be removed
     * @return the previous value associated with key, or {@code null} if not
     *         present
     */
    V remove(K k);

    /**
     * Returns a {@link Collection} view of the keys contained in this map.
     *
     * @return a collection of keys
     */
    Collection<K> keys();

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     *
     * @return a collection of values
     */
    Collection<V> values();

    /**
     * Returns a {@link Collection} view of the key-value entries contained in this
     * map.
     *
     * @return a collection of entries
     */
    Collection<Entry<K, V>> entries();
}