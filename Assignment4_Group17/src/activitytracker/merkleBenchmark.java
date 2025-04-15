package activitytracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class merkleBenchmark {

    private static final int[] SAMPLE_SIZES = {
            5_000, 10_000, 25_000, 50_000, 100_000, 250_000, 500_000, 1_000_000, 2_500_000, 5_000_000, 10_000_000
    };

    private static List<String> generateLogs(int count) {
        List<String> data = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            data.add("log_event_student" + (i % 100) + "_timestamp_" + System.nanoTime());
        }
        return data;
    }

    private static String getRandomData(List<String> data) {
        Random rand = new Random();
        return data.get(rand.nextInt(data.size()));
    }

    public static void main(String[] args) {
        System.out.println("SampleSize,BuildTime(ms),ProofGenTime(ms),ProofVerifyTime(ms)");

        for (int size : SAMPLE_SIZES) {
            List<String> logs = generateLogs(size);

            // Build Merkle Tree
            long start = System.nanoTime();
            merkleTree tree = new merkleTree(logs);
            long buildTime = System.nanoTime() - start;

            // Select target log for proof
            String target = getRandomData(logs);

            // Proof generation
            start = System.nanoTime();
            List<merkleTree.ProofNode> proof = tree.getProof(target);
            long proofGenTime = System.nanoTime() - start;

            // Verification
            start = System.nanoTime();
            boolean isValid = merkleTree.verifyProof(target, proof, tree.getRootHash());
            long verifyTime = System.nanoTime() - start;

            if (!isValid) {
                throw new RuntimeException("Merkle Proof verification failed for size: " + size);
            }

            // Print benchmark result
            System.out.printf(Locale.US, "%d,%.3f,%.3f,%.3f\n",
                    size,
                    buildTime / 1e6,
                    proofGenTime / 1e6,
                    verifyTime / 1e6);
        }
    }
}
