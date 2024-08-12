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

        for (int i = 0; i < hashAlgorithms.length; i++) {
            try {
                    this.hashFunctions[i] = MessageDigest.getInstance(hashAlgorithms[i]);
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Hash name invalid" + e.getMessage());
            }
        }
    }

    private int getBit(String value, MessageDigest hashFunction) {
        byte[] bytesResult;
        BigInteger bigResult;
        int intResult;
        bytesResult = hashFunction.digest(value.getBytes());
        bigResult = new BigInteger(bytesResult);
        intResult = Math.abs(bigResult.intValue()) % this.bitSetSize;
        return intResult;
    }

    public void add(String value) {
        for (MessageDigest hashFunction : hashFunctions) {
            if (hashFunction != null) {
                this.bitSet.set(getBit(value, hashFunction), true);
            }
        }
    }

    public boolean contains(String value) {
        for (MessageDigest hashFunction : hashFunctions) {
            if (hashFunction != null && !bitSet.get(getBit(value, hashFunction))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bitSetSize);
        
        for (int i = 0; i <= bitSet.previousSetBit(bitSet.length()); i++) {
            sb.append('0');
        }

        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            if (i > bitSet.previousSetBit(bitSet.length())) {break;}
            sb.setCharAt(i, '1');
        }

        return sb.toString();
    }
}