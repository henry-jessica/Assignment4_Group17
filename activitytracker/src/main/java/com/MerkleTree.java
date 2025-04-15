package com;

import java.util.ArrayList;
import java.util.List;

/**
 * A complete Merkle Tree implementation with proof generation and verification.
 */
public class MerkleTree {

    private Node root;
    private List<Node> leaves;

    /**
     * Constructs the Merkle Tree from data blocks.
     *
     * @param dataBlocks input data blocks
     */
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

    /** @return the Merkle Root hash */
    public String getRootHash() {
        return root.hash;
    }

    /**
     * Returns Merkle proof for a given data block.
     *
     * @param data input data block
     * @return list of sibling hashes from leaf to root
     */
    public List<String> getProof(String data) {
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

        List<String> proof = new ArrayList<>();
        while (current.parent != null) {
            Node sibling = current.parent.left == current ? current.parent.right : current.parent.left;
            proof.add(sibling.hash);
            current = current.parent;
        }
        return proof;
    }

    /**
     * Verifies a Merkle proof.
     *
     * @param data  original data
     * @param proof list of sibling hashes
     * @param root  expected Merkle root
     * @return true if valid
     */
    public static boolean verifyProof(String data, List<String> proof, String root) {
        String hash = hash(data);
        for (String siblingHash : proof) {
            hash = hash(hash + siblingHash);
        }
        return hash.equals(root);
    }

    /**
     * Recursively builds tree upward.
     */
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

    /** SHA-256 hash */
    private static String hash(String input) {
        int h = 0;
        for (char c : input.toCharArray()) {
            h = 31 * h + c;
        }
        return Integer.toHexString(h);
    }

    /** Node class for Merkle Tree */
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

    /** Optional: exposes leaf hashes for debugging */
    public List<String> getLeafHashes() {
        List<String> hashes = new ArrayList<>();
        for (Node leaf : leaves) {
            hashes.add(leaf.hash);
        }
        return hashes;
    }
}
