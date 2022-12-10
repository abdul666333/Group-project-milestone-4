import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The players name and operations done such as who's turn it is, scores, and tile racks of players
 * @author Umniyah Mohammed, 101158792
 * @version 4.0
 */
public class Player implements Serializable {
    private String name;
    private TilePile tilePile;
    private int score;
    private int turn;
    private Tile[] tileRack;
    public static final int NUM_OF_TILES = 7;
    private int passCount; //game end: all players pass twice, in consecutive turns
    private ArrayList<PlayerViews> views;

    /**
     * Constructor of Player class that takes a player's name
     * @param name
     */
    public Player(String name, TilePile tilePile){
        this.name = name;
        tileRack = new Tile[NUM_OF_TILES];
        score = 0;
        this.tilePile = tilePile;
        views = new ArrayList<>();
    }

    /**
     * Constructor of Player class that takes multiple parameters
     * @param name, String
     * @param tilePile, tilePile
     * @param tileRack, Tile[]
     * @param score, int
     * @param passCount, int
     * @author Hasan Al-Hasoo
     */
    public Player (String name, TilePile tilePile, Tile[] tileRack, int score, int passCount){
        this.name = name;
        this.tileRack = tileRack;
        this.score = score;
        this.passCount = passCount;
        this.tilePile = tilePile;
        views = new ArrayList<>();
    }

    /**
     * getName returns the player's name
     * @return String, player's name
     */
    public String getName(){
        return name;
    }

    /**
     * getScore method is an accessor
     * @return int, value of the score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * setScore method adds the word's score to the player's score
     * @param wordScore int, the word score
     */
    public void setScore(int wordScore)
    {
        score = score + wordScore;
    }

