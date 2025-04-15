package com;

import java.util.*;

public class Benchmark {

    private static final int NUM_KEYS = 10_000_000;
    private static final int[] SAMPLE_SIZES = { 100, 500, 1_000, 5_000, 10_000, 25_000, 50_000, 75_000, 100_000 };

    private static List<String> generateKeys(int n) {
        List<String> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            keys.add(UUID.randomUUID().toString());
        }
        return keys;
    }

    private static void benchmark(String name, SimpleMap<String, Integer> map, List<String> keys, int sampleSize) {
        List<String> sampleKeys = keys.subList(0, sampleSize);

        // Insertion
        long start = System.nanoTime();
        for (String k : keys) {
            map.put(k, 1);
        }
        long insertTime = System.nanoTime() - start;

        // Lookup
        start = System.nanoTime();
        for (String k : sampleKeys) {
            map.get(k);
        }
        long getTime = System.nanoTime() - start;

        // Deletion
        start = System.nanoTime();
        for (String k : sampleKeys) {
            map.remove(k);
        }
        long removeTime = System.nanoTime() - start;

        System.out.printf(Locale.US, "%s,%d,%.3f,%.3f,%.3f\n",
                name, sampleSize,
                insertTime / 1e6,
                getTime / 1e6,
                removeTime / 1e6);
    }

    public static void main(String[] args) {
        List<String> keys = generateKeys(NUM_KEYS);

        System.out.println("MapType,SampleSize,InsertTime(ms),GetTime(ms),RemoveTime(ms)");

        for (int sampleSize : SAMPLE_SIZES) {
            SimpleMap<String, Integer> linearProbingMap = new LinearProbingHashTable<>(NUM_KEYS);
            benchmark("LinearProbing", linearProbingMap, keys, sampleSize);
        }

        for (int sampleSize : SAMPLE_SIZES) {
            SimpleMap<String, Integer> separateChainingMap = new SeparateChainingHashTable<>(NUM_KEYS);
            benchmark("SeparateChaining", separateChainingMap, keys, sampleSize);
        }
    }
}
