package activitytracker;

import java.util.ArrayList;
import java.util.List;

/**
 * A merkle tree implementation with hash chaining for ensuring the integrity of
 * datasets like activity logs
 */
public class merkleTree {

    private Node root;
    private List<Node> leaves;
    /**
     * Constructs a Merkle Tree from a list of data blocks.
     */

    public merkleTree(List<String> dataBlocks) {
        // Check for invalid input: null or empty data list.
        if (dataBlocks == null || dataBlocks.isEmpty()) {
            throw new IllegalArgumentException("Input data cannot be empty");
        }
        this.leaves = new ArrayList<>();
        for (String data : dataBlocks) {
            Node leaf = new Node(hash(data));
            leaves.add(leaf);
        }
        this.root = buildTree(leaves); // Initialize the list to store leaf nodes.

    }

    /**
     * Returns the root hash of the Merkle Tree.
     */
    public String getRootHash() {
        return root.hash;
    }
     /**
     * Generates a Merkle Proof for a given data element.
     */

    public List<ProofNode> getProof(String data) {
        String targetHash = hash(data); // Hash the target data to find its corresponding leaf.
        Node current = null;
        for (Node leaf : leaves) {
            if (leaf.hash.equals(targetHash)) {
                current = leaf;
                break;
            }
        }
        if (current == null)
            throw new IllegalArgumentException("Data not found in tree");

        List<ProofNode> proof = new ArrayList<>();// List to store the Merkle Proof.
        while (current.parent != null) {
            Node parent = current.parent;
            boolean isLeft = (parent.left != current);
            Node sibling = isLeft ? parent.left : parent.right;
            proof.add(new ProofNode(sibling.hash, isLeft));
            current = parent;
        }
        return proof; // Return the generated Merkle Proof.
    }

    /**
     * Verifies a Merkle Proof against a given data element and root hash.
     */
    public static boolean verifyProof(String data, List<ProofNode> proof, String rootHash) {
        String computed = hash(data); // Hash the data.
        for (ProofNode node : proof) {
            if (node.isLeft) {
                computed = hash(node.hash + computed);
            } else {
                computed = hash(computed + node.hash);
            }
        }
        return computed.equals(rootHash);
    }

    /**
     * Builds the Merkle Tree from a list of nodes (leaves or intermediate nodes).
     */
    private Node buildTree(List<Node> nodes) {
         // Continue building the tree until only one node (the root) remains.
        while (nodes.size() > 1) {
            List<Node> parents = new ArrayList<>();// List to store the parent nodes for the next level.
            for (int i = 0; i < nodes.size(); i += 2) {
                Node left = nodes.get(i);
                Node right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : left;
                String parentHash = hash(left.hash + right.hash);
                Node parent = new Node(parentHash, left, right);
                left.parent = parent;
                right.parent = parent;
                parents.add(parent);
            }
            nodes = parents;
        }
        return nodes.get(0);// The last remaining node is the root.
    }

    /**
     * Hashes the input string using a simple polynomial rolling hash function.
     */

    private static String hash(String input) {
        long hash = 0;
        long p = 31;// A prime number used as a multiplier.
        long m = 1_000_000_009; // A large prime number used as the modulus

        for (int i = 0; i < input.length(); i++) {
            hash = (hash * p + input.charAt(i)) % m;
        }

        return Long.toString(hash);// Convert the hash to a String.
    }

    /** Internal tree node */
    private static class Node {
        String hash; // The hash value of the sibling node
        Node left, right, parent;

        Node(String hash) {
            this.hash = hash;
        }

        Node(String hash, Node left, Node right) {
            this.hash = hash;
            this.left = left;
            this.right = right;
        }
    }

    /** Represents a step in the Merkle proof */
    public static class ProofNode {
        public final String hash;
        public final boolean isLeft;

        public ProofNode(String hash, boolean isLeft) {
            this.hash = hash;
            this.isLeft = isLeft;
        }
    }
}
