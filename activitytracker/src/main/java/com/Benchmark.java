package com;

import java.util.*;

public class Benchmark {

    private static final int MAX_KEYS = 10_000_000;
    private static final int[] SAMPLE_SIZES = { 100, 500, 1_000, 5_000, 10_000, 25_000, 50_000, 75_000, 100_000,
            250_000, 500_000, 1_000_000 };

    private static List<String> generateKeys(int n) {
        List<String> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            keys.add(UUID.randomUUID().toString());
        }
        return keys;
    }

    private static void benchmark(String name, SimpleMap<String, Integer> map, List<String> baseKeys,
            List<String> sampleKeys) {
        // Prepopulate map with baseKeys
        for (String k : baseKeys) {
            map.put(k, 1);
        }

        // Insertion (on top of prepopulated map)
        long start = System.nanoTime();
        for (String k : sampleKeys) {
            map.put(k, 2);
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
                name, sampleKeys.size(),
                insertTime / 1e6,
                getTime / 1e6,
                removeTime / 1e6);
    }

    public static void main(String[] args) {
        List<String> allKeys = generateKeys(MAX_KEYS);

        System.out.println("MapType,SampleSize,InsertTime(ms),GetTime(ms),RemoveTime(ms)");

        for (int sampleSize : SAMPLE_SIZES) {
            int baseSize = MAX_KEYS - sampleSize;
            List<String> baseKeys = allKeys.subList(0, baseSize);
            List<String> sampleKeys = allKeys.subList(baseSize, MAX_KEYS);

            SimpleMap<String, Integer> linearProbingMap = new LinearProbingHashTable<>(MAX_KEYS);
            benchmark("LinearProbing", linearProbingMap, baseKeys, sampleKeys);
        }

        for (int sampleSize : SAMPLE_SIZES) {
            int baseSize = MAX_KEYS - sampleSize;
            List<String> baseKeys = allKeys.subList(0, baseSize);
            List<String> sampleKeys = allKeys.subList(baseSize, MAX_KEYS);

            SimpleMap<String, Integer> separateChainingMap = new SeparateChainingHashTable<>(MAX_KEYS);
            benchmark("SeparateChaining", separateChainingMap, baseKeys, sampleKeys);
        }
    }
}