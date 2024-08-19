package test;

import java.util.Arrays;

public class Word {
    private int row, col;
    private Tile[] tiles;
    private boolean vertical;

    public Word (Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.col = col;
        this.row = row;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {
        return this.tiles;
    }

    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
	
    public boolean isVertical() {
        return this.vertical;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        if (!Arrays.equals(tiles, other.tiles))
            return false;
        if (vertical != other.vertical)
            return false;
        return true;
    }

    public String toString() {
        String toReturn = "";
        for (int i = 0; i < this.tiles.length; i ++) {
            if (tiles[i] != null) {
                toReturn += tiles[i].toString();
            } else {
                toReturn += "null";
            }
        }
        return toReturn;
    }
}