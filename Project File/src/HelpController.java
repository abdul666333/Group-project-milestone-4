import java.awt.event.ActionEvent;

/**
 * A controller for the help command
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public class HelpController extends ScrabbleController {
    /**
     * HelpController creates a HelpController object
     * @param model Scrabble, the model to control
     */
    public HelpController(ScrabbleModel model) {
        super(model);
    }

    /**
     * actionPerformed displays all commands and how they work
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.help();
    }
}
