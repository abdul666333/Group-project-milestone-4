
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class implements several tests which checks all components of the ScrabbleModel class for proper
 * serialization
 * @author Hasan Al-Hasoo
 */
public class ScrabbleModelTest {

    private BoardModel boardModel;
    private ScrabbleModel scrabbleModel;
    private TilePile tilePile;


    /**
     * The test constructor. Initializes an ArrayList of Strings as player names, a TilePile object,
     * 2 players (playerOne and playerTwo), adds the players to the ArrayList of Strings, creates a new
     * ScrabbleModel passing in the player Strings and gets the board used by the ScrabbleModel instance.
     * @author Hasan Al-Hasoo
     */
    @Before
    public void setUp() {
        ArrayList<String> playerNames;
        playerNames = new ArrayList<>();
        tilePile = new TilePile();
        Player playerOne = new Player("Player 1", tilePile);
        Player playerTwo = new Player("Player 2", tilePile);
        playerNames.add(playerOne.getName());
        playerNames.add(playerTwo.getName());
        scrabbleModel = new ScrabbleModel(playerNames);
        boardModel = scrabbleModel.getBoard();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test method to test if the state of the board is correctly serialized and de-serialized. The board is first
     * filled with tiles then serialized via exportScrabble method. The imported / loaded scrabbleModel is assigned to a
     * local ScrabbleModel variable to maintain a reference. Fields that make up the board are then tested
     * i.e. double word squares (and letter squares), triple word squares (and letter squares) as well as the
     * tiles that were placed prior to the serialization.
     * @author Hasan Al-Hasoo
     */
    @Test
    public void testBoardExport() {
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        scrabbleModel.exportScrabble("test");
        ScrabbleModel scrabbleModelTwo = new ScrabbleModel(new ArrayList<>(Arrays.asList("P1", "P2")));
        ScrabbleModel importedModel = scrabbleModelTwo.importScrabble(("test"));
        scrabbleModelTwo.updateScrabbleModel(new ScrabbleEvent(importedModel));
        String stringBoardImport = scrabbleModelTwo.getBoard().toString();
        assertEquals(boardModel.toString(),stringBoardImport);
        assertArrayEquals(boardModel.getDoubleLetterSqaures(), scrabbleModelTwo.getBoard().getDoubleLetterSqaures());
        assertArrayEquals(boardModel.getTripleLetterSqaures(), scrabbleModelTwo.getBoard().getTripleLetterSqaures());
        assertArrayEquals(boardModel.getDoubleWordSqaures(),   scrabbleModelTwo.getBoard().getDoubleWordSqaures());
        assertArrayEquals(boardModel.getTripleWordSqaures(),   scrabbleModelTwo.getBoard().getTripleWordSqaures());
    }

    /**
     * Test method to assure that all players were successfully serialized and de-serialized correctly. First,
     * serialized the scrabbleModel instance created in the constructor and tested all player Strings
     * for equality
     * @author Hasan Al-Hasoo
     */
    @Test
    public void testPlayerExport() {
        scrabbleModel.exportScrabble("test");
        ScrabbleModel scrabbleModel1 = scrabbleModel.importScrabble("test");
        assertEquals(scrabbleModel.getPlayers().size(), scrabbleModel1.getPlayers().size());

        for (int i = 0; i < scrabbleModel.getPlayers().size(); i ++)
        {
            assertEquals(scrabbleModel.getPlayers().get(i).getPlayerString(), scrabbleModel1.getPlayers().get(i).getPlayerString());
        }
    }

    /**
     * Test method to assure the isFirstRound boolean was successfully serialized and deserialized correctly.
     * Serialized scrabbleModel instance created in the constructor and assigned the loaded ScrabbleModel to
     * a variable to maintain a reference. Compares the original ScrabbleModel isFirstRound boolean to the
     * deserialized ScrabbleModel assigned to a variable.
     * @author Hasan Al-Hasoo
     */
    @Test
    public void testFirstRoundExport() {
        scrabbleModel.exportScrabble("test");
        ScrabbleModel scrabbleModel1 = scrabbleModel.importScrabble("test");
        assertEquals(scrabbleModel.isFirstRound(), scrabbleModel1.isFirstRound());
    }

    /**
     * Test method to assure the TilePile is correctly serialized and deserialized. First, a few tiles
     * are placed. The ScrabbleModel instance is then serialized and deserialized in a new ScrabbleModel variable
     * to maintain reference. Compares the String representation of the TilePiles of the original ScrabbleModel
     * instance and the deserialized instance.
     * @author Hasan Al-Hasoo
     */
    @Test
    public void testTilePileExport() {
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        scrabbleModel.exportScrabble("test");
        ScrabbleModel scrabbleModelTwo = new ScrabbleModel(new ArrayList<>(Arrays.asList("P1", "P2")));
        ScrabbleModel importedModel = scrabbleModelTwo.importScrabble(("test"));
        scrabbleModelTwo.updateScrabbleModel(new ScrabbleEvent(importedModel));
        assertEquals(scrabbleModel.getTilePile().getTilePileString(), scrabbleModelTwo.getTilePile().getTilePileString());
    }

    /**
     * Test method to assure the curPlayer field is successfully serialized and deserialized. The original
     * ScrabbleModel instance is serialized and deserialized into a ScrabbleModel variable to maintain a reference.
     * Compares the original ScrabbleModel's curPlayer in String form and the deserialized ScrabbleModel's curPlayer
     * in String form
     * @author Hasan Al-Hasoo
     */
    @Test
    public void testCurrPlayerExport() {
        scrabbleModel.exportScrabble("test");
        ScrabbleModel scrabbleModel1 = scrabbleModel.importScrabble("test");
        assertEquals(scrabbleModel.getActivePlayer().getPlayerString(), scrabbleModel1.getActivePlayer().getPlayerString());
    }
    /**
     * Test method to assure the curPlayer field is Same after undo functionality. The original
     * ScrabbleModel instance changes to next player but once we perform undo operation we revert back to original player
     * Compares the original ScrabbleModel's before undo operation curPlayer in String form and after undo ScrabbleModel's curPlayer
     * in String form
     * @author Abdal Alkawasmeh
     */
    @Test
    public void testUndoCurrentPlayerTest(){
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);

        ScrabbleModel scrableModelBeforeUndo = scrabbleModel;
        scrabbleModel.place(false);

        try {
            scrabbleModel.undo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(scrabbleModel.getActivePlayer().getPlayerString(), scrableModelBeforeUndo.getActivePlayer().getPlayerString());

    }

    /**
     * Test method to assure the curPlayer field is Same after Redo functionality. The original
     * ScrabbleModel instance changes to currplayer once we perform Redo operation we revert back to original player
     * to perform redo we need to perform undo first
     * Compares the original ScrabbleModel's and model after undo operation curPlayer in String form and after redo ScrabbleModel's curPlayer
     * in String form
     * @author Abdal Alkawasmeh
     */
    @Test
    public void testRedo(){
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        try {
            scrabbleModel.saveState();
            ScrabbleModel scrableModelBeforeUndoRedo = scrabbleModel;
            scrabbleModel.undo();
            scrabbleModel.undo();
            scrabbleModel.redo();
            scrabbleModel.redo();
            assertEquals(scrabbleModel.getActivePlayer().getPlayerString(), scrableModelBeforeUndoRedo.getActivePlayer().getPlayerString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Test method to assure the curPlayer field is Same after Redo functionality on current instance. The original
     * ScrabbleModel instance on currplayer once we perform Redo operation nothing happens as we are on latest instance
     * Compares the original ScrabbleModel's selected tiles and model after redo operation selected tiles
     * @author Abdal Alkawasmeh
     */
    @Test
    public void testEmptyRedo(){
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);

        try {
            scrabbleModel.saveState();
            ScrabbleModel scrableModelBeforeUndoRedo = scrabbleModel;
            scrabbleModel.redo();
            assertEquals(scrabbleModel.getActivePlayer().getSelectedTiles(), scrableModelBeforeUndoRedo.getActivePlayer().getSelectedTiles());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Test method to assure the selected tile is same as initial state after Undo functionality on current instance. The original
     * ScrabbleModel instance selected tiles will revert once we perform undo operation
     * Compares the original ScrabbleModel's selected tiles and model after undo operation selected tiles
     * @author Abdal Alkawasmeh
     */
    @Test
    public void testUndoTileTest(){
        ScrabbleModel InitialscrableModel = scrabbleModel;
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        scrabbleModel.place(false);
        try {
            scrabbleModel.undo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(scrabbleModel.getActivePlayer().getSelectedTiles(), InitialscrableModel.getActivePlayer().getSelectedTiles());
    }

    /**
     * Test method to assure the selected tile is same after Undo/ redo functionality on current instance. The original
     * ScrabbleModel instance selected tiles will be same once we perform Redo operation
     * Compares the original ScrabbleModel's selected tiles and model after undo operation selected tiles
     * @author Abdal Alkawasmeh
     */
    @Test
    public void testRedoTileTest(){
        boardModel.placeOnBoard(7, 7 , 'h',  TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7,  'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10,7,  'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(11,7,  'o',  TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        try {
            scrabbleModel.saveState();
            ScrabbleModel scrableModelBeforeUndoRedo = scrabbleModel;
            scrabbleModel.undo();
            scrabbleModel.undo();
            scrabbleModel.redo();
            scrabbleModel.redo();
            assertEquals(scrabbleModel.getActivePlayer().getSelectedTiles(), scrableModelBeforeUndoRedo.getActivePlayer().getSelectedTiles());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}


