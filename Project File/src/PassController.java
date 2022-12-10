import java.awt.event.ActionEvent;
/**
 * A controller for the pass command
 * @author Tuna Uygun
 * @version 1.0
 */

public class PassController extends ScrabbleController {
    /**
     * PassController creates a PassController object.
     * @param model Scrabble, the model to control.
     */
    public PassController(ScrabbleModel model) {
        super(model);
    }

    /**
     * actionPerformed quits the game
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.pass();
    }
}
