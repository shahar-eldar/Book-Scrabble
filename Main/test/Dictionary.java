package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private CacheManager notExist, exist;
    private BloomFilter bloomFilter;
    private String[] fileNames;
    private final int BLOOM_FILTER_SIZE = 256;
    
    public Dictionary(String... fileNames) {
        this.exist = new CacheManager(400, new LRU());
        this.notExist = new CacheManager(100, new LFU());
        this.bloomFilter = new BloomFilter(BLOOM_FILTER_SIZE, fileNames);
        this.fileNames = fileNames;
        
        for (String fileName : fileNames) {
            List<String> words = readWordsFromFile(fileName);
            for (String word : words) {
                this.bloomFilter.add(word);
            }
        }
    }

    public static List<String> readWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+"); // split by spaces
                for (String word : lineWords) {
                    if (!word.trim().isEmpty()) {
                        words.add(word.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return words;
    }

    public boolean query(String word) {
        if (this.exist.query(word)) {
            return true;
        }

        if (this.notExist.query(word)) {
            return false;
        }

        boolean bloomFilter = this.bloomFilter.contains(word);
        if (!bloomFilter) {
            notExist.add(word);
        }

        return bloomFilter;
    }

    public boolean challenge(String word) {
        boolean result = IOSearcher.search(word, this.fileNames);
        if (result) {
            this.exist.add(word);
        } else {
            this.notExist.add(word);
        }
        return result;
    }
}
