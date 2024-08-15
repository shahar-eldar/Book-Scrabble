package test;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    private Map<String, Dictionary> dictionaries = new HashMap<>();

    private DictionaryManager() {}

    public static DictionaryManager get() {
        if (instance == null) {
            synchronized (DictionaryManager.class) {
                if (instance == null) {
                    instance = new DictionaryManager();
                }
            }
        }
        return instance;
    }

    public boolean query(String... bookNames) {
        boolean containes = false;
        String word = bookNames[bookNames.length - 1];
        
        for (int i = 0; i < bookNames.length - 1; i++) {
            String book = bookNames[i];
            
            if (!dictionaries.containsKey(book)) {
                dictionaries.put(book, new Dictionary(book));
            }

            if (dictionaries.get(book).query(word)) {
                containes = true;
            }
        }
        return containes;
    }

    public boolean challenge(String... bookNames) {
        boolean containes = false;
        String word = bookNames[bookNames.length - 1];

        for (int i = 0; i < bookNames.length - 1; i++) {
            String book = bookNames[i];

            if (!dictionaries.containsKey(book)) {
                dictionaries.put(book, new Dictionary(book));
            }
            if (dictionaries.get(book).challenge(word)) {
                containes = true;
            }
        }
        return containes;
    }

    public int getSize() {
        return dictionaries.size();
    }
}