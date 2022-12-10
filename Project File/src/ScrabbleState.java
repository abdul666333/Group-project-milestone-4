import java.util.ArrayList;

/**
 * ScrabbleState class saves the state of the game for the undo/redo feature
 * @version 1.0
 * @author Hasan Al-Hasoo
 */
public class ScrabbleState {
    private String[][] model;
    private boolean isFirstRound;
    private ArrayList<ArrayList<String>> playerList;
    private ArrayList<String> tilePile; //Field storing string rep
    private ArrayList<String> currPlayer; //Field storing string rep of currPlayer (obtained from ScrabbleModel

    /**
     * @author Hasan Al-Hasoo (Wrote Class/Design)
     * ScrabbleState Constructor. Takes a string representation of the BoardModel, ArrayList Of String Players,
     * String tilePiles, String currPlayer.
     * @param isFirstRound Boolean of isFirstRound obtained from ScrabbleModel
     * @param model String Representation of the BoardModel
     * @param playerList String Representation of ArrayList Of Players
     * @param tilePile String Representation of TilePile
     * @param currPlayer String Representation of the Current Player Obtained From ScrabbleModel
     */
    public ScrabbleState(boolean isFirstRound, String[][] model, ArrayList<ArrayList<String>> playerList, ArrayList<String> tilePile, ArrayList<String> currPlayer) {
        this.isFirstRound = isFirstRound;
        this.model = model;
        this.playerList = playerList;
        this.tilePile = tilePile;
        this.currPlayer = currPlayer;
    }

    /**
     * @author Hasan Al-Hasoo
     * Return the string representation of the model field
     * @return model of type String
     */
    public String[][] getModel() {
        return model;
    }

    /**
     * @author Hasan Al-Hasoo
     * Return the boolean indictating the state of the game (whether its the firts round or not)
     * @return boolean indicating the state of the first round
     */
    public boolean isFirstRound() {
        return isFirstRound;
    }

    /**
     * @author Hasan Al-Hasoo
     * Return the string representation of the list of players
     * @return Player List of type ArrayList<String>
     */
    public ArrayList<ArrayList<String>> getPlayerList() {
        return playerList;
    }

    /**
     * @author Hasan Al-Hasoo
     * Return the string representation of the TilePile field
     * @return TilePile of type String
     */
    public ArrayList<String> getTilePile() {
        return tilePile;
    }

    /**
     * @author Hasan Al-Hasoo
     * Return the field currPlayer (used in ScrabbleModel)
     * @return The string representation of the current Player (obtained from ScrabbleModel)
     */
    public ArrayList<String> getCurrPlayer() {
        return currPlayer;
    }
}
