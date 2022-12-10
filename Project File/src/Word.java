import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Word models a word formed by a player
 * @author Tuna Uygun
 * @version 10/22/22
 */
public class Word {
    private static final ArrayList<String> validWords = loadValidWords();
    private String word;
    private List<Character> playerLetters;
    private List<Character> boardLetters;

    /**
     * Creates a word
     * @param word The word that has parenthesis around the letters that are on the board
     */
    public Word(String word){
        this.boardLetters = extractBoardLetters(word);
        this.playerLetters = extractPlayerLetters(word, boardLetters);
        this.word = extractWord(word, boardLetters);
    }

    /**
     * Reads the dictionary file and returns the List of Strings from
     * the dictionary of valid English words
     * @return List of Strings from the dictionary of valid English words
     */
    public static ArrayList<String> loadValidWords(){
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Word.class.getClassLoader().getResourceAsStream("wordlist.txt"),
                        StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                words.add(word);
            }
        } catch (IOException e) {
            System.out.println("Dictionary of acceptable words file cannot be read!");
            e.printStackTrace();
        }


        return words;
    }

    /**
     * Returns an arrayList of all valid words
     * @return An arrayList of all valid words
     */
    public static ArrayList<String> getValidWords(){
        return Word.validWords;
    }

    /**
     * Returns a list of letters of the word that are already on the board
     * @param word The word that has parenthesis around the letters that are on the board
     * @return A list of letters of the word that are already on the board
     */
    private ArrayList<Character> extractBoardLetters(String word) {
        ArrayList<Character> letters = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\(\\w\\)");
        Matcher matcher = pattern.matcher(word);
        while(matcher.find()){
            char boardLetter = matcher.group().charAt(1);
            letters.add(boardLetter);
        }
        return letters;
    }

    /**
     * Return a list of the letters of the word that comes from the user's rack
     * @param word The word that has parenthesis around the letters that are on the board
     * @param boardLetters The letters of the word that are already in the board
     * @return A list of the letters of the word that comes from the user's rack
     */
    private ArrayList<Character> extractPlayerLetters(String word, List<Character> boardLetters) {
        ArrayList<Character> letters = new ArrayList<>();
        for(char c: boardLetters){
            word = word.replace("("+c+")", "");
        }
        for(char c: word.toCharArray()){
            letters.add(c);
        }
        return letters;
    }

    /**
     * Return the word without the parenthesis which surrounds letters of the word
     * that already exists in the board
     *
     * @param word The word that has parenthesis around the letters that are on the board
     * @param boardLetters The letters of the word that are already in the board
     * @return The word without the parenthesis which surrounds letters of the word
     *         that already exists in the board
     */
    private String extractWord(String word, List<Character> boardLetters) {
        for(char c: new HashSet<Character>(boardLetters)){
            word = word.replaceAll("\\("+c+"\\)", ""+c);
        }
        return word;
    }

    /**
     * getWord is an accessor
     * @return word, String
     *
     */
    public String getWord() {
        return word;
    }

    /**
     * getPlayerLetters is an accessor
     * @return playerLetters, ArrayList<Character>
     */
    public ArrayList<Character> getPlayerLetters() {
        return (ArrayList<Character>) playerLetters;
    }

    /**
     * getBoardLetters is an accessor
     * @return boardLetters, List<Character>
     */
    public List<Character> getBoardLetters() {
        return boardLetters;
    }

    /**
     * isValidWord returns true if the word is valid
     * @return boolean
     */
    public Boolean isValidWord(){
        return validWords.contains(this.word.toLowerCase());
    }

    /**
     * getLength returns the length of the word
     * @return int
     */
    public int getLength(){
        return this.word.length();
    }
}