    /**
     * checkTurn checks whether it's the players turn
     * @return boolean, true if it is the players turn and false if not
     */
    public boolean checkTurn() {
        if (turn != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * setTurn sets the value of turn to 1 if it is the players turn
     */
    public void setTurn() {
        turn = 1;
    }

    /**
     * isTileRackEmpty checks whether the tile rack is empty
     * @return boolean, true if empty
     */
    public boolean isTileRackEmpty() {
        return tileRack == null;
    }

    /**
     * getTileRack is an accessor
     * @return Arraylist, the current tile rack of player
     */
    public Tile[] getTileRack() {
        return tileRack;
    }

    /**
     * addTileToRack adds tiles to rack
     */
    public void addTileToRack() {
        ArrayList<Tile> _tiles;
        for(int i = 0; i < NUM_OF_TILES; i++) {
            if(tileRack[i] == null) {
                _tiles = tilePile.getTile(1);
                tileRack[i] = _tiles.get(0);
            }
        }
    }

    /**
     * getTile returns the tile if it's in the player's tile rack
     * @return Tile, the tile in the rack
     */
    public Tile getTile(char tileCharacter){
        for(int i = 0; i < tileRack.length; i++){
            Tile t = tileRack[i];
            if(t!=null && t.getChar() == tileCharacter){
                tileRack[i] = null;
                return t;
            }
        }
        return null;
    }

    /**
     * getPassCount is an accessor
     * @return int, the number of consecutive turns players have passed
     */
    public int getPassCount(){return passCount;}

    /**
     * incrPCount increments the number of passed turns by 1
     */
    public void incrPCount(){
        passCount++;
    }

    /**
     * resetPCount resets the passed turns to zero
     */
    public void resetPCount(){
        passCount = 0;
    }

    /**
     * @author Hasan Al-Hasoo
     * Reads from the Arraylist of tiles passed and adds them to the tileRack
     * @param tileList the ArrayList passed containing tiles that must be added to the tileRack
     */
    public void addTiles(ArrayList<Tile> tileList) {

        for (int i = 0; i < tileRack.length; i++) {
            if (tileRack[i] == null && tileList.size()>0) {
                tileRack[i] = tileList.remove(0);
            }
        }
    }

    /**
     * @author Hasan Al-Hasoo
     * Determines if a tile in the player rack is selected.
     * If selected, add it to a local Arraylist and return it
     * @return ArrayList of selected Tile objects
     */

    public ArrayList<Tile> getSelectedTiles() {

        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile t: tileRack){
            if (t.isSelected()) {
                tiles.add(t);
            }
        }
        return tiles;
    }

    /**
     * getNonPlacedTiles gets the tiles that aren't placed on the board
     * @return tiles, ArrayList<Tile>
     * @author Tuna Uygun
     */
    public ArrayList<Tile> getNonPlacedTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile t: tileRack){
            if (!t.isPlaced()) {
                tiles.add(t);
            }
        }
        return tiles;
    }

    /**
     * removePlacedTiles removes the tiles placed on the board
     * @author Tuna Uygun
     */
    public void removePlacedTiles(){
        for (Tile t: tileRack) {
            if(t.isPlaced()){
                getTile(t.getChar());
            }
        }
    }

    /**
     * addPlayerViews adds the player views
     * @author Tuna Uygun
     */
    public void addPlayerViews(PlayerViews view){
        views.add(view);
    }

    /**
     * updatePlayerViews updates the player views
     * @author Tuna Uygun
     */
    public void updatePlayerViews(){
        for (PlayerViews v: views) {
            v.handlePlayerRackUpdate(getNonPlacedTiles());
        }
    }

    /**
     * @author Hasan Al-Hasoo
     * Empties the player rack for testing purposes
     */
    public void emptyPlayerRack() {
        Arrays.fill(tileRack, null);
    }

    /**
     * Returns a string representation of the players rack
     * @return String representation of the players rack
     * @author Tuna Uygun
     */
    public String getRackString(){
        String rack = "";
        for (Tile t: tileRack) {
            if(t != null){
                rack += t.getChar();
            }
        }
        return rack;
    }

    /**
     * Returns True if the player is a bot player, false otherwise.
     * @return True if the player is a bot player, false otherwise.
     * @author Tuna Uygun
     */
    public boolean isBotPlayer(){
        return false;
    }


    /**
     * @author Hasan Al-Hasoo
     * Convert the ArrayList<Player> obtained from the ScrabbleModel into a string. Will be passed in as a
     * parameter to ScrabbleState. Will be used to push and pop
     * @return
     */
    public ArrayList<String> getPlayerString() {

        ArrayList<String> playerListString = new ArrayList<>();

        for (Tile tile: tileRack){
            playerListString.add(tile.getChar() + "," +  tile.getScore());
        }

        playerListString.add(String.valueOf(passCount));
        playerListString.add(String.valueOf(score));
        playerListString.add(name);

        return playerListString;
    }


    /**
     * @author Hasan Al-Hasoo
     * Obtains an ArrayList of ArrayList<String> where each index of the ArrayList will be an ArrayList<String>
     * holding neccasary player details
     * @param players ArrayList of Players
     * @return The ArrayList<ArrayList<String>>
     */
    public static ArrayList<ArrayList<String>> getArrayListPlayerStrings(ArrayList<Player> players){

        ArrayList<ArrayList<String>> playerList = new ArrayList<>();

        for (Player p: players){
            playerList.add(p.getPlayerString());
        }

        return playerList;
    }

    /**
     * ArrayList of Tiles passed in as a string (player rack) will be converted back to the original state of the
     * tileRack held by the player
     * @param tileRack ArrayList of String representation of tileRack
     * @return The array of tiles (tileRack)
     */
    private static Tile[] aListTilesToTileRack(ArrayList<String> tileRack) {

        Tile[] t = new Tile[NUM_OF_TILES];

        for (int i = 0; i < NUM_OF_TILES; i++){
            t[i] = Tile.stringToTile(tileRack.get(i));
        }
        return t;
    }


    /**
     * Takes an ArrayList of "Player" Strings and a TilePile and returns a player
     * @param players ArrayList of String players
     * @param tilePile tilePile that will be used by the player
     * @return the player
     */
    public static Player StringToPlayer(ArrayList<String> players, TilePile tilePile){

        Tile[] t =  Player.aListTilesToTileRack((ArrayList<String>) players.subList(0,NUM_OF_TILES));

        Player p = new Player(players.get(9),tilePile, t,Integer.parseInt(players.get(8)),Integer.parseInt(players.get(7)));

        return p;
    }
}

