package tetrisgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The StartWindow class serves as the entry point for the Tetris game.
 * providing a menu to start a new game, load a saved game, or quit.
 */
public class StartWindow extends JFrame {
    private GameFrame CurrentGame; // Reference to the current game instance
    private JButton newgame = new JButton("New Game"); // Button to start a new game
    private JButton loadgame = new JButton("Load Game"); // Button to load a saved game
    private JButton quit = new JButton("Quit"); // Button to quit the application
    private JPanel panel = new JPanel(); // Panel to hold the buttons

    /**
     * Constructor for StartWindow.
     * Initializes the UI components and adds action listeners for the buttons.
     */
    public StartWindow() {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 110);
        setResizable(false);

        // Add buttons to the panel
        panel.add(newgame);
        panel.add(loadgame);
        panel.add(quit);
        this.add(panel);

        // Attach action listeners to buttons
        newgame.addActionListener(new StartButton());
        loadgame.addActionListener(new LoadButton());
        quit.addActionListener(new QuitButton());
    }

    /**
     * Opens a file chooser dialog to allow the user to select a saved game file
     * and loads the game from the selected file. 
     * The loading is done with a special constructor of the {@link GameFrame} class which takes a path as an argument
     * The {@link TetrisPanel} of the new GameFrame will be loaded from this path, if it exists and is a correct savefile.
     */
    public void loadGame() {
        // Show the file chooser dialog
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Select Saved Game");

        int userSelection = saveChooser.showOpenDialog(this); // Use showOpenDialog for loading

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = saveChooser.getSelectedFile();

            // Initialize the game frame with the selected file path
            CurrentGame = new GameFrame(fileToLoad.getAbsolutePath());
            CurrentGame.runGame(); // Start the game
        }
    }

    /**
     * ActionListener for the Load Game button.
     * Opens the load game menu.
     */
    public class LoadButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadGame();
        }
    }

    /**
     * ActionListener for the Quit button.
     * Exits the application.
     */
    public class QuitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    /**
     * ActionListener for the New Game button.
     * Starts a new Tetris game window. {@link GameFrame}
     */
    public class StartButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CurrentGame = new GameFrame();
            CurrentGame.runGame();
        }
    }

    /**
     * The main method to launch the StartWindow.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        StartWindow frame = new StartWindow();

        // Make the window visible
        frame.setVisible(true);
    }
}
