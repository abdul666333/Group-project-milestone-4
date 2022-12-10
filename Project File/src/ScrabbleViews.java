/**
 * Scrabble view is used to subscribe to the Scrabble model
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public interface ScrabbleViews {
    /**
     * handleScrabbleUpdate handles the game updates
     * @param e, ScrabbleEvent
     * @author Umniyah Mohammed
     */
    void handleScrabbleUpdate(ScrabbleEvent e);

    /**
     * handleEndGameResults handles the results at the end of the game
     * @param s, String
     * @author Umniyah Mohammed
     */
    void handleEndGameResults(String s);

    /**
     * handleWarningMessage handles warning messages such as errors and help dialog
     * @param s, String
     * @author Umniyah Mohammed
     */
    void handleWarningMessage(String s);

    /**
     * updateScrabbleModel updates the scrabble model
     * @param event, ScrabbleEvent
     * @author Umniyah Mohammed
     */
    void updateScrabbleModel(ScrabbleEvent event);
}
