package com;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * A cryptographically secure Merkle Tree implementation with directional proof
 * generation.
 */
public class MerkleTree {

    private Node root;
    private List<Node> leaves;

    public MerkleTree(List<String> dataBlocks) {
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
            boolean isLeft = (parent.left != current); // sibling is on left if current is right
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
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
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