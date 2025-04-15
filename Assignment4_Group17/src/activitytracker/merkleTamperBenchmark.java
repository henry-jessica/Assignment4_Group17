package activitytracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A benchmark for testing the Merkle Tree's ability to detect tampering in a
 * collection of logs.
 */
public class merkleTamperBenchmark {

    private static final int LOG_COUNT = 1000;

    public static void main(String[] args) {
        List<String> originalLogs = generateLogs(LOG_COUNT);
        merkleTree tree = new merkleTree(originalLogs);
        String originalRoot = tree.getRootHash();

        int passedTests = 0;
        int totalTests = 0;

        // 1. Replace a log
        List<String> tampered1 = new ArrayList<>(originalLogs);
        tampered1.set(0, "cheating_event_detected");
        totalTests++;
        if (tamperingDetected(tampered1, originalRoot))
            passedTests++;

        // 2. Remove a log
        List<String> tampered2 = new ArrayList<>(originalLogs);
        tampered2.remove(0);
        totalTests++;
        if (tamperingDetected(tampered2, originalRoot))
            passedTests++;

        // 3. Append a log
        List<String> tampered3 = new ArrayList<>(originalLogs);
        tampered3.add("extra_event_hacker_injected");
        totalTests++;
        if (tamperingDetected(tampered3, originalRoot))
            passedTests++;

        // 4. Single-character change
        List<String> tampered4 = new ArrayList<>(originalLogs);
        String changed = tampered4.get(10);
        changed = changed.substring(0, changed.length() - 1) + "X"; // change last char
        tampered4.set(10, changed);
        totalTests++;
        if (tamperingDetected(tampered4, originalRoot))
            passedTests++;

        // 5. Swap two logs
        List<String> tampered5 = new ArrayList<>(originalLogs);
        Collections.swap(tampered5, 2, 3);
        totalTests++;
        if (tamperingDetected(tampered5, originalRoot))
            passedTests++;

        System.out.printf("Tamper Detection Score: %d/%d tampering attempts were detected and blocked\n",
                passedTests, totalTests);
    }

    private static boolean tamperingDetected(List<String> tamperedLogs, String originalRoot) {
        try {
            merkleTree tamperedTree = new merkleTree(tamperedLogs);
            return !tamperedTree.getRootHash().equals(originalRoot);
        } catch (Exception e) {
            return true;
        }
    }

    private static List<String> generateLogs(int count) {
        List<String> logs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            logs.add("student_" + (i % 30) + "_event_" + UUID.randomUUID());
        }
        return logs;
    }
}