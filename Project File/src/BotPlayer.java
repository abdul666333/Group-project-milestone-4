import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Player AI Class
 * @author Umniyah Mohammed
 * @version 1.0
 */
public class BotPlayer extends Player {

    private BoardModel boardModel;

    /**
     * Constructor of Player class that takes a player's name
     * @param name
     * @param tilePile
     */
    public BotPlayer(String name, TilePile tilePile, BoardModel boardModel) {
        super(name, tilePile);
        this.boardModel = boardModel;
    }

    /**
     * Searches the word dictionary and attempts to place a valid word on the board.
     * If a valid word is placed on board, returns true, otherwise returns false.
     * @return True if a valid word is placed, false otherwise.
     */
    public Boolean botPlay() {
        Random r = new Random();
        ArrayList<Square> nonEmptySquares;
        nonEmptySquares = boardModel.nonEmptySquares();
        for (Square square : nonEmptySquares) {
            int emptySquaresLeft = boardModel.countEmptySquaresLeft(square);
            int emptySquaresRight = boardModel.countEmptySquaresRight(square);
            int emptySquaresUp = boardModel.countEmptySquaresUp(square);
            int emptySquaresDown = boardModel.countEmptySquaresDown(square);

            if (isPlacedHorizontally(r, square, emptySquaresLeft, emptySquaresRight)) return true;
            if (isPlacedVertically(r, square, emptySquaresUp, emptySquaresDown)) return true;
        }
        return false;
    }


