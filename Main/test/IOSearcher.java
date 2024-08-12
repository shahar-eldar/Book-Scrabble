package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IOSearcher {

    // Main search method
    public static boolean search(String word, String... fileNames) {
        if (word == null || word.trim().isEmpty() || fileNames == null || fileNames.length == 0) {
            return false; // Early exit for empty or null inputs
        }

        Set<String> foundInFiles = new HashSet<>();
        for (String fileName : fileNames) {
            if (searchWordInFile(word.trim(), fileName)) {
                foundInFiles.add(fileName);
            }
        }

        return !foundInFiles.isEmpty();
    }

    // Search for a word in a single file
    private static boolean searchWordInFile(String word, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(word)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return false;
    }

    // Search with case sensitivity option
    public static boolean search(String word, boolean caseSensitive, String... fileNames) {
        if (!caseSensitive) {
            word = word.toLowerCase();
        }

        Set<String> foundInFiles = new HashSet<>();
        for (String fileName : fileNames) {
            if (searchWordInFile(word, fileName, caseSensitive)) {
                foundInFiles.add(fileName);
            }
        }

        return !foundInFiles.isEmpty();
    }

    private static boolean searchWordInFile(String word, String fileName, boolean caseSensitive) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!caseSensitive) {
                    line = line.toLowerCase();
                }
                if (line.contains(word)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return false;
    }
}
