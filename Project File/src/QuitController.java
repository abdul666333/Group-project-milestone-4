import java.awt.event.ActionEvent;

/**
 * A controller for the help command
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public class QuitController extends ScrabbleController {
    /**
     * QuitController creates a QuitController object.
     * @param model Scrabble, the model to control.
     */
    public QuitController(ScrabbleModel model) {
        super(model);
    }

    /**
     * actionPerformed quits the game
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.quit();
    }
}
