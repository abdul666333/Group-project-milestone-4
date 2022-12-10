import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel implements BoardViews {

    private static final Color CENTER_COLOR = new Color(224, 240, 255);
    private static final Color TW_COLOR = new Color(253, 200, 196);
    private static final Color DW_COLOR = new Color(233, 245, 211);
    private static final Color TL_COLOR = new Color(224, 218, 243);
    private static final Color DL_COLOR = new Color(255, 233, 217);

    private JButton[][] buttonGrid;
    private BoardModel model;

    /**
     * Constructs the board panel
     * @author Tuna Uygun
     * @param model, BoardModel
     */
    public BoardPanel(BoardModel model){
        super();
        this.model = model;
        buttonGrid = new JButton[15][15];
        this.setLayout(new GridLayout(15, 15));
        BoardPaneController controller = new BoardPaneController(model);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton b = new JButton();
                b.setActionCommand(i + " " + j);
                b.addActionListener(controller);
                buttonGrid[i][j] = b;
                this.add(b);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                buttonGrid[i][j].setBackground(Color.WHITE);
            }
        }

        for (int[] DLCoordinates: model.getDoubleLetterSqaures()) {
            buttonGrid[DLCoordinates[0]][DLCoordinates[1]].setBackground(DL_COLOR);
        }

        for (int[] TLCoordinates: model.getTripleLetterSqaures()) {
            buttonGrid[TLCoordinates[0]][TLCoordinates[1]].setBackground(TL_COLOR);
        }

        for (int[] DWCoordinates: model.getDoubleWordSqaures()) {
            buttonGrid[DWCoordinates[0]][DWCoordinates[1]].setBackground(DW_COLOR);
        }

        for (int[] TWCoordinates: model.getTripleWordSqaures()) {
            buttonGrid[TWCoordinates[0]][TWCoordinates[1]].setBackground(TW_COLOR);
        }

        buttonGrid[7][7].setBackground(CENTER_COLOR);
    }

    /**
     * handleBoardUpdate handles the board updates
     * @author Tuna Uygun
     * @param boardGrid, Square[][]
     */
    @Override
    public void handleBoardUpdate(Square[][] boardGrid) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                String letter = boardGrid[i][j].hasTile()? String.valueOf(boardGrid[i][j].getTile().getChar()) : " ";
                buttonGrid[i][j].setText(letter);
            }
        }
    }
}