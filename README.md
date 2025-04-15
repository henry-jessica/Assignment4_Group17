# Tamper-Proof Logging System Using Hash Chains, Merkle Trees and HMACs

**Assignment 4 | COMP47500 – Advanced Data Structures**  
**MSc Computer Science (Negotiated Learning), University College Dublin**  
**Group 17: Jessica Henry, John Hughes, Ritika Verma**

## Overview

This project presents a tamper-proof logging system for online examinations, built using custom hash-based data structures and cryptographic techniques. The system ensures log integrity and authenticity using:

- **Hash Chains**: Guarantee sequential integrity by chaining log entries with cryptographic hashes.
- **HMACs (Hash-based Message Authentication Codes)**: Provide verification of authenticity using secret keys.
- **Merkle Trees**: Allow efficient verification of entire exam sessions through hierarchical hashing.
- **Linear Probing Hash Table**: Supports efficient log entry storage with fast insertion and retrieval.

## Features

- Tamper-evident logs with chained cryptographic hashes  
- Authentication of log origins using HMACs  
- Batch log verification via Merkle Tree root comparisons  
- Dynamic and efficient log storage with a custom-built hash table  

## Components

- `Benchmark.java`: Benchmarks performance of hash table implementations (linear probing vs. separate chaining)  
- `LinearProbingHashTable.java`: Custom hash table using open addressing and linear probing  
- `SeparateChainingHashTable.java`: Hash table using linked lists to handle collisions  
- `MerkleTree.java`: Constructs a Merkle Tree from log entries and supports proof generation and verification  
- `SimpleMap.java`: A custom interface that defines basic key-value operations  

## Build and Run

### Prerequisites

- Java Development Kit (JDK) 17 or above  
- Git (for cloning this repository)  

### Compile and Run

```bash
javac -d bin src/com/*.java
java -cp bin com.Benchmark
