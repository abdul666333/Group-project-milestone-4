import java.io.Serializable;
import java.util.*;

/**
 * @author Hasan Al-Hasoo, 101196381
 * @version 1.1
 * TilePile stores a list of tiles in a backing data structure and supports operations such as random such as random
 * tile distribution to players
 */
public class TilePile implements Serializable {

    /**
     * @author Hasan Al-Hasoo
     * @version 1.0
     * Special enumerated class which an association between corresponding chars, number of each tile and the value of
     * each tile are stored
     */
    public enum Letter {
        A(9, 1),
        B(2, 3),
        C(2, 3),
        D(4, 2),
        E(12, 1),
        F(2, 4),
        G(3, 2),
        H(2, 4),
        I(9, 1),
        J(1, 8),
        K(1, 5),
        L(4, 1),
        M(2, 3),
        N(6, 1),
        O(8, 1),
        P(2, 3),
        Q(1, 10),
        R(6, 1),
        S(4, 1),
        T(6, 1),
        U(4, 1),
        V(2, 4),
        W(2, 4),
        X(1, 8),
        Y(2, 4),
        Z(1, 10);

        private final int count; //The number of tiles
        private final int score; //The value of the corresponding tile

        /**
         * @param count The number of tiles corresponding to a specific char
         * @param score The value of each tile corresponding to a specific char
         * @author Hasan Al-Hasoo
         * Constructor for enums
         */
        Letter(int count, int score) {
            this.count = count;
            this.score = score;
        }

        /**
         * @return the count field
         * @author Hasan Al-Hasoo
         * Getter method for count field
         */
        public int count() {
            return count;
        }

        /**
         * @return the value field
         * @author Hasan Al-Hasoo
         * Getter method for value field
         */
        public int score() {
            return score;
        }
    }

    private ArrayList<Tile> tiles; //The arraylist which will store all the constructed tiles
    public static final char blank = '*';

    /**
     * @author Hasan Al-Hasoo
     * Constructor for the pile of tiles. Will construct A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4,
     * M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1 tiles
     */
    public TilePile() {
        tiles = new ArrayList<Tile>();
        for (Letter letter : Letter.values()) {
            for (int i = 0; i < letter.count(); i++) {
                tiles.add(new Tile(letter.score(), letter.name().charAt(0)));
            }
        }
        tiles.add(new Tile(0, blank));
        tiles.add(new Tile(0, blank));
    }

    /**
     * TilePile constructor that takes an ArrayList of Strings and turns them into Tile objects and
     * adds to the field "tile" of type TilePile
     * @param tilePile ArrayList of String tiles
     */
    public TilePile(ArrayList<String> tilePile) {
        tiles = new ArrayList<Tile>();

        for (String s: tilePile){
            Tile t = Tile.stringToTile(s);
            tiles.add(t);
        }
    }

    /**
     * @param numTiles represents the number of tiles requested by the player
     * @return an arraylist containing numTiles randomly selected tiles from the backing arraylist
     * @author Hasan Al-Hasoo
     * Method used by players to request tiles after each turn. Draws numTiles tiles from the arraylist
     * and shrinks the size by the corresponding amount
     */
    public ArrayList<Tile> getTile (int numTiles) {

        Tile tile;
        ArrayList<Tile> listOfTiles = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < numTiles; i++) {
            int randomTileIndex = r.nextInt(tiles.size());
            tile = tiles.remove(randomTileIndex);
            listOfTiles.add(tile);
        }
        return listOfTiles;
    }

    /**
     * isTilePileEmpty checks whether the tile pile is empty or not
     * @author Umniyah Mohammed
     * @author Hasan Al-Hasoo [Refactored]
     * @return boolean, true if tile pile is empty
     */
    public boolean isTilePileEmpty() {
        return tiles.size() == 0;
    }

    /**
     * Adds the passed tile to the Arraylist of tiles
     * @param tile is the object that will be added to the ArrayList
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
    }


    /**
     * @author Hasan Al-Hasoo
     * Will be used to convert the TilePile from the ScrabbleModel
     * into a String representation
     * @return
     */
    public ArrayList<String> getTilePileString() {

        ArrayList<String> strList = new ArrayList<>();

        for (Tile tile: tiles){
            strList.add(tile.getChar() + "," +  tile.getScore());
        }

        return strList;
    }
}
