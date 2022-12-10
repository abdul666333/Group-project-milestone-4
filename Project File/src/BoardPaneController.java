import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A controller for the board panel
 * @author Umniyah Mohammed 101158792
 * @version 1.0
 */
public class BoardPaneController implements ActionListener {
    private BoardModel model;

    /**
     * BoardPaneController creates a BoardPaneController object
     * @param model Scrabble, the model to control
     */
    public BoardPaneController(BoardModel model) {
        this.model = model;
    }

    /**
     * actionPerformed
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] input = e.getActionCommand().split(" ");
        int x = Integer.parseInt(input[0]);
        int y = Integer.parseInt(input[1]);
        model.placeTile(x, y);
    }
}
