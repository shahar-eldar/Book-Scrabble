package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private static Board singleBoard = null;
    private Tile[][] tiles;
    private static final int BOARD_SIZE = 15;
    private static final int STAR = 7;
    private static final char UNDERSCORE = '_';
    private static boolean isFirst = true;
    private static final Map<String, String> bonusTiles = new HashMap<>();
    private static ArrayList<Word> boardWords = new ArrayList<>();

    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    private Board() {
        this.tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
    }

    @Override
    public String toString() {
        String string = "Board [tiles= \n";
        for (Tile[] row : this.tiles) {
            string += "[";
            for (Tile tile : row) {
                string += (tile != null) ? tile.toString() : "null, ";
            }
            string += "]\n";
        }
        return string + "]";
    }

    private static void addPositions(Map<String, String> map, String bonusType, int[][] positions) {
        for (int[] pos : positions) {
            map.put(pos[0] + "," + pos[1], bonusType);
        }
    }    

    static {
        addPositions(bonusTiles, "TW", new int[][]{{0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}});
        addPositions(bonusTiles, "DW", new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {10, 10}, {11, 11}, {12, 12}, {13, 13}, {14, 1}, {13, 1}, {1, 13}, {12, 2}, {12, 2}, {11, 3}, {10, 4}, {4, 10}, {3, 11}});
        addPositions(bonusTiles, "TL", new int[][]{{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1}, {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}});
        addPositions(bonusTiles, "DL", new int[][]{{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14}, {12, 6}, {12, 8}, {14, 3}, {14, 11}});
    }
    
    // Methods to check if a given position is a bonus tile
    private static boolean isTripleWordScore(int row, int col) {
        return "TW".equals(bonusTiles.get(row + "," + col));
    }
    
    private static boolean isDoubleWordScore(int row, int col) {
        return (isFirst && row == STAR && col == STAR) || "DW".equals(bonusTiles.get(row + "," + col));
    }
    
    private static boolean isTripleLetterScore(int row, int col) {
        return "TL".equals(bonusTiles.get(row + "," + col));
    }
    
    private static boolean isDoubleLetterScore(int row, int col) {
        return "DL".equals(bonusTiles.get(row + "," + col));
    }

    public static Board getBoard() {
        if (singleBoard == null) {singleBoard = new Board();}
        return singleBoard;
    }

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            copy[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        }
        return copy;
    }

    public boolean isInside(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int length = word.getTiles().length;
    
        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE) {
            return false;
        }
        
        if (word.isVertical()) {
            return (row + length <= BOARD_SIZE);
        }
        
        return (col + length <= BOARD_SIZE);
    }

    private boolean hasATile(int row, int col) {
        Tile tile = this.tiles[row][col];
        return (tile != null && tile.getLetter() != UNDERSCORE);
    }

    public boolean isBasedOnExistingTiles(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int length = word.getTiles().length;
        if (isFirst) {
            if (word.isVertical()) {
                return (col == STAR && row <= STAR && row + length >= STAR);
            } else {
                return (row == STAR && col <= STAR && col + length >= STAR);
            }
        }
        for (int i = 0; i < length; i++) {  
            if (word.isVertical()) {
                // Check vertical and surrounding positions
                if ((i == 0 && hasATile(row - 1, col)) ||
                (hasATile(row + i, col)) ||
                (row + i < BOARD_SIZE && col - 1 >= 0 && hasATile(row + i, col - 1)) ||
                (row + i < BOARD_SIZE && col + 1 < BOARD_SIZE && hasATile(row + i, col + 1)) ||
                (i == length - 1 && hasATile(row + i + 1, col))) {return true;}
            } else {
                // Check horizontal and surrounding positions
                if ((i == 0 && hasATile(row, col - 1)) ||
                (row + 1 < BOARD_SIZE && col + i < BOARD_SIZE && hasATile(row + 1, col + i)) ||
                (row - 1 >= 0 && col + i < BOARD_SIZE && hasATile(row - 1, col + i)) ||
                (hasATile(row, col + i)) ||
                (i == length - 1 && hasATile(row, col + i + 1))) {return true;}
            }
        }
        return false;
    }

    private boolean isTilesOverlapping(Word word) {
        if (!isFirst) {
            int nullCounter = 0;
            int length = word.getTiles().length;
            for (int i = 0; i < length; i++) {
                int row = word.isVertical() ? word.getRow() + i : word.getRow();
                int col = word.isVertical() ? word.getCol() : word.getCol() + i;
                
                if (hasATile(row, col) && word.getTiles()[i] != null) {return false;}
                if (word.getTiles()[i] == null) {
                    if (!hasATile(row, col)) {return false;}
                    nullCounter++;
                }
            }
            if (nullCounter == length) {return false;}
        }
        return true;
    }

    public boolean boardLegal(Word word) {
        if (isInside(word)) {
            return isBasedOnExistingTiles(word) && isTilesOverlapping(word);
        }
        return false;
    }

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    private void placeWord(Tile[][] board, Word word) {
        for (int i = 0; i < word.getTiles().length; i++) {
            Tile currentTile = word.getTiles()[i];
            
            if (currentTile != null && currentTile.getLetter() != UNDERSCORE) {
                int row = word.isVertical() ? word.getRow() + i : word.getRow();
                int col = word.isVertical() ? word.getCol() : word.getCol() + i;
                board[row][col] = currentTile;
            }
        }
    }

    private Word getHorizontalWord(Tile[][] board, int row, int col, int length) {
        int startCol = col;
        while (startCol > 0 && board[row][startCol - 1] != null && board[row][startCol - 1].getLetter() != UNDERSCORE) {
            startCol--;
        }
    
        int endCol = col;
        while (endCol < BOARD_SIZE && board[row][endCol + 1] != null && board[row][endCol + 1].getLetter() != UNDERSCORE) {
            endCol++;
        }
    
        int newLength = endCol - startCol + 1;
        if (newLength > 1) {
                Tile[] tiles = new Tile[newLength];
                for (int i = 0; i < newLength; i++) {
                    tiles[i] = board[row][startCol + i];
                }
                return new Word(tiles, row, startCol, false);
        }
        return null;
    }

    private Word getVerticalWord(Tile[][] board, int row, int col, int length) {
        int startRow = row;
        while (startRow > 0 && board[startRow - 1][col] != null && board[startRow - 1][col].getLetter() != UNDERSCORE) {
            startRow--;
        }
    
        int endRow = row;
        while (endRow < BOARD_SIZE && board[endRow + 1][col] != null && board[endRow + 1][col].getLetter() != UNDERSCORE) {
            endRow++;
        }
        
        int newLength = endRow - startRow + 1;
        if (newLength > 1) {
                Tile[] tiles = new Tile[newLength];
                for (int i = 0; i < newLength; i++) {
                    tiles[i] = board[startRow + i][col];
                }
                return new Word(tiles, startRow, col, true);
        }
        return null;
    }

    public ArrayList<Word> getWords(Word word) {
        Tile[][] tempBoard = getBoard().getTiles();
        ArrayList<Word> newWords = new ArrayList<>();
        int length = word.getTiles().length;
        int row, col;
        Word newWord;
        
        placeWord(tempBoard, word);

        for (int i = 0; i < length; i++) {
            if (word.isVertical()) {
                row = word.getRow() + i;
                col = word.getCol();
                newWord = getHorizontalWord(tempBoard, row, col, length);
                if (newWord != null && !(boardWords.contains(newWord))) {newWords.add(newWord);}
            } else {
                row = word.getRow();
                col = word.getCol() + i;
                newWord = getVerticalWord(tempBoard, row, col, length);
                if (newWord != null && !(boardWords.contains(newWord))) {newWords.add(newWord);}
            }
        }

        row = word.getRow();
        col = word.getCol();
        if (word.isVertical()) {
            newWord = getVerticalWord(tempBoard, row, col, length);
            if (newWord != null && !(boardWords.contains(newWord))) {
                newWords.add(newWord);
            }
        } else {
            newWord = getHorizontalWord(tempBoard, row, col, length);
            if (newWord != null && !(boardWords.contains(newWord))) {
                newWords.add(newWord);
            }
        }
        return newWords;
    }
    
    public int getScore(Word word) {
        int score = 0;
        Tile[] tiles = word.getTiles();
        int tileScore;
        int tripleWordScoreCount = 1, doubleWordScoreCount = 1;

        for (int i = 0; i < tiles.length; i++) {
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;
            
            if (tiles[i] == null) {
                if ((this.tiles[row][col] != null && this.tiles[row][col].getLetter() != UNDERSCORE) ||
                    tiles[i].getLetter() != UNDERSCORE) {
                    tileScore = this.tiles[row][col].getTileScore();
                } else {break;}
            } else {
                tileScore = tiles[i].getTileScore();
            }

            if (isTripleLetterScore(row, col)) {
                score += 3 * tileScore;
            } else if (isDoubleLetterScore(row, col)) {
                score += 2 * tileScore;
            } else {
                score += tileScore;
            }
            
            if (isDoubleWordScore(row, col)) {
                doubleWordScoreCount *= 2;
            } else if (isTripleWordScore(row, col)) {
                tripleWordScoreCount *= 3;
            }
        }
        return score * tripleWordScoreCount * doubleWordScoreCount;
    }

    public int tryPlaceWord(Word word) {
        int score = 0;
        
        if (!boardLegal(word) || !dictionaryLegal(word)) {
            return score;
        }
        
        for (Word newWord : getWords(word)) {
            if (dictionaryLegal(newWord)) {
                    boardWords.add(newWord);
                    score += getScore(newWord);
            } else {
                return 0;
            }
        }

        placeWord(this.tiles, word);
        
        if (isFirst) {isFirst = false;}
        return score;
    }
}
