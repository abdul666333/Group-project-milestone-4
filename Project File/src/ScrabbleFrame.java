import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.ArrayList;

/**
 * ScrabbleFrame is used to generate a graphical interface for user input and display
 * @author Umniyah Mohammed 101158792
 * @version 3.0
 */
public class ScrabbleFrame extends JFrame implements ScrabbleViews {

    private JFrame scrabbleFrame;
    private ScrabbleModel model;
    private BoardModel board;

    private BoardPanel boardPane;
    private RackPanel rackPane;
    private CommandsPanel commandsPane;

    private JLabel currentPlayerText;
    private JLabel scoreText;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenu edit = new JMenu("Edit");
    private JMenuItem undo = new JMenuItem("Undo");
    private JMenuItem redo = new JMenuItem("Redo");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem load = new JMenuItem("Load");
    private MenuController menuController;

    /**
     * A constructor for the ScrabbleFrame class
     */
    public ScrabbleFrame() {
        super("Group 36 Scrabble");
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JOptionPane.showMessageDialog(this,"\nWelcome to the digital word game Scrabble!\n" +
                "After choosing the number of players and their names, the first randomly chosen player\n" +
                "can place a word by clicking on a tile from the rack and on the square on the board.\n" +
                "The five bottom commands do the following:\n" +
                " -> SWAP: swaps selected tiles of the player's rack\n" +
                " -> PASS: passes turn to the next player\n" +
                " -> QUIT: quits the game\n" +
                " -> HELP: shows the help dialog\n" +
                " -> SUBMIT: submit the current word placed on the board"
        );

        ArrayList<String> playerNames = new ArrayList<>();
        String numberOfPlayersString = JOptionPane.showInputDialog(scrabbleFrame,"Please enter the number of players (2-4)");
        while(!numberOfPlayersString.matches("^[2-4]$")) {
            numberOfPlayersString = JOptionPane.showInputDialog(scrabbleFrame,"Please enter the number of players (2-4)");
        }
        String numberOfBotPlayersString = JOptionPane.showInputDialog(scrabbleFrame,"Please enter the number of AI players");
        while(!numberOfBotPlayersString.matches("^[0-3]$")) {
            numberOfBotPlayersString = JOptionPane.showInputDialog(scrabbleFrame,"Please enter the number of AI players");
        }
        String numberOfHumanPlayers = String.valueOf(Integer.parseInt(numberOfPlayersString) - Integer.parseInt(numberOfBotPlayersString));
        int numberOfPlayersInt = Integer.parseInt(numberOfHumanPlayers);
        for (int i = 1; i < numberOfPlayersInt + 1; i++){
            String playerName = JOptionPane.showInputDialog(scrabbleFrame,"Please enter the name of player " + i + ": ");
            playerNames.add(playerName);
        }
        int numberOfBotPlayersInt = Integer.parseInt(String.valueOf(Integer.parseInt(numberOfBotPlayersString)));
        for (int i = 1; i < numberOfBotPlayersInt + 1; i++){
            String playerName = "Bot " + i;
            playerNames.add(playerName);
        }

        this.model = new ScrabbleModel(playerNames);

        model.addScrabbleViews(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = model.getBoard();

        Object[] versionOptions = {"Original Version", "Custom Version"};
        int versionInt = JOptionPane.showOptionDialog(scrabbleFrame,"Which version would you like to play?", "Scrabble Version",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, versionOptions, versionOptions[0]);
        if(versionInt == JOptionPane.YES_OPTION){
            board.setSquareMultipliers();
        } else {
            board.setCustomBoard();
        }

        //Menu Bar
        this.menuBar.add(file);
        this.menuBar.add(edit);
        file.add(save);
        file.add(load);
        edit.add(undo);
        edit.add(redo);
        this.setJMenuBar(menuBar);
        menuController = new MenuController(model);
        save.addActionListener(menuController);
        load.addActionListener(menuController);
        save.setActionCommand("save");
        load.setActionCommand("load");

        undo.addActionListener((menuController));
        redo.addActionListener(menuController);

        undo.setActionCommand("undo");
        redo.setActionCommand("redo");



        //Board Panel
        boardPane = new BoardPanel(board);
        board.addBoardView(boardPane);
        boardPane.setPreferredSize(new Dimension(900, 600));
        boardPane.setMaximumSize(new Dimension(750, 600));
        this.add(boardPane, BorderLayout.NORTH);

        // Rack Panel
        Player player = model.getActivePlayer();
        rackPane = new RackPanel(player);
        for (Player p: model.getPlayers()) {
            p.addPlayerViews(rackPane);
        }
        model.addScrabbleViews(rackPane);

        rackPane.setPreferredSize(new Dimension(50, 50));
        rackPane.setMaximumSize(new Dimension(70, 70));
        this.add(rackPane, BorderLayout.CENTER);

        // Commands Panel
        commandsPane = new CommandsPanel(model);
        //Player and score labels
        currentPlayerText = new JLabel("Player: " + model.getActivePlayer().getName());
        scoreText = new JLabel("Score: " + model.getActivePlayer().getScore());
        commandsPane.add(currentPlayerText);
        commandsPane.add(scoreText);
        this.add(commandsPane, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * @author Umniyah Mohammed
     * result will display the results of the game
     */
    public void result(String results){
        JOptionPane.showMessageDialog(this,results,"Results",JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void handleScrabbleUpdate(ScrabbleEvent e) {
        currentPlayerText.setText("Player: " + e.getActivePlayer().getName());
        scoreText.setText("Score: " + String.valueOf(e.getActivePlayer().getScore()));
    }

    @Override
    public void handleEndGameResults(String s){
        this.result(s);
    }
    @Override
    public void handleWarningMessage(String s){
        JOptionPane.showMessageDialog(this,s,"Message",JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns the full path to the selected file with the given extension
     * @param fileDescription The description of the file which will be shown to the user in the file
     *                        chooser menu as the required file type
     * @param fileExtension The extension of the file to be viewed in the file chooser
     * @param isInputFile True if the file chooser is opening a file (input), and false if it is saving (output)
     * @return Full path to the selected file
     */
    public static String getFileName(String fileDescription, String fileExtension, Boolean isInputFile){
        JFileChooser fileChooser = new JFileChooser("../");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileDescription, fileExtension);
        fileChooser.setFileFilter(filter);
        int option;
        if(isInputFile){
            option = fileChooser.showOpenDialog(null);
        }else{
            option = fileChooser.showSaveDialog(null);
        }
        if(option == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    @Override
    public void updateScrabbleModel(ScrabbleEvent event){
        this.board = event.getBoard();
        handleScrabbleUpdate(event);
    }

    /**
     * main method for the ScrabbleFrame class
     * @param args, String[]
     */
    public static void main(String[] args) {
        new ScrabbleFrame();
    }

}