import java.awt.event.ActionEvent;

/**
 * @author Hasan Al-Hasoo
 * Controller Specifically designed to listen for the swap() invocation
 */
public class SwapController extends ScrabbleController {

    /**
     * @author Hasan Al-Hasoo
     * Constructs a SwapController object
     * @param model represents the Scrabble model passed
     */
    public SwapController(ScrabbleModel model) {
        super(model);
    }

    /**
     * @author Hasan Al-Hasoo
     * Listens for the swap() invocation event
     * @param e The event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.swap();
    }
}
