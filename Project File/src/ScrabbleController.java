import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * An abstract controller for the Scrabble game
 * @author Tuna Uygun
 * @version 1.0
 */
public abstract class ScrabbleController implements ActionListener, Serializable {

    public ScrabbleModel model;

    /**
     * Constructor for ScrabbleController class
     * @param model, ScrabbleModel
     * @author Tuna Uygun
     */
    public ScrabbleController(ScrabbleModel model) {
        this.model = model;
    }


}
