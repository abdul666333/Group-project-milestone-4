import java.awt.event.ActionEvent;
import java.util.concurrent.CompletionService;

/**
 * A controller for the submit command
 *
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public class SubmitController extends ScrabbleController {

    /**
     * SubmitController creates a SubmitController object
     *
     * @param model Scrabble, the model to control
     */
    public SubmitController(ScrabbleModel model) {
        super(model);
    }

    /**
     * actionPerformed submits changes
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            model.saveState();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        model.place(false);

    }
}
