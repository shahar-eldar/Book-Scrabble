package test;

import java.util.BitSet;

public class BitSetToString {
    private final BitSet bitSet;

    public BitSetToString(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    public String toString(int bitSetSize) {
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

