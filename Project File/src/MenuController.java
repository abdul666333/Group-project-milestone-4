import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A controller for the menu bar
 *
 * @author Hasan Al-Hasoo
 * @author Tuna Uygun
 * @author Umniyah Mohammed
 * @version 1.0
 */
public class MenuController extends ScrabbleController {

    private static final String SAVE = "save"; // String constant for "save"
    private static final String LOAD = "load"; // String constant for "load"
    private static final String UNDO = "undo"; // String constant for "undo"
    private static final String REDO = "redo"; // String constant for "redo"
    private static final String SEREXTENSION = "ser"; // String constant for ser extension (compiler doesn't flag fault
    // strings!
    private static final String SERDESCRIPTION = "*.ser"; // String constant for ser file description

    /**
     * MenuController creates a menuController object.
     *
     * @param model Scrabble, the model to control.
     * @author Hasan Al-Hasoo
     */
    public MenuController(ScrabbleModel model) {
        super(model);
    }

    /**
     * actionPerformed saves or loads the game
     *
     * @param e ActionEvent
     * @author Tuna Uygun
     * @author Umniyah Mohammed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals((SAVE))) {
            String fileName = ScrabbleFrame.getFileName(SERDESCRIPTION, SEREXTENSION, false);
            model.exportScrabble(fileName);
        }
        if (e.getActionCommand().equals(LOAD)) {
            String fileName = ScrabbleFrame.getFileName(SERDESCRIPTION, SEREXTENSION, true);
            ScrabbleModel updatedModel = model.importScrabble(fileName);
            model.updateScrabbleModel(new ScrabbleEvent(updatedModel));
        }
        if (e.getActionCommand().equals(UNDO)) {
            ScrabbleModel updatedModel;
            try {
                updatedModel = model.undo();
                model.updateScrabbleModel(new ScrabbleEvent(updatedModel));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        if (e.getActionCommand().equals(REDO)) {
            ScrabbleModel updatedModel;
            try {
                updatedModel = model.redo();
                model.updateScrabbleModel(new ScrabbleEvent(updatedModel));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

    }
}

