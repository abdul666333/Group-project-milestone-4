import javax.swing.*;
import java.awt.*;

/**
 * The panel for the four commands: swap, pass, quit, and submit
 * @author Umniyah Mohammed, 101158792
 * @version 1.0
 */
public class CommandsPanel extends JPanel{
    private JButton swapButton;
    private JButton passButton;
    private JButton quitButton;
    private JButton submitButton;
    private JButton helpButton;
    private SwapController swapController;
    private PassController passController;
    private QuitController quitController;
    private SubmitController submitController;
    private HelpController helpController;

    /**
     * The constructor for CommandsPanel which adds the 4 command buttons to the panel
     * @param model, the scrabble model
     */
    public CommandsPanel(ScrabbleModel model){
        super();
        //instantiating controllers
        swapController = new SwapController(model);
        passController = new PassController(model);
        quitController = new QuitController(model);
        helpController = new HelpController(model);
        submitController = new SubmitController(model);

        //Setting the layout
        this.setLayout(new FlowLayout());

        //instantiating buttons and adding them to the panel
        swapButton = new JButton("SWAP");
        passButton = new JButton("PASS");
        quitButton = new JButton("QUIT");
        helpButton = new JButton("HELP");
        submitButton = new JButton("SUBMIT");
        this.add(swapButton);
        swapButton.addActionListener(swapController);
        this.add(passButton);
        passButton.addActionListener(passController);
        this.add(quitButton);
        quitButton.addActionListener(quitController);
        this.add(helpButton);
        helpButton.addActionListener(helpController);
        this.add(submitButton);
        submitButton.addActionListener(submitController);
        swapButton.setBackground(Color.WHITE);
        passButton.setBackground(Color.WHITE);
        quitButton.setBackground(Color.WHITE);
        helpButton.setBackground(Color.WHITE);
        submitButton.setBackground(Color.WHITE);
    }
}