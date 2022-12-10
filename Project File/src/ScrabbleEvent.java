import java.util.EventObject;
import java.util.List;

/**
 * ScrabbleEvent is used to send a signal from the model to the view
 * @author Umniyah Mohammed 101158792
 * @version 2.0
 */
public class ScrabbleEvent extends EventObject {
    private List<Player> players;
    private BoardModel board;
    private int numPlayers;
    private boolean isFirstRound;
    private TilePile tilePile;
    private Player activePlayer;

    /**
     * Constructor for ScrabbleEvent class
     * @param scrabbleModel, the scrabble model
     * @author Umniyah Mohammed
     */
    public ScrabbleEvent(ScrabbleModel scrabbleModel){
        super(scrabbleModel);
        activePlayer = scrabbleModel.getActivePlayer();
        players = scrabbleModel.getPlayers();
        board = scrabbleModel.getBoard();
        numPlayers = scrabbleModel.getNumPlayers();
        isFirstRound = scrabbleModel.isFirstRound();
        tilePile = scrabbleModel.getTilePile();
    }

    /**
     * get the active player in the model
     * @return the active player
     * @author Umniyah Mohammed
     */
    public Player getActivePlayer(){
        return this.activePlayer;
    }


    /**
     * getPlayers is an accessor
     * @return players, List<Player>
     * @author Tuna Uygun
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * getBoard is an accessor
     * @return board, BoardModel
     * @author Tuna Uygun
     */
    public BoardModel getBoard() {
        return board;
    }

    /**
     * getNumPlayers is an accessor
     * @return numPlayers, int
     * @author Tuna Uygun
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * isFirstRound checks whether it is the first round of the game
     * @return isFirstRound, boolean
     * @author Tuna Uygun
     */
    public boolean isFirstRound() {
        return isFirstRound;
    }

    /**
     * getTilePile is an accessor
     * @return tilePile, TilePile
     * @author Tuna Uygun
     */
    public TilePile getTilePile() {
        return tilePile;
    }
}