    /**
     * Searches the word dictionary and attempts to place a vertical word around the given square.
     * Then, returns true if a valid word is placed successfully, false otherwise.
     * @param r A random object
     * @param square The starting square which has a tile placed on it
     * @param emptySquaresUp The number of empty squares to the up
     * @param emptySquaresDown The number of empty squares to the down
     * @return True if the word is placed horizontally, false otherwise.
     */
    private boolean isPlacedVertically(Random r, Square square, int emptySquaresUp, int emptySquaresDown) {
        ArrayList<String> verticalWords = getPossibleWords(square.getLetter(), emptySquaresUp, emptySquaresDown);
        verticalWords = uniqueLetterPlacement(verticalWords, square.getLetter());
        if(verticalWords.size()>0){
            int randomNumber =  r.nextInt(verticalWords.size());
            String word = verticalWords.get(randomNumber);
            String[] startAndEndStrings = getStartAndEndPart(word, square.getLetter(), emptySquaresUp, emptySquaresDown);
            String startString = startAndEndStrings[0];
            for (int row = square.getRowNum()-startString.length(), i = 0; row < square.getRowNum(); row++, i++) {
                boardModel.placeOnBoard(row, square.getColumnNum(), startString.charAt(i), TilePile.Letter.valueOf(String.valueOf(startString.charAt(i))).score());
            }
            String endString = startAndEndStrings[1];
            for (int row = square.getRowNum()+1, i = 0; row <= square.getRowNum()+ endString.length(); row++, i++) {
                boardModel.placeOnBoard(row, square.getColumnNum(), endString.charAt(i), TilePile.Letter.valueOf(String.valueOf(endString.charAt(i))).score());
            }
            int wordScore = boardModel.placeWord(false);
            if(wordScore>=0){
                this.setScore(wordScore);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches the word dictionary and attempts to place a horizontal word around the given square.
     * Then, returns true if a valid word is placed successfully, false otherwise.
     * @param r A random object
     * @param square The starting square which has a tile placed on it
     * @param emptySquaresLeft The number of empty squares to the left
     * @param emptySquaresRight The number of empty squares to the right
     * @return True if the word is placed vertically, false otherwise.
     */
    private boolean isPlacedHorizontally(Random r, Square square, int emptySquaresLeft, int emptySquaresRight) {
        ArrayList<String> horizontalWords = getPossibleWords(square.getLetter(), emptySquaresLeft, emptySquaresRight);
        horizontalWords = uniqueLetterPlacement(horizontalWords, square.getLetter());
        if(horizontalWords.size()>0){
            int randomNumber =  r.nextInt(horizontalWords.size());
            String word = horizontalWords.get(randomNumber);
            String[] startAndEndStrings = getStartAndEndPart(word, square.getLetter(), emptySquaresLeft, emptySquaresRight);
            String startString = startAndEndStrings[0];
            for (int col = square.getColumnNum()- startString.length(), i = 0; col < square.getColumnNum(); col++, i++) {
                boardModel.placeOnBoard(square.getRowNum(), col, startString.charAt(i), TilePile.Letter.valueOf(String.valueOf(startString.charAt(i))).score());
            }
            String endString = startAndEndStrings[1];
            for (int col = square.getColumnNum()+1, i = 0; col <= square.getColumnNum()+ endString.length(); col++, i++) {
                boardModel.placeOnBoard(square.getRowNum(), col, endString.charAt(i), TilePile.Letter.valueOf(String.valueOf(endString.charAt(i))).score());
            }
            int wordScore = boardModel.placeWord(false);
            if(wordScore>=0){
                this.setScore(wordScore);
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if a word can be created from the Player's rack
     * @param words ArrayList<word>
     * @param ch the character at the reference square
     * @return An ArrayList of valid words
     * @author Abdal Alkawasmeh
     */
    private ArrayList<String> uniqueLetterPlacement(ArrayList<String> words, char ch) {
        ArrayList<String> validWords = new ArrayList<>();
        for (String w : words) {
            ArrayList<String> wordLetters = new ArrayList<String>();
            String[] wordLettersList = w.split("");
            Collections.addAll(wordLetters, wordLettersList);
            wordLetters.remove(String.valueOf(ch).toLowerCase());

            ArrayList<String> rackLetters = new ArrayList<String>();
            String[] rackLettersList = getRackString().split("");
            Collections.addAll(rackLetters, rackLettersList);

            Boolean isValid = true;
            for (String letter : wordLetters) {
                int occurrenceInWord = countOccurrences(wordLetters, letter);
                int occurrenceInRack = countOccurrences(rackLetters, letter);
                if (occurrenceInRack < occurrenceInWord) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                validWords.add(w);
            }
        }
        return validWords;
    }


    /**
     * Searches the valid words dictionary based on the player rack and available spaces and
     * returns an arraylist of string words that can possibly form a valid word placement
     * @return A list of possible words that can possibly be a valid
     * @author Tuna Uygun
     */
    private ArrayList<String> getPossibleWords(char character, int numberOfSpacesAtStart, int numberOfSpacesAtEnd){
        String rackString = getRackString().toLowerCase();
        character = Character.toLowerCase(character);

        String regex1 = "[" + rackString + "]{0," + numberOfSpacesAtStart + "}" + character;
        String regex2 = character + "[" + rackString + "]{0," + numberOfSpacesAtEnd + "}";
        String regex3 = "[" + rackString + "]{0," + numberOfSpacesAtStart + "}" + character + "[" + rackString + "]{0," + numberOfSpacesAtEnd + "}";

        HashSet<String> possibleWords = new HashSet<>();
        for (String w: Word.getValidWords()) {
            if(w.matches(regex1) || w.matches(regex2) || w.matches(regex3)){
                possibleWords.add(w);
            }
        }
        return new ArrayList<>(possibleWords);
    }

    /**
     * Returns the start and end part of the words based on the reference character on the board
     * @return The start and end part of the words based on the reference character on the board
     * @author Tuna Uygun
     */
    private String[] getStartAndEndPart(String word, char c, int numberOfSpacesAtStart, int numberOfSpacesAtEnd){
        word = word.toUpperCase();
        c = Character.toUpperCase(c);
        int count = 0;
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            if (letter.equals(String.valueOf(c))) {
                count++;
                index.add(i);
            }
        }

        if(count == 1){
            return new String[]{word.substring(0, index.get(0)), word.substring(index.get(0)+1, word.length())};
        } else{
            for(int i: index){
                String startPart = word.substring(0, index.get(0));
                String endPart = word.substring(index.get(0)+1, word.length());
                if(startPart.length() <= numberOfSpacesAtStart && endPart.length() <= numberOfSpacesAtEnd){
                    return new String[]{startPart, endPart};
                }
            }
        }
        return new String[]{"", ""};
    }

    /**
     * Takes a List of strings and another string s and checks if s is in the arrayList. If so, increment the counter
     * otherwise counter variable remains unmodified
     * @param strList An ArrayList of strings of size 1
     * @param s a string of size 1
     * @return The number of times s occurs in the arrayList of strings strList
     */
    private int countOccurrences (ArrayList<String> strList, String s){

        ArrayList<String> strListCopy;

        int numOccurrences = 0;

        strListCopy = (ArrayList<String>)strList.clone();

        int charListSize = strListCopy.size();

        for (int i = 0; i < charListSize; i++) {
            if (strListCopy.get(i).equalsIgnoreCase(s)) {
                numOccurrences++;
            }
        }

        return numOccurrences;
    }

    /**
     * Returns True if the player is a bot player, false otherwise.
     * @return True if the player is a bot player, false otherwise.
     * @author Tuna Uygun
     */
    @Override
    public boolean isBotPlayer() {
        return true;
    }
}