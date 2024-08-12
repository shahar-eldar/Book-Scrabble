package test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy {
    private final Map<String, Integer> frequencies;
    private final Map<String, String> cache;
    private final PriorityQueue<String> leastFrequentlyUsed;

    public LFU() {
        this.frequencies = new HashMap<>();
        this.cache = new HashMap<>();
        this.leastFrequentlyUsed = new PriorityQueue<>(
            (a, b) -> frequencies.get(a).compareTo(frequencies.get(b))
        );
    }

    @Override
    public void add(String word) {
        if (cache.containsKey(word)) {
            frequencies.put(word, frequencies.get(word) + 1);
        } else {
            cache.put(word, word);
            frequencies.put(word, 1);
        }
        leastFrequentlyUsed.remove(word);
        leastFrequentlyUsed.offer(word);
    }

    @Override
    public String remove() {
        if (cache.isEmpty()) {
            return null; // או התנהלות אחרת במצב של מטמון ריק
        }
        String leastUsed = leastFrequentlyUsed.poll();
        cache.remove(leastUsed);
        frequencies.remove(leastUsed);
        return leastUsed;
    }

    public int size() {
        return cache.size();
    }
}
