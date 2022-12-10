import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

/**
 * @author Hasan Al-Hasoo
 * @author Tuna Uygun
 * @author Umniyah Mohammed c:
 * @version 2.0
 *
 * The test harness regarding several tests with respect to word placements and scoring
 */
public class BoardModelTest {

    BoardModel boardModel; //BoardModel instance field

    @Before
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Player 1");
        arrayList.add("Player 2");
        ScrabbleModel scrabbleModel = new ScrabbleModel(arrayList);
        scrabbleModel.getActivePlayer().emptyPlayerRack();
        boardModel = new BoardModel(scrabbleModel);boardModel.resetSquaresFilledInCurrentTurnList();
    }

    @After
    public void tearDown() {
        boardModel.clearBoard();
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Test for starting the game without placing a word in the middle
     */

    @Test
    public void testInvalidFirstRound() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(4, 4, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(4, 5, 't', TilePile.Letter.T.score());
        int score = boardModel.placeWord(true);
        assertEquals(BoardModel.INVALID_FIRST_ROUND,score);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Test for first player placing a tile in the middle. This test should fail as the logic
     * for doubling the first tile score in the middle has not yet been implemented
     */

    @Test
    public void testValidFirstRound() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(7, 8, 't', TilePile.Letter.T.score());
        int score = boardModel.placeWord(true);
        assertEquals(4,score);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests whether score is properly given to the player if a valid vertical word is placed
     * The logic for starting in the middle has not yet been implemented (double the score) so this test is
     * SUPPOSED to fail
     */

    @Test
    public void testPlaceVertical(){
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7 , 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7, 'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7, 'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10, 7, 'l', TilePile.Letter.L.score());
        boardModel.placeOnBoard(11, 7, 'o', TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        assertEquals(18,score);
    }

    /**
     * @author Tuna Uygun
     * @author Hasan Al-Hasoo
     * Tests the case of when it is the first round and the player attempts to place the word "hello" vertically
     *
     * The logic for starting in the middle and doubling tile score has not yet been implemented (double the score)
     * so this test is SUPPOSED to fail
     *
     *
     */
    @Test
    public void testPlaceHorizontal(){
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7 , 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(7, 8, 'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(7, 9, 'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(7, 10, 'l', TilePile.Letter.L.score());
        boardModel.placeOnBoard(7, 11, 'o', TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        assertEquals(18,score);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where a player places the first word "hello" vertically and second word "the" horizontally
     * intersecting "hello".
     *
     * The logic for starting in the middle has not yet been implemented (double the score) so this test is
     * SUPPOSED to fail
     */
    @Test
    public void testHorizontalVerticalIntersect() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7 , 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7, 'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7, 'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10, 7, 'l', TilePile.Letter.L.score());
        boardModel.placeOnBoard(11, 7, 'o', TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        assertEquals(18, score);
        boardModel.placeOnBoard(7, 6 , 't', 1);
        boardModel.placeOnBoard(7, 8, 'e', 1);
        int scoreTwo = boardModel.placeWord(false);
        assertEquals(6, scoreTwo);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case which the first player places the word "hello" vertically and the second player
     * places the word "the" horizontally without intersecting.
     *
     * The logic for starting in the middle has not yet been implemented (double the score) so this test is
     * SUPPOSED to fail
     */
    @Test
    public void testHorizontalVerticalNoIntersect() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7 , 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(8, 7, 'e',  TilePile.Letter.E.score());
        boardModel.placeOnBoard(9, 7, 'l',  TilePile.Letter.L.score());
        boardModel.placeOnBoard(10, 7, 'l', TilePile.Letter.L.score());
        boardModel.placeOnBoard(11, 7, 'o', TilePile.Letter.O.score());
        int score = boardModel.placeWord(true);
        assertEquals(18, score);
        boardModel.placeOnBoard(13, 6 , 't', TilePile.Letter.T.score());
        boardModel.placeOnBoard(13, 7 , 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(13, 8, 'e',  TilePile.Letter.E.score());
        int scoreTwo = boardModel.placeWord(false);
        assertEquals(BoardModel.INVALID_WORD_PLACEMENT, scoreTwo);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where a player places a word that is not in the dictionary
     *
     * This test should pass
     */
    @Test
    public void testInvalidWord() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(7, 8, 'e', TilePile.Letter.E.score());
        boardModel.placeOnBoard(7, 9, 'f', TilePile.Letter.F.score());
        int score = boardModel.placeWord(true);
        assertEquals(BoardModel.INVALID_WORD, score);

    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where hello is placed horizontally in the middle then the word "polar" is placed
     * vertically intersecting "he(l)lo"
     *
     * This test is SUPPOSED to fail as the logic for doubling the word placed in the middle and tripling
     * respective tile scores has NOT yet been implemented
     */
    @Test
    public void testTripleLetterScore() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7, 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(7, 8, 'e', TilePile.Letter.E.score());
        boardModel.placeOnBoard(7, 9, 'l', TilePile.Letter.L.score());
        boardModel.placeOnBoard(7, 10, 'l',TilePile.Letter.L.score());
        boardModel.placeOnBoard(7, 11, 'o',TilePile.Letter.O.score());

        int score = boardModel.placeWord(true);
        assertEquals(18, score);

        boardModel.placeOnBoard(5, 9, 'p', TilePile.Letter.P.score()); //Triple Square
        boardModel.placeOnBoard(6, 9, 'o', TilePile.Letter.O.score());
        boardModel.placeOnBoard(8, 9, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(9, 9, 'r', TilePile.Letter.R.score()); //Triple Square

        int scoreTwo = boardModel.placeWord(false);
        assertEquals(15, scoreTwo);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where a word is placed and a letter making
     * up that word is placed over a double letter score tile
     *
     * This test SHOULD fail as the logic for doubling letter or word score has NOT yet been implemented
     *
     */
    @Test
    public void testDoubleLetterScore(){
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 3 ,'g', TilePile.Letter.G.score()); //DL
        boardModel.placeOnBoard(7, 4, 'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(7, 5, 'o', TilePile.Letter.O.score());
        boardModel.placeOnBoard(7, 6, 's', TilePile.Letter.S.score());
        boardModel.placeOnBoard(7, 7, 't', TilePile.Letter.T.score());
        int score = boardModel.placeWord(true);
        assertEquals(22,score);
    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where a player places a tile ontop of a triple word score square
     *
     * This test SHOULD fail as the logic regarding triping word score has NOT yet been implemented
     */
    @Test
    public void testTripleWordScore() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 0 ,'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(7, 1, 'b', TilePile.Letter.B.score());
        boardModel.placeOnBoard(7, 2, 's', TilePile.Letter.S.score());
        boardModel.placeOnBoard(7, 3, 't', TilePile.Letter.T.score());
        boardModel.placeOnBoard(7, 4, 'r', TilePile.Letter.R.score());
        boardModel.placeOnBoard(7, 5 ,'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(7, 6, 'c', TilePile.Letter.C.score());
        boardModel.placeOnBoard(7, 7, 't', TilePile.Letter.T.score());
        int score = boardModel.placeWord(true);
        assertEquals(78,score);

    }

    /**
     * @author Hasan Al-Hasoo
     * @author Tuna Uygun
     * Tests the case where a player places all tiles in their rack in one turn. This results in a
     * bonus score of 50 being added
     *
     * This test SHOULD fail as the logic regarding doubling word score has NOT yet been added
     */
    @Test
    public void testPlaceAllTilesInRack() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(4, 7 ,'h', TilePile.Letter.H.score());
        boardModel.placeOnBoard(5, 7, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(6, 7, 'b', TilePile.Letter.B.score());
        boardModel.placeOnBoard(7, 7, 'i', TilePile.Letter.I.score());
        boardModel.placeOnBoard(8, 7, 't', TilePile.Letter.T.score());
        boardModel.placeOnBoard(9, 7 ,'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(10, 7,'t', TilePile.Letter.T.score());
        int score = boardModel.placeWord(true);
        assertEquals(74,score);
    }

    /**
     * @author Tuna Uygun
     * Tests the case where a player places a word which also creates additional words due to
     * the interaction with tiles that are already on the board
     */
    @Test
    public void testMultipleWordInteractions() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7, 'g', TilePile.Letter.G.score()); // Double word
        boardModel.placeOnBoard(8, 7, 'o', TilePile.Letter.O.score());

        int score = boardModel.placeWord(true);
        assertEquals(6, score);

        boardModel.placeOnBoard(8, 6, 'h', TilePile.Letter.H.score()); // Double letter
        boardModel.placeOnBoard(8, 8, 'p', TilePile.Letter.P.score()); // Double Letter
        boardModel.placeOnBoard(8, 9, 'e', TilePile.Letter.E.score());

        int scoreTwo = boardModel.placeWord(false);
        assertEquals(16, scoreTwo);

        boardModel.resetSquaresFilledInCurrentTurnList();
        boardModel.placeOnBoard(7, 8, 'e', TilePile.Letter.E.score());
        boardModel.placeOnBoard(7, 9, 'l', TilePile.Letter.L.score());

        int scoreThree = boardModel.placeWord(false);
        assertEquals(10, scoreThree);
    }

    /**
     * Tests placing a single tile to complete a word using the tiles that are already
     * on the board
     * @author Tuna Uygun
     */
    @Test
    public void testSingleTilePlacement() {
        boardModel.setSquareMultipliers();
        boardModel.placeOnBoard(7, 7, 'f', TilePile.Letter.F.score());
        boardModel.placeOnBoard(7, 8, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(7, 9, 'n', TilePile.Letter.N.score());

        int score = boardModel.placeWord(true);
        assertEquals(12, score);

        boardModel.placeOnBoard(8, 7, 'o', TilePile.Letter.O.score());
        boardModel.placeOnBoard(9, 7, 'x', TilePile.Letter.X.score());

        int scoreTwo = boardModel.placeWord(false);
        assertEquals(13, scoreTwo);

        boardModel.placeOnBoard(6, 8, 't', TilePile.Letter.T.score());
        boardModel.placeOnBoard(8, 8, 'n', TilePile.Letter.N.score());

        int scoreThree = boardModel.placeWord(false);
        assertEquals(7, scoreThree);

        boardModel.placeOnBoard(8, 6, 'n', TilePile.Letter.N.score());
        int scoreFour = boardModel.placeWord(false);
        assertEquals(4, scoreFour);
    }

    /**
     * Reads a custom board xml file and tests if the new custom board multipliers are placed in the
     * correct location by placing intersecting words and comparing the expected scores
     * @author Tuna Uygun
     */
    @Test
    public void testCustomBoardPlacement() {
        CustomBoardReader reader = new CustomBoardReader();
        reader.readCustomBoardFile("customBoard.xml");
        reader.setMultiplier(boardModel);

        boardModel.placeOnBoard(7, 7, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(8, 7, 'c', TilePile.Letter.C.score());
        boardModel.placeOnBoard(9, 7, 'c', TilePile.Letter.C.score());
        boardModel.placeOnBoard(10, 7, 'o', TilePile.Letter.O.score());
        boardModel.placeOnBoard(11, 7, 'u', TilePile.Letter.U.score());
        boardModel.placeOnBoard(12, 7, 'n', TilePile.Letter.N.score());
        boardModel.placeOnBoard(13, 7, 't', TilePile.Letter.T.score());

        int score = boardModel.placeWord(true);
        assertEquals(72, score);

        boardModel.placeOnBoard(11, 6   , 'c', TilePile.Letter.C.score());
        boardModel.placeOnBoard(11, 8, 's', TilePile.Letter.S.score());
        boardModel.placeOnBoard(11, 9, 't', TilePile.Letter.T.score());
        boardModel.placeOnBoard(11, 10, 'o', TilePile.Letter.O.score());
        boardModel.placeOnBoard(11, 11, 'm', TilePile.Letter.M.score());

        int scoreTwo = boardModel.placeWord(false);
        assertEquals(13, scoreTwo);

        boardModel.placeOnBoard(10, 10, 'b', TilePile.Letter.B.score());
        boardModel.placeOnBoard(12, 10, 'a', TilePile.Letter.A.score());
        boardModel.placeOnBoard(13, 10, 'r', TilePile.Letter.R.score());
        boardModel.placeOnBoard(14, 10, 'd', TilePile.Letter.D.score());

        int scoreThree = boardModel.placeWord(false);
        assertEquals(36, scoreThree);
    }

}
