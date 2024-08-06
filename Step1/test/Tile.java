package test;

import java.util.Arrays;
import java.util.Random;

public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public char getLetter() {
        return this.letter;
    }

    public int getTileScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return "  " + this.letter + ",  ";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + letter;
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (letter != other.letter)
            return false;
        if (score != other.score)
            return false;
        return true;
    }

    public static class Bag {
        private static Bag singleInstance = null;
        
        private final int[] lettersCount;
        private final Tile[] tiles;
        
        private static final int[] DEFAULT_LETTER_COUNT = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private static final int[] LETTER_SCORES = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        
        private Bag() {
            this(DEFAULT_LETTER_COUNT, LETTER_SCORES);
        }

        private Bag(int[] lettersCount, int[] scores) {
            this.lettersCount = Arrays.copyOf(lettersCount, lettersCount.length);
            this.tiles = createTiles();
        }

        private Tile[] createTiles() {
            Tile[] tiles = new Tile[LETTER_SCORES.length];
            for (int i = 0; i < LETTER_SCORES.length; i++) {
                tiles[i] = new Tile((char) ('A' + i), LETTER_SCORES[i]);
            }
            return tiles;
        }

        public static Bag getBag() {
            if (singleInstance == null) {
                singleInstance = new Bag();
            }
            return singleInstance;
        }

        public int[] getQuantities() {
            return Arrays.copyOf(lettersCount, lettersCount.length);
        }

        public Tile getRand() {
            if (Arrays.equals(this.lettersCount, new int[this.lettersCount.length])) {
                return null;
            }

            Random random = new Random();
            int randomNumber; 
            
            do {
                randomNumber = random.nextInt(26);
            } while (this.lettersCount[randomNumber] == 0);
            
            this.lettersCount[randomNumber]--;
            return this.tiles[randomNumber];
        }

        public Tile getTile(char letter) {
            int index = letter - 'A';
            if (index < 0 || index > 25 || this.lettersCount[index] <= 0) {
                return null;
            }

            this.lettersCount[index]--;
            return this.tiles[index];
        }

        public void put(Tile tile) {
            int index = tile.letter - 'A';
            if (this.lettersCount[index] < DEFAULT_LETTER_COUNT[index]) {
                this.lettersCount[index]++;
            }
        }

        public int size() {
            return Arrays.stream(this.lettersCount).sum();
        }
    }
}
