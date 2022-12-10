import java.io.Serializable;

/**
 * Tile has the value of a tile (the character and its score)
 * @author Hasan Al-Hasoo
 * @version 10/20/22
 */
public class Tile implements Serializable {
    private int score; //The value associated with each tile
    private char c;    //The char associated with each tile

    private boolean selected;
    private boolean placed;

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }



    public boolean isPlaced() {
        return placed;
    }

    /**
     * Tile constructor
     * @param score the value field
     * @param c the character field
     */
    public Tile(int score, char c){
        selected = false;
        this.score = score;
        this.c = c;
        placed = false;
    }

    /**
     * Getter method for the value associated with the tile
     * @return the value associated with the tile
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter method for the chaecter assocaited with the tile
     * @return the char associated with the tile
     */
    public char getChar() {
        return c;
    }

    /**
     * Getter method for field: "selected"
     * @return the field: "selected"
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * If the tile is selected, deselect and vice versa.
     */
    public void toggleSelect () {
        selected = !selected;
    }

    /**
     * Setter for the tile's character
     * @author Umniyah Mohammed
     * @param c, char
     */
    public void setChar(char c){
        this.c = c;
    }

    /**
     * toString returns the value given to it in string format
     * @return String
     * @author Umniyah Mohammed
     */
    public String toString(){
        return String.valueOf(c);
    }

    /**
     * stringToTile converts a string to a tile
     * @param tile, String
     * @return Tile
     * @author Hasan Al-Hasoo
     */
    public static Tile stringToTile(String tile){
        String[] split = tile.split(",");
        Tile t = new Tile(Integer.parseInt(split[1]), split[0].charAt(0));
        return t;
    }
}
