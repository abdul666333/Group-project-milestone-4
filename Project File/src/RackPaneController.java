import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A controller for the rack panel
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public class RackPaneController implements ActionListener {
    private Player model;

    /**
     * RackPaneController creates a RackPaneController object
     * @param model Scrabble, the model to control
     */
    public RackPaneController(Player model) {
        this.model = model;
    }

    /**
     * actionPerformed
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
