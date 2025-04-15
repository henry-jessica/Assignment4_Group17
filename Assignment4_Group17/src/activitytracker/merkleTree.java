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

    public merkleTree(List<String> dataBlocks) {
        if (dataBlocks == null || dataBlocks.isEmpty()) {
            throw new IllegalArgumentException("Input data cannot be empty");
        }
        this.leaves = new ArrayList<>();
        for (String data : dataBlocks) {
            Node leaf = new Node(hash(data));
            leaves.add(leaf);
        }
        this.root = buildTree(leaves);
    }

    public String getRootHash() {
        return root.hash;
    }

    public List<ProofNode> getProof(String data) {
        String targetHash = hash(data);
        Node current = null;
        for (Node leaf : leaves) {
            if (leaf.hash.equals(targetHash)) {
                current = leaf;
                break;
            }
        }
        if (current == null)
            throw new IllegalArgumentException("Data not found in tree");

        List<ProofNode> proof = new ArrayList<>();
        while (current.parent != null) {
            Node parent = current.parent;
            boolean isLeft = (parent.left != current);
            Node sibling = isLeft ? parent.left : parent.right;
            proof.add(new ProofNode(sibling.hash, isLeft));
            current = parent;
        }
        return proof;
    }

    public static boolean verifyProof(String data, List<ProofNode> proof, String rootHash) {
        String computed = hash(data);
        for (ProofNode node : proof) {
            if (node.isLeft) {
                computed = hash(node.hash + computed);
            } else {
                computed = hash(computed + node.hash);
            }
        }
        return computed.equals(rootHash);
    }

    private Node buildTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            List<Node> parents = new ArrayList<>();
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
        return nodes.get(0);
    }

    private static String hash(String input) {
        long hash = 0;
        long p = 31;
        long m = 1_000_000_009;

        for (int i = 0; i < input.length(); i++) {
            hash = (hash * p + input.charAt(i)) % m;
        }

        return Long.toString(hash);
    }

    /** Internal tree node */
    private static class Node {
        String hash;
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