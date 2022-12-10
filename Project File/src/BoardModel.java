import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A class that performs necessary operations directly concerning the board
 *
 * @author Tuna Uygun
 * @author Hasan Al-Hasoo
 * @author Umniyah Mohammed
 * @version 10/25/22, v1.0
 */
public class BoardModel implements Serializable, Cloneable {

    public static final int INVALID_WORD = -1;
    public static final int INVALID_FIRST_ROUND = -2;
    public static final int INVALID_WORD_PLACEMENT = -3;
    public static final int DEFAULT_TRIPLE_WORD_SQUARES[][] = { { 0, 0 }, { 7, 0 }, { 14, 0 }, { 0, 7 }, { 0, 14 },
            { 7, 14 }, { 14, 7 }, { 14, 14 } };
    public static final int DEFAULT_TRIPLE_LETTER_SQUARES[][] = { { 5, 1 }, { 9, 1 }, { 1, 5 }, { 5, 5 }, { 9, 5 },
            { 13, 5 }, { 1, 9 }, { 5, 9 }, { 9, 9 }, { 13, 9 }, { 5, 13 }, { 9, 13 } };
    public static final int DEFAULT_DOUBLE_WORD_SQUARES[][] = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 4, 10 },
            { 3, 11 }, { 2, 12 }, { 1, 13 }, { 13, 1 }, { 12, 2 }, { 11, 3 }, { 10, 4 }, { 10, 10 }, { 11, 11 },
            { 12, 12 }, { 13, 13 } };
    public static final int DEFAULT_DOUBLE_LETTER_SQUARES[][] = { { 3, 0 }, { 11, 0 }, { 0, 3 }, { 6, 2 }, { 7, 3 },
            { 8, 2 }, { 14, 3 }, { 2, 6 }, { 6, 6 }, { 8, 6 }, { 12, 6 }, { 3, 7 }, { 11, 7 }, { 2, 8 }, { 6, 8 },
            { 8, 8 }, { 12, 8 }, { 0, 11 }, { 7, 11 }, { 14, 11 }, { 6, 12 }, { 8, 12 }, { 3, 14 }, { 6, 12 },
            { 11, 14 } };
    private static final String XMLEXTENSION = "xml"; // String constant for ser extension (compiler doesn't flag fault
    // strings!
    private static final String XMLDESCRIPTION = "*.xml"; // String constant for ser file description

    public enum Multipliers {
        TW(3), TL(3), DW(2), DL(2), N(1);

        public final static int NO_MULTIPLIER = 1;
        private int multiplier; // Field that holds a constant multiplier

        /**
         * @author Hasan Al-Hasoo
         *         Constructs enums passing in 1 parameter and assigning it to the field
         *         "multiplier"
         * @param multiplier multiplier passed into the constructed enum
         */
        Multipliers(int multiplier) {
            this.multiplier = multiplier;
        }

        /**
         * @author Hasan Al-Hasoo
         *         Getter method for multiplier field
         * @return an int representing the multipler
         */
        public int getMultiplier() {
            return this.multiplier;
        }

    }

    public enum ColumnName {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O
    }

    public enum Direction {
        HORIZONTAL, VERTICAL
    }

    private Square[][] grid; // 15x15 array representing the board grid
    private ScrabbleModel scrabbleModel;

    private ArrayList<Square> squaresFilledInCurrentTurn;

    private ArrayList<BoardViews> views;

    private int[][] tripleWordSqaures;
    private int[][] doubleWordSqaures;
    private int[][] tripleLetterSqaures;
    private int[][] doubleLetterSqaures;

    public final static int MATRIX_SIZE = 15;

    /**
     * Constructs the new Scrabble Board
     *
     * @author Tuna Uygun
     * @author Hasan Al-Hasoo
     */
    public BoardModel(ScrabbleModel scrabbleModel) {
        ;
        this.scrabbleModel = scrabbleModel;
        grid = new Square[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                grid[i][j] = new Square(i, j);
            }
        }
        tripleWordSqaures = DEFAULT_TRIPLE_WORD_SQUARES;
        doubleWordSqaures = DEFAULT_DOUBLE_WORD_SQUARES;
        tripleLetterSqaures = DEFAULT_TRIPLE_LETTER_SQUARES;
        doubleLetterSqaures = DEFAULT_DOUBLE_LETTER_SQUARES;

        this.squaresFilledInCurrentTurn = new ArrayList<>();
        this.views = new ArrayList<>();
    }

    /**
     * Creates a custom board reader and reads the xml file to set the
     * board multipliers
     *
     * @author Tuna Uygun
     */
    public void setCustomBoard() {
        CustomBoardReader reader = new CustomBoardReader();
        String fileName = ScrabbleFrame.getFileName(XMLDESCRIPTION, XMLEXTENSION, true);
        reader.readCustomBoardFile(fileName);
        reader.setMultiplier(this);
    }

    /**
     * placeTile places a tile from the player's rack and on the board
     *
     * @author Tuna Ngyun
     * @param row, int
     * @param col, int
     */
    public void placeTile(int row, int col) {
        ArrayList<Tile> selectedTiles = scrabbleModel.getActivePlayer().getSelectedTiles();
        if (selectedTiles.size() == 1 && !grid[row][col].hasTile()) {
            grid[row][col].placeTile(selectedTiles.get(0));
            squaresFilledInCurrentTurn.add(grid[row][col]);
            selectedTiles.get(0).setPlaced(true);
            selectedTiles.get(0).toggleSelect();
            scrabbleModel.getActivePlayer().updatePlayerViews();
            updateBoardViews();
        }
    }

    /**
     * Resets the game. Sets up all operations for a new round of Scrabble and a
     * winner will be once again undefined
     *
     * @author Tuna Uygun
     * @author Hasan Al-Hasoo
     */
    public void clearBoard() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                grid[i][j].removeTile();
            }
        }
    }

    /**
     * setSquareMultipliers method adds multipliers to the Square grid
     *
     * @author Tuna Uygun
     * @author Umniyah Mohammed
     */
    public void setSquareMultipliers() {
        grid[7][7].setMultiplier(Multipliers.DW);
        this.setSquareMultipliers(DEFAULT_TRIPLE_WORD_SQUARES, DEFAULT_DOUBLE_WORD_SQUARES,
                DEFAULT_TRIPLE_LETTER_SQUARES, DEFAULT_DOUBLE_LETTER_SQUARES);
    }

    /**
     * setSquareMultipliers method adds multipliers to the Square grid
     *
     * @param twMult, int[][] an integer 2d array of triple word multipliers
     * @param dwMult, int[][] an integer 2d array of double word multipliers
     * @param tlMult, int[][] an integer 2d array of triple letter multipliers
     * @param dlMult, int[][] an integer 2d array of double letter multipliers
     * @author Umniyah Mohammed
     */
    public void setSquareMultipliers(int[][] twMult, int[][] dwMult, int[][] tlMult, int[][] dlMult) { // writes to
        // board
        for (int[] coordinate : twMult) {
            grid[coordinate[0]][coordinate[1]].setMultiplier(Multipliers.TW);
        }
        for (int[] coordinate : tlMult) {
            grid[coordinate[0]][coordinate[1]].setMultiplier(Multipliers.TL);
        }
        for (int[] coordinate : dwMult) {
            grid[coordinate[0]][coordinate[1]].setMultiplier(Multipliers.DW);
        }
        for (int[] coordinate : dlMult) {
            grid[coordinate[0]][coordinate[1]].setMultiplier(Multipliers.DL);
        }
        tripleWordSqaures = twMult;
        doubleWordSqaures = dwMult;
        tripleLetterSqaures = tlMult;
        doubleLetterSqaures = dlMult;
    }

    /**
     * The string representation of the game board
     *
     * @return The string representation of the game board
     * @author Tuna Uygun
     */
    public String toString() {
        String boardString = "      A B C D E F G H I J K L M N O\n";
        for (int i = 0; i < MATRIX_SIZE; i++) {
            boardString += "\n" + (i >= 9 ? " " : "  ") + (i + 1) + "  ";
            for (Square square : grid[i]) {
                char squareLetter = (square.hasTile()) ? square.getLetter() : ' ';
                boardString += " " + squareLetter;
            }
        }
        return boardString;
    }

    /**
     * Places the word that is passed as a parameter to the board and
     * returns the score for the placed word. If there is an error, it returns a
     * specific negative integer depending on the error.
     *
     * @param isFirstRound True if it is the first round, false otherwise.
     * @return The score of the word placed to the board. Returns a negative number
     *         if an error occurred
     * @author Tuna Uygun
     */
    public int placeWord(boolean isFirstRound) {
        if (squaresFilledInCurrentTurn.size() <= 0) {
            returnPlacedTilesToPlayer();
            return BoardModel.INVALID_WORD_PLACEMENT;
        }
        Direction direction = extractDirection();
        if (direction == null) {
            returnPlacedTilesToPlayer();
            return BoardModel.INVALID_WORD_PLACEMENT;
        }

        int[] startingLocationCoordinates = this.extractStartCoordinates();
        int startingRow = startingLocationCoordinates[0];
        int startingCol = startingLocationCoordinates[1];

        int[] endingLocationCoordinates = this.extractEndCoordinates();
        int endingRow = endingLocationCoordinates[0];
        int endingCol = endingLocationCoordinates[1];

        Word word = getWordFromExtends(new int[][] { startingLocationCoordinates, endingLocationCoordinates });

        if (isFirstRound && !grid[7][7].hasTile()) {
            returnPlacedTilesToPlayer();
            return BoardModel.INVALID_FIRST_ROUND; // first word of the game needs to have a tile in the center
        }

        if (getNumberOfLettersInside(startingRow, startingCol, endingRow, endingCol) == word.getLength()
                && !isFirstRound) {
            returnPlacedTilesToPlayer();
            return BoardModel.INVALID_WORD_PLACEMENT; // placed word does not intersect another word
        }

        ArrayList<int[]> coordinatesOfPlacedTiles = new ArrayList<>();
        for (Square s : squaresFilledInCurrentTurn) {
            coordinatesOfPlacedTiles.add(new int[] { s.getRowNum(), s.getColumnNum() });
        }
        int score = 0;
        ArrayList<int[][]> wordExtents = getWordExtents(coordinatesOfPlacedTiles, direction);
        for (int[][] wordExtend : wordExtents) {
            Word formedWord = this.getWordFromExtends(wordExtend);
            if (formedWord.isValidWord()) {
                score += this.getScoreFromWordExtends(wordExtend);
            } else {
                returnPlacedTilesToPlayer();
                return BoardModel.INVALID_WORD; // Newly-formed word is not a valid English word.
            }
        }

        if (squaresFilledInCurrentTurn.size() == 7) {
            score += 50;
        }

        squaresFilledInCurrentTurn.clear();
        return score;
    }

    /**
     * @author Tuna Uygun
     *         returnPlacedTilesToPlayer returns the tiles placed on board to the
     *         player's rack
     */
    public void returnPlacedTilesToPlayer() {
        Player p = scrabbleModel.getActivePlayer();
        for (Square s : squaresFilledInCurrentTurn) {
            grid[s.getRowNum()][s.getColumnNum()].getTile().setPlaced(false);
            grid[s.getRowNum()][s.getColumnNum()].removeTile();
        }
        squaresFilledInCurrentTurn.clear();
        p.updatePlayerViews();
        this.updateBoardViews();
    }

    /**
     * Returns an arraylist of coordinates for start and end of all newly-formed
     * words
     *
     * @param coordinatesOfPlacedTiles The list of coordinates for each square where
     *                                 a tile is placed for the current word
     * @param wordDirection            The direction of the word placement
     *                                 (horizontal or vertical)
     * @return An arraylist of coordinates (2d array) for start and end of all
     *         newly-formed words
     * @author Tuna Uygun
     */
    private ArrayList<int[][]> getWordExtents(ArrayList<int[]> coordinatesOfPlacedTiles, Direction wordDirection) {
        HashSet<int[][]> wordExtents = new HashSet<>();
        boolean checkedWordDirection = false;

        for (int[] coordinate : coordinatesOfPlacedTiles) {
            int row = coordinate[0];
            int col = coordinate[1];

            if (wordDirection != Direction.HORIZONTAL || !checkedWordDirection) {
                while (col - 1 >= 0 && this.grid[row][col - 1].hasTile()) {
                    col--;
                }
                int[] horizontalStart = { row, col };

                while (col + 1 < MATRIX_SIZE && this.grid[row][col + 1].hasTile()) {
                    col++;
                }
                int[] horizontalEnd = { row, col };
                if (!Arrays.equals(horizontalStart, horizontalEnd)) {
                    wordExtents.add(new int[][] { horizontalStart, horizontalEnd });
                }
            }

            if (wordDirection != Direction.VERTICAL || !checkedWordDirection) {
                col = coordinate[1];
                while (row - 1 >= 0 && this.grid[row - 1][col].hasTile()) {
                    row--;
                }
                int[] verticalStart = { row, col };

                while (row + 1 < MATRIX_SIZE && this.grid[row + 1][col].hasTile()) {
                    row++;
                }
                int[] verticalEnd = { row, col };

                if (!Arrays.equals(verticalStart, verticalEnd)) {
                    wordExtents.add(new int[][] { verticalStart, verticalEnd });
                }
            }
            checkedWordDirection = true;
        }
        return new ArrayList<>(wordExtents);
    }

    /**
     * Returns the word object created from the end and start coordinates passed as
     * a parameter
     *
     * @param wordExtend An array of start and end coordinate of a word
     * @return The word object created from the end and start coordinates passed as
     *         a parameter
     * @author Tuna Uygun
     */
    private Word getWordFromExtends(int[][] wordExtend) {
        int[] wordStart = wordExtend[0];
        int[] wordEnd = wordExtend[1];

        String word = "";
        if (wordStart[0] == wordEnd[0]) { // the word is horizontal
            for (int i = wordStart[1]; i <= wordEnd[1]; i++) {
                word += this.grid[wordStart[0]][i].getLetter();
            }
        } else { // the word is vertical
            for (int i = wordStart[0]; i <= wordEnd[0]; i++) {
                word += this.grid[i][wordStart[1]].getLetter();
            }
        }
        return new Word(word);
    }

    /**
     * Returns he score of the word that starts and ends in the coordinates passed
     * on the wordExtend parameter
     *
     * @param wordExtend An array of start and end coordinate of a word
     * @return The score of the word that starts and ends in the coordinates passed
     *         on the wordExtend parameter
     * @author Tuna Uygun
     */
    private int getScoreFromWordExtends(int[][] wordExtend) {
        int[] wordStart = wordExtend[0];
        int[] wordEnd = wordExtend[1];

        int score = 0;
        int multiplier = 1;
        if (wordStart[0] == wordEnd[0]) { // the word is horizontal
            for (int i = wordStart[1]; i <= wordEnd[1]; i++) {
                score += this.grid[wordStart[0]][i].getScoreValue();
                multiplier *= this.grid[wordStart[0]][i].getWordMultiplier();
            }
        } else { // the word is vertical
            for (int i = wordStart[0]; i <= wordEnd[0]; i++) {
                score += this.grid[i][wordStart[1]].getScoreValue();
                multiplier *= this.grid[i][wordStart[1]].getWordMultiplier();
            }
        }
        return multiplier * score;
    }

    /**
     * Returns the tile with the given character from the ArrayList of tiles.
     *
     * @param list      ArrayList of tiles to search.
     * @param character A character to be returned form the tile list.
     * @return The tile with the given character from the ArrayList of tiles.
     * @author Tuna Uygun
     */
    private static Tile getTileFromTileList(ArrayList<Tile> list, char character) {
        for (Tile tile : list) {
            if (tile.getChar() == character) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Removes the tiles for the squares in the locations specified in the
     * tileCoordinates ArrayList
     *
     * @param tileCoordinates The Arraylist of coordinates of locations on the board
     *                        to remove the tiles
     * @author Tuna Uygun
     */
    private void removeTilesFromCoordinates(ArrayList<int[]> tileCoordinates) {
        for (int[] coordinate : tileCoordinates) {
            this.grid[coordinate[0]][coordinate[1]].removeTile();
        }
    }

    /**
     * Returns the number of letters within the area enclosed by the starting and
     * ending row and column
     *
     * @param startRow The index of the starting row for the boundary
     * @param startCol The index of the starting column for the boundary
     * @param endRow   The index of the ending row for the boundary
     * @param endCol   The index of the ending column for the boundary
     * @return The number of letters within the area enclosed by the parameters
     * @author Tuna Uygun
     */
    private int getNumberOfLettersInside(int startRow, int startCol, int endRow, int endCol) {
        int numOfLetters = 0;
        startRow = startRow - 1 < 0 ? 0 : startRow - 1;
        endRow = endRow + 1 > MATRIX_SIZE - 1 ? MATRIX_SIZE - 1 : endRow + 1;
        startCol = startCol - 1 < 0 ? 0 : startCol - 1;
        endCol = endCol + 1 > MATRIX_SIZE - 1 ? MATRIX_SIZE - 1 : endCol + 1;
        for (int i = startRow; i < endRow + 1; i++) {
            for (int j = startCol; j < endCol + 1; j++) {
                if (this.grid[i][j].hasTile())
                    numOfLetters++;
            }
        }
        return numOfLetters;
    }

    /**
     * Handles the starting position, and returns an array that contains x and y
     * coordinate of the starting position.
     * If the starting position is not valid, returns -1 for both x and y.
     *
     * @return The array that contains x and y coordinate of the starting position.
     *         Returns -1 if the starting location is not valid.
     * @author Tuna Uygun
     */
    private int[] extractStartCoordinates() {
        int minRow = squaresFilledInCurrentTurn.get(0).getRowNum();
        int minCol = squaresFilledInCurrentTurn.get(0).getColumnNum();

        for (int i = 1; i < squaresFilledInCurrentTurn.size(); i++) {
            if (squaresFilledInCurrentTurn.get(i).getRowNum() < minRow) {
                minRow = squaresFilledInCurrentTurn.get(i).getRowNum();
            }
            if (squaresFilledInCurrentTurn.get(i).getColumnNum() < minCol) {
                minCol = squaresFilledInCurrentTurn.get(i).getColumnNum();
            }
        }
        return new int[] { minRow, minCol };
    }

    /**
     * Handles the ending position, and returns an array that contains x and y
     * coordinate of the ending position.
     * If the ending position is not valid, returns -1 for both x and y.
     *
     * @return The array that contains x and y coordinate of the ending position.
     *         Returns -1 if the ending location is not valid.
     * @author Tuna Uygun
     */
    private int[] extractEndCoordinates() {
        int maxRow = squaresFilledInCurrentTurn.get(0).getRowNum();
        int maxCol = squaresFilledInCurrentTurn.get(0).getColumnNum();

        for (int i = 1; i < squaresFilledInCurrentTurn.size(); i++) {
            if (squaresFilledInCurrentTurn.get(i).getRowNum() > maxRow) {
                maxRow = squaresFilledInCurrentTurn.get(i).getRowNum();
            }
            if (squaresFilledInCurrentTurn.get(i).getColumnNum() > maxCol) {
                maxCol = squaresFilledInCurrentTurn.get(i).getColumnNum();
            }
        }
        return new int[] { maxRow, maxCol };
    }

    /**
     * Returns a Direction enum based on the placement of the tiles
     *
     * @return enum, Direction
     * @author Tuna Uygun
     */
    private Direction extractDirection() {
        boolean isVertical = true;
        boolean isHorizontal = true;

        int row = squaresFilledInCurrentTurn.get(0).getRowNum();
        int col = squaresFilledInCurrentTurn.get(0).getColumnNum();

        for (int i = 1; i < squaresFilledInCurrentTurn.size(); i++) {
            if (squaresFilledInCurrentTurn.get(i).getRowNum() != row) {
                isHorizontal = false;
            }
            if (squaresFilledInCurrentTurn.get(i).getColumnNum() != col) {
                isVertical = false;
            }
        }

        if (squaresFilledInCurrentTurn.size() == 1 || (isVertical && !isHorizontal)) {
            return Direction.VERTICAL;
        } else if (isHorizontal && !isVertical) {
            return Direction.HORIZONTAL;
        }
        return null;
    }

    /**
     * updateBoardViews updates the views of the board
     *
     * @author Tuna Uygun
     */
    public void updateBoardViews() {
        for (BoardViews v : views) {
            v.handleBoardUpdate(grid);
        }
    }

    /**
     * addBoardView adds a board view
     *
     * @author Tuna Uygun
     */
    public void addBoardView(BoardViews v) {
        views.add(v);
    }

    /**
     * @author Hasan Al-Hasoo
     *         Method used to place tiles on the board with custom coordinates,
     *         scores and chars.
     *         Used for testing
     * @param row   coordinate in matrix
     * @param col   coordinate in matrix
     * @param c     char used to compose tile locally
     * @param score corresponding score will be used to construct the tile
     */

    public void placeOnBoard(int row, int col, char c, int score) {
        Tile tile = new Tile(score, c);
        grid[row][col].placeTile(tile);
        squaresFilledInCurrentTurn.add(grid[row][col]);
    }

    public void resetSquaresFilledInCurrentTurnList() {
        squaresFilledInCurrentTurn.clear();
    }

    /**
     * nonEmptySquares is an accessor. It contains the squares that have tiles
     *
     * @return emptySquares, ArrayList<Square>
     */
    public ArrayList<Square> nonEmptySquares() {
        ArrayList<Square> nonEmptySquares = new ArrayList<>();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (grid[i][j].hasTile()) {
                    nonEmptySquares.add(grid[i][j]);
                }
            }
        }
        return nonEmptySquares;
    }

    /**
     * Returns the array of integer arrays containing the row and column of triple
     * word squares
     *
     * @return Array of integer arrays containing the row and column of triple word
     *         squares
     */
    public int[][] getTripleWordSqaures() {
        return tripleWordSqaures;
    }

    /**
     * Returns the array of integer arrays containing the row and column of double
     * word squares
     *
     * @return Array of integer arrays containing the row and column of double word
     *         squares
     */
    public int[][] getDoubleWordSqaures() {
        return doubleWordSqaures;
    }

    /**
     * Returns the array of integer arrays containing the row and column of triple
     * letter squares
     *
     * @return Array of integer arrays containing the row and column of triple
     *         letter squares
     */
    public int[][] getTripleLetterSqaures() {
        return tripleLetterSqaures;
    }

    /**
     * Returns the array of integer arrays containing the row and column of double
     * letter squares
     *
     * @return Array of integer arrays containing the row and column of double
     *         letter squares
     */
    public int[][] getDoubleLetterSqaures() {
        return doubleLetterSqaures;
    }

    /**
     * @author Hasan Al-Hasoo
     *         Retrieves the coordinates of the square passed in and counts the
     *         number of empty spaces to the left
     * @param square target square
     * @return number of empty spaces to the left
     */
    public int countEmptySquaresLeft(Square square) {
        if (square.getColumnNum() + 1 < MATRIX_SIZE && grid[square.getRowNum()][square.getColumnNum() + 1].hasTile()) {
            return 0;
        }

        int counter = 0;
        int i = square.getRowNum();

        for (int j = square.getColumnNum() - 1; j >= 0; j--) {
            if (!grid[i][j].hasTile()) {
                counter++;
            } else {
                return counter == 0 ? counter : counter - 1;
            }
        }
        return counter;
    }

    /**
     * @author Hasan Al-Hasoo
     *         Retrieves the coordinates of the square passed in and counts the
     *         number of empty spaces to the right
     * @param square target square
     * @return number of empty spaces to the right
     */
    public int countEmptySquaresRight(Square square) {
        if (square.getColumnNum() - 1 >= 0 && grid[square.getRowNum()][square.getColumnNum() - 1].hasTile()) {
            return 0;
        }

        int counter = 0;
        int i = square.getRowNum();

        for (int j = square.getColumnNum() + 1; j < MATRIX_SIZE; j++) {
            if (!grid[i][j].hasTile()) {
                counter++;
            } else {
                return counter == 0 ? counter : counter - 1;
            }
        }
        return counter;
    }

    /**
     * @author Hasan Al-Hasoo
     *         Retrieves the coordinates of the square passed in and counts the
     *         number of empty spaces downwards
     * @param square target square
     * @return number of empty spaces downwards
     */
    public int countEmptySquaresDown(Square square) {
        if (square.getRowNum() - 1 >= 0 && grid[square.getRowNum() - 1][square.getColumnNum()].hasTile()) {
            return 0;
        }

        int counter = 0;
        int j = square.getColumnNum();

        for (int i = square.getRowNum() + 1; i < MATRIX_SIZE; i++) {
            if (!grid[i][j].hasTile()) {
                counter++;
            } else {
                return counter == 0 ? counter : counter - 1;
            }
        }
        return counter;
    }

    /**
     * @author Hasan Al-Hasoo
     *         Retrieves the coordinates of the square passed in and counts the
     *         number of empty spaces upwards
     * @param square target square
     * @return number of empty spaces downwards
     */
    public int countEmptySquaresUp(Square square) {
        if (square.getRowNum() + 1 < MATRIX_SIZE && grid[square.getRowNum() + 1][square.getColumnNum()].hasTile()) {
            return 0;
        }

        int counter = 0;
        int j = square.getColumnNum();

        for (int i = square.getRowNum() - 1; i >= 0; i--) {
            if (!grid[i][j].hasTile()) {
                counter++;
            } else {
                return counter == 0 ? counter : counter - 1;
            }
        }
        return counter;
    }

    /**
     * @author Hasan Al-Hasoo
     *         Convert BoardModel into a matrix of strings. Method will be passed to
     *         ScrabbleState class as a string rep of
     *         the current game
     * @return a matrix of strings
     */
    public String[][] boardString() {
        String[][] boardStringList;

        boardStringList = new String[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                boardStringList[i][j] = grid[i][j].getTile().getChar() + "," + grid[i][j].getTile().getScore() + ","
                        + grid[i][j].getMultiplier();
            }
        }
        return boardStringList;
    }

    /**
     * getViews is an accessor
     *
     * @return views, ArrayList<BoardViews>
     * @author Tuna Uygun
     */
    public ArrayList<BoardViews> getViews() {
        return views;
    }

    /**
     * setViews sets the views
     *
     * @param views, ArrayList<BoardViews>
     * @author Tuna Uygun
     */
    public void setViews(ArrayList<BoardViews> views) {
        this.views = views;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
