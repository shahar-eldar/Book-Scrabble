package test;

import java.util.HashSet;

public class CacheManager {
    private final int size;
    private final CacheReplacementPolicy crp;
    private final HashSet<String> cache;

    public CacheManager(int size, CacheReplacementPolicy crp) {
        if (size <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than 0");
        }
        if (crp == null) {
            throw new IllegalArgumentException("CacheReplacementPolicy cannot be null");
        }
        this.size = size;
        this.crp = crp;
        this.cache = new HashSet<>(size);
    }

    public boolean query(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return cache.contains(word);
    }

    public void add(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        if (cache.size() >= size) {
            String removedWord = crp.remove();
            cache.remove(removedWord);
        }
        cache.add(word);
        crp.add(word);
    }
}
