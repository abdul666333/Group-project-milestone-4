import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Hasan Al-Hasoo, Tuna Uygun
 * @version 1.0
 *
 * Class handles all operations concerning the GUI of the rack
 */

public class RackPanel extends JPanel implements ScrabbleViews, PlayerViews {
    private static final int SIZE = 7;

    /**
     * @author Hasan Al-Hasoo, Tuna Uygun
     * RackPanel class creates a panel for the players' rack
     * @param model The current player model
     */
    public RackPanel(Player model) {
        super();
        this.setLayout(new GridLayout(1, SIZE));
        this.updateRackButtons(model.getNonPlacedTiles());
    }

    /**
     * @author Hasan Al-Hasoo, Tuna Uygun
     * Updates the rack dependent on the current players rack.
     * @param e The event containing the active players' rack
     */
    @Override
    public void handleScrabbleUpdate(ScrabbleEvent e) {
        Player p = (Player) e.getActivePlayer();
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.updateRackButtons(p.getNonPlacedTiles());
    }

    /**
     * @author Tuna and Umniyah
     * handleEndGameResults handles the results of the game
     * @param s, String
     */
    @Override
    public void handleEndGameResults(String s) {

    }

    /**
     * @author Tuna and Umniyah
     * handlePlacementErrors handles placement errors
     * @param s, String
     */
    @Override
    public void handleWarningMessage(String s){}

    @Override
    public void updateScrabbleModel(ScrabbleEvent event) {
//        ScrabbleEvent event = new ScrabbleEvent(model);
        handleScrabbleUpdate(event);
    }

    /**
     * @Tuna Uygun
     * handlePlayerRackUpdate handles the update of the player rack
     * @param tiles, ArrayList<Tile>
     */
    @Override
    public void handlePlayerRackUpdate(ArrayList<Tile> tiles) {
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.updateRackButtons(tiles);
    }

    /**
     * @author Tuna Uygun and Hasan Al-Hasoo
     * updateRackButtons updates the rack buttons
     * @param tiles, ArrayList<Tile>
     */
    private void updateRackButtons(ArrayList<Tile> tiles){
        for (Tile t: tiles) {
            JButton b = new JButton(String.valueOf(t.getChar()));
            b.setBackground(Color.WHITE);
            b.addActionListener(e -> {
                t.toggleSelect();
                if(t.isSelected()){
                    b.setBackground(Color.GRAY);
                }else{
                    b.setBackground(null);
                }if(t.isSelected() && t.getChar() == TilePile.blank){ //accounts for blank tiles
                    String letter = JOptionPane.showInputDialog("Enter Letter to Be Substituted").toUpperCase();
                    //check if it's valid
                    while(!letter.toUpperCase().matches("^[A-Z]$")) {
                        letter = JOptionPane.showInputDialog("Enter Letter to Be Substituted").toUpperCase();
                    }
                    t.setChar(letter.charAt(0));
                    b.setText(letter); //Sets new letter's text to tile in tile rack
                }
            });
            this.add(b);
        }
    }
}
