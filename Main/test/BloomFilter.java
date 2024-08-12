package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private BitSet bitSet;
    private int bitSetSize;
    private MessageDigest[] hashFunctions;

    public BloomFilter(int bitSetSize, String... hashAlgorithms) {
        this.bitSetSize = bitSetSize;
        this.bitSet = new BitSet(bitSetSize + 1);
        this.hashFunctions = new MessageDigest[hashAlgorithms.length];

        try {
            for (int i = 0; i < hashAlgorithms.length; i++) {
                this.hashFunctions[i] = MessageDigest.getInstance(hashAlgorithms[i]);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Invalid hash algorithm specified", e);
        }
    }

    public int getBit(String value, MessageDigest hashFunction) {
        byte[] bytesResult;
        BigInteger bigResult;
        int intResult;
        bytesResult = hashFunction.digest(value.getBytes());
        bigResult = new BigInteger(1, bytesResult);
        intResult = Math.abs(bigResult.intValue()) % this.bitSetSize;
        return intResult;
    }

    public void add(String value) {
        for (MessageDigest hashFunction : hashFunctions) {
            this.bitSet.set(getBit(value, hashFunction));
        }
    }

    public boolean contains(String value) {
        for (MessageDigest hashFunction : hashFunctions) {
            if (bitSet.get(getBit(value, hashFunction))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new BitSetToString(this.bitSet).toString(bitSetSize);
    }
}