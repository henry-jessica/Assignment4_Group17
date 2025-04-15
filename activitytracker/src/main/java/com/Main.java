package com;

public class Main {
    public static void main(String[] args) {
        SimpleMap<String, String> map = new LinearProbingHashTable<>(16);

        map.put("apple", "red fruit");
        map.put("banana", "yellow fruit");
        map.put("cherry", "small red fruit");
        map.put("date", "sweet brown fruit");
        map.put("eggplant", "purple vegetable");

        System.out.println("Get 'banana': " + map.get("banana"));
        System.out.println("Get 'cherry': " + map.get("cherry"));

        map.put("banana", "ripe yellow fruit");
        System.out.println("Updated 'banana': " + map.get("banana"));

        System.out.println("Remove 'date': " + map.remove("date"));

        System.out.println("Get 'date' after removal: " + map.get("date"));

        System.out.println("Size: " + map.size());

        System.out.println("Keys: " + map.keys());
        System.out.println("Values: " + map.values());
    }
}
