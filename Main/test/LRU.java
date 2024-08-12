package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy {
    private LinkedHashMap<String, String> cache;

    public LRU() {
        this.cache = new LinkedHashMap<>(16, 0.75f, true);
    }

    @Override
    public void add(String word) {
        cache.put(word, word);
    }

    @Override
    public String remove() {
        if (cache.isEmpty()) {
            return null; // או התנהלות אחרת במצב של מטמון ריק
        }
        Map.Entry<String, String> eldestEntry = cache.entrySet().iterator().next();
        String removedKey = eldestEntry.getKey();
        cache.remove(removedKey);
        return removedKey;
    }

    public int size() {
        return cache.size();
    }
}
