package com;

import java.util.*;

public class Benchmark {

    private static final int NUM_KEYS = 100_000;
    private static final int SAMPLE_SIZE = 10_000;

    private static List<String> generateKeys(int n) {
        List<String> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            keys.add(UUID.randomUUID().toString());
        }
        return keys;
    }

    private static void benchmark(String name, SimpleMap<String, Integer> map, List<String> keys) {
        List<String> sampleKeys = keys.subList(0, SAMPLE_SIZE);

        System.out.println("--- " + name + " ---");

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

        System.out.printf("Insert Time: %.3f ms\n", insertTime / 1e6);
        System.out.printf("Get Time: %.3f ms\n", getTime / 1e6);
        System.out.printf("Remove Time: %.3f ms\n", removeTime / 1e6);
        System.out.println();
    }

    public static void main(String[] args) {
        List<String> keys = generateKeys(NUM_KEYS);

        SimpleMap<String, Integer> linearProbingMap = new LinearProbingHashTable<>(NUM_KEYS);
        SimpleMap<String, Integer> separateChainingMap = new SeparateChainingHashTable<>(NUM_KEYS);

        benchmark("Linear Probing", linearProbingMap, keys);
        benchmark("Separate Chaining", separateChainingMap, keys);
    }
}
