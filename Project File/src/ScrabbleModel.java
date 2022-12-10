import java.io.*;
import java.util.*;

/**
 * The Scrabble class will act as the main class which will instantiate all
 * required game aspects. Scrabble will
 * also listen for commands to suppprt game logic
 *
 * @author Hasan Al-Hasoo
 * @author Tuna Uygun
 * @author Umniyah Mohammed
 */

public class ScrabbleModel implements Serializable {
    private Stack<byte[]> scrabbleStackRedo;
    private Stack<byte[]> scrabbleStackUndo;
    private BoardModel b; // field variable which keeps track of board

    private Player curPlayer; // field keeps track of current player
    private int numPlayers; // field stores the number of players
    private TilePile tilePile; // field keeps track of TilePile

    private ArrayList<Player> players; // Arraylist that stores all players
    private boolean isFirstRound; // keeps track of the first round

    public static final int MIN_PLAYERS = 2; // const variable for minimum players
    public static final int MAX_PLAYERS = 4; // const variable for maximum players
    private List<ScrabbleViews> views;

    /**
     * Constructor for Scrabble class. instantiates a new board, random variable and
     * TilePile.
     * Loops through the ArrayList of player names passed as a paramteter and adds
     * these players to the
     * players list. The player with respect to the random value generated
     * corresponding
     * to the index of the Arraylist of players will play first
     *
     * @param playerNames is the ArrayList passed in of players
     * @author Hasan Al-Hasoo
     */
    public ScrabbleModel(ArrayList<String> playerNames) {

        scrabbleStackRedo = new Stack();

        scrabbleStackUndo = new Stack();

        players = new ArrayList<>();
        b = new BoardModel(this);
        Random r = new Random();
        numPlayers = playerNames.size();
        tilePile = new TilePile();
        views = new ArrayList<>();

        for (String name : playerNames) {
            Player p;
            if (name.matches("^Bot [0-4]$")) {
                p = new BotPlayer(name, tilePile, b);
            } else {
                p = new Player(name, tilePile);
            }
            p.addTileToRack();
            players.add(p);
        }

        // first player has to be human (restriction)
        do {
            int randomPlayerIndex = r.nextInt(playerNames.size());
            curPlayer = players.get(randomPlayerIndex);
        } while (curPlayer.getName().matches("^Bot [0-4]$"));

        isFirstRound = true;
        try {
            saveState();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return Active player
     * @author Tuna Uygun
     *         Returns the active player
     */
    public Player getActivePlayer() {
        return curPlayer;
    }

    /**
     * getPlayers is an accessor
     *
     * @return players, Player
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     *         Proceeds to the next player. If the current player
     *         is the last player, loop back to the first player
     */
    public void switchPlayers() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(curPlayer)) {
                curPlayer = players.get((i + 1) % players.size());
                break;
            }
        }
        if (curPlayer.isBotPlayer()) {
            BotPlayer bot = (BotPlayer) curPlayer;
            Boolean isWordPlaced = bot.botPlay();
            if (isWordPlaced) {
                curPlayer.removePlacedTiles();
                curPlayer.addTileToRack();
                switchPlayers();
            } else {
                pass();
            }
            for (ScrabbleViews view : views) {
                view.handleScrabbleUpdate(new ScrabbleEvent(this));
            }
            b.updateBoardViews();
        }
    }

    /**
     * @return winner, String of the winner/s
     * @author Tuna and Umniyah
     *         resultString stores the results of the game
     */
    public String resultsString() {
        String winner = "";
        Player playerWithHighestScore = players.get(0);
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getScore() > playerWithHighestScore.getScore()) {
                playerWithHighestScore = players.get(i);
            }
        }
        ArrayList<Player> winners = new ArrayList<>();
        for (Player p : players) {
            if (p.getScore() == playerWithHighestScore.getScore()) {
                winners.add(p);
            }
        }

        if (winners.size() == 1) {
            winner = "\nThe winner is " + winners.get(0).getName() + "(score: " + winners.get(0).getScore() + ")";
        } else {
            winner += "\nThe winners are ";
            for (int i = 0; i < winners.size() - 1; i++) {
                winner += winners.get(i).getName() + "(score: " + winners.get(i).getScore() + ")" + ", ";
            }
            winner += "and " + winners.get(winners.size() - 1).getName() + "(score: "
                    + winners.get(winners.size() - 1).getScore() + ")" + ".";
        }
        winner += "\nThank you for playing. Good bye!";
        return winner;
    }

    /**
     * @author UTH
     *         This method allows a player to swap a tile on the board for another
     *         one present on the player's tileRack
     */
    public void swap() {
        b.returnPlacedTilesToPlayer();
        ArrayList<Tile> selectedTiles = this.getActivePlayer().getSelectedTiles();
        if (selectedTiles.size() > 0) {
            for (int i = 0; i < selectedTiles.size(); i++) {
                Tile t = curPlayer.getTile(selectedTiles.get(i).getChar());
                t.toggleSelect();
                tilePile.addTile(t);
                curPlayer.addTileToRack();
            }
            curPlayer.resetPCount();
            switchPlayers();
            for (ScrabbleViews view : views) {
                view.handleScrabbleUpdate(new ScrabbleEvent(this));
            }
        } else {
            for (ScrabbleViews view : views) {
                view.handleWarningMessage("Select at least one tile!");
            }
        }
    }

    /**
     * @author Umniyah Mohammed
     *         Terminates the game
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     *         Allows the user to place a word on the board
     */
    public void place(Boolean isBotsTurn) {
        int wordScore = wordScore = b.placeWord(isFirstRound);

        if (wordScore < 0) {
            // curPlayer.addTiles(validTiles);
            scrabbleStackUndo.pop();
            String message;
            switch (wordScore) {
                case BoardModel.INVALID_WORD:
                    message = "ERROR: The word is not valid";
                    break;
                case BoardModel.INVALID_FIRST_ROUND:
                    message = "ERROR: The first letter of the game needs to have a tile in the center";
                    break;
                case BoardModel.INVALID_WORD_PLACEMENT:
                    message = "ERROR: The word is not placed in a legal position";
                    break;
                default:
                    message = "ERROR";
                    break;
            }
            for (ScrabbleViews view : views) {
                if (!isBotsTurn) {
                    view.handleWarningMessage(message);
                }

            }
        } else {
            curPlayer.removePlacedTiles();
            curPlayer.addTileToRack();
            curPlayer.setScore(wordScore);
            isFirstRound = false;
            for (ScrabbleViews view : views) {
                view.handleScrabbleUpdate(new ScrabbleEvent(this));
            }
            switchPlayers();
            for (ScrabbleViews view : views) {
                view.handleScrabbleUpdate(new ScrabbleEvent(this));
            }
            b.updateBoardViews();
        }
    }

    /**
     * @return true if the end game sequence was successful, otherwise false
     * @author UTH
     *         Passes the turn from one player to the next registered player. Once
     *         the final registered player switches turns,
     *         loop back to the first player
     */
    public void pass() {
        b.returnPlacedTilesToPlayer();
        curPlayer.incrPCount();
        for (Player p : players) {
            if (p.getPassCount() < 2) {
                switchPlayers();
                for (ScrabbleViews view : views) {
                    view.handleScrabbleUpdate(new ScrabbleEvent(this));
                }
                return;
            }
        }
        for (ScrabbleViews view : views) {
            view.handleEndGameResults(resultsString());
        }
        this.quit();
    }

    /**
     * @author Umniyah Mohammed
     *         Prints a set of operations which may be invoked by the user
     */
    public void help() {
        String helpString = "Please place a valid word by clicking on a tile from the rack and on the square on the board\n"
                +
                "OR click on one of the following commands to proceed:\n " +
                " -> SWAP: swaps selected tiles of the player's rack\n" +
                " -> PASS: passes turn to the next player\n" +
                " -> QUIT: quits the game\n" +
                " -> HELP: shows the help dialog\n" +
                " -> SUBMIT: submit the current word placed on the board";
        for (ScrabbleViews view : views) {
            view.handleWarningMessage(helpString);
        }
    }

    /**
     * @param view, ScrabbleViews
     * @author Umniyah Mohammed
     *         addScrabbleViews adds the views
     */
    public void addScrabbleViews(ScrabbleViews view) {
        views.add(view);
    }

    /**
     * @return b, Board
     * @author Umniyah Mohammed
     *         getBoard is an accessor
     */
    public BoardModel getBoard() {
        return b;
    }

    /**
     * exportScrabble saves a Scrabble game (object) using serialization
     *
     * @param fileName, String
     * @author Umniyah Mohammed
     */
    public void exportScrabble(String fileName) {
        FileOutputStream fileOutputStream = null;
        File file = new File(fileName);
        try {
            fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * importScrabble loads a previously saved Scrabble game from files using
     * serialization
     *
     * @param fileName, String
     * @return the saved Scrabble object
     */
    public ScrabbleModel importScrabble(String fileName) {
        FileInputStream fileInputStream = null;
        File file = new File(fileName);
        ScrabbleModel scrabbleModel = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(fileInputStream);
            scrabbleModel = (ScrabbleModel) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return scrabbleModel;
    }

    /**
     * updateScrabbleModel updates the scrabble model
     *
     * @param event, ScrabbleEvent
     * @author Tuna Uygun
     */
    public void updateScrabbleModel(ScrabbleEvent event) {
        this.curPlayer = event.getActivePlayer();
        this.players = (ArrayList<Player>) event.getPlayers();
        ArrayList<BoardViews> boardViews = b.getViews();
        this.b = event.getBoard();
        this.b.setViews(boardViews);
        this.tilePile = event.getTilePile();
        this.isFirstRound = event.isFirstRound();
        this.numPlayers = event.getNumPlayers();
        for (ScrabbleViews view : views) {
            view.updateScrabbleModel(event);
        }
        b.updateBoardViews();
    }

    /**
     * getNumPlayers is an accessor
     *
     * @return numPlayers, int
     * @author Tuna Uygun
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * getTilePile is an accessor
     *
     * @return tilePile, TilePile
     * @author Tuna Uygun
     */
    public TilePile getTilePile() {
        return tilePile;
    }

    /**
     * isFirstRound checks whether it is the first round of the game
     *
     * @return isFirstRound, boolean
     * @author Tuna Uygun
     */
    public boolean isFirstRound() {
        return isFirstRound;
    }

    /**
     * Function for storing the current state of the game board
     * @author Abdal Alkawasmeh
     * @throws IOException
     *
     */
    public void saveState() throws Exception {
        // Push the game state onto the undo stack

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(this);
        byte[] state = byteArrayOutputStream.toByteArray();
        scrabbleStackUndo.push(state);

        // Clear the redo stack
        scrabbleStackRedo.clear();
    }

    /**
     * Function for undoing a move
     *
     * @author Abdal Alkawasmeh
     * @return
     * @throws Exception
     */
    public ScrabbleModel undo() throws Exception {
        // Check if the undo stack is empty
        if (!scrabbleStackUndo.isEmpty()) {
            // Pop the top state from the undo stack and apply it to the game board
            byte[] loaded = scrabbleStackUndo.pop();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(loaded);
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);

            ScrabbleModel scrabbles = (ScrabbleModel) ois.readObject();
            // Push the previous state onto the redo stack
            scrabbleStackRedo.push(loaded);
            return scrabbles;
        }
        return this;
    }

    /**
     * Function for redoing a move
     * @author Abdal Alkawasmeh
     * @return
     * @throws Exception
     */
    public ScrabbleModel redo() throws Exception {
        // Check if the redo stack is empty
        if (!scrabbleStackRedo.isEmpty()) {
            // Pop the top state from the redo stack and apply it to the game board
            byte[] loaded = scrabbleStackRedo.pop();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(loaded);
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);

            ScrabbleModel scrabbles = (ScrabbleModel) ois.readObject();

            // Push the redone state onto the undo stack
            scrabbleStackUndo.push(loaded);
            return scrabbles;
        }
        return this;
    }
}
