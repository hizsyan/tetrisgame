package tetrisgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The PauseMenu class represents a menu that appears when the game is paused. 
 * It allows the player to resume, save, or quit the game.
 */
public class PauseMenu extends JFrame {
    private JButton Resume; // Button to resume the game
    private JButton SQuit; // Button to save and quit the game
    private JButton Quit; // Button to quit the game without saving
    private JLabel Status; // Label to display the current game status
    private GameFrame Game; // Reference to the main game frame

    /**
     * ActionListener for the Quit button.
     * Closes the game entirely.
     */
    private class QuitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Game.dispatchEvent(new WindowEvent(Game, WindowEvent.WINDOW_CLOSING)); // Close the game
            kill();
        }
    }

    /**
     * ActionListener for the Save & Quit button.
     * Opens the save menu where the user can name and save the current game state.
     */
    private class SQuitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveMenu();
        }
    }

    /**
     * ActionListener for the Resume button.
     * Resumes the paused game and closes the pause menu.
     */
    private class ResumeButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Game.resume(); // Resume the game
            kill();        // Close the pause menu
        }
    }

    /**
     * Closes the Pause Menu window.
     */
    public void kill() {
        this.dispose(); // Close this frame
    }

    /**
     * Displays the Pause Menu window.
     */
    public void display() {
        this.setVisible(true); // Make the frame visible
    }

    /**
     * Opens the save menu where the user can enter a save name and save the game state.
     */
    public void saveMenu() {
        // Clear existing components
        this.getContentPane().removeAll();
    
        // Create a new panel for the save menu
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        savePanel.setSize(200, 200);
    
        // Add a label and text field for entering the save file name
        JLabel saveLabel = new JLabel("Enter Save Name:", SwingConstants.CENTER);
        JTextField saveNameField = new JTextField();
    
        // Create a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String saveName = saveNameField.getText();
            if (!saveName.isEmpty()) {
                this.Game.saveGame(saveName); // Save the game with the specified name
                kill(); // Close the menu after saving
            } else {
                JOptionPane.showMessageDialog(this, "Save name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Add components to the save panel
        savePanel.add(saveLabel, BorderLayout.NORTH);
        savePanel.add(saveNameField, BorderLayout.CENTER);
        savePanel.add(saveButton, BorderLayout.SOUTH);
    
        // Add the save panel to the frame
        this.getContentPane().add(savePanel);
    
        // Revalidate and repaint to update the frame
        this.revalidate();
        this.repaint();
    
        // Make the frame visible (if it isn't already)
        this.setVisible(true);
    }

    /**
     * Constructs the PauseMenu and initializes its components.
     *
     * @param Game The {@link GameFrame} instance representing the main game.
     */
    public PauseMenu(GameFrame Game) {
        this.Game = Game; // Store the reference to the GameFrame

        // Create Components
        JPanel P = new JPanel();
        P.setLayout(new BoxLayout(P, BoxLayout.Y_AXIS));

        this.Resume = new JButton("Resume");
        this.Quit = new JButton("Quit");
        this.SQuit = new JButton("Save & Quit");
        this.Status = new JLabel("Game Paused | Score: " + Game.getBoard().getScore());

        // Set Frame Properties
        setTitle("Pause Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this window
        setSize(200, 200);
        setResizable(false);

        // Add Components to Panel
        P.add(Status);
        P.add(Resume);
        P.add(SQuit);
        P.add(Quit);

        // Add Listeners to Buttons
        Resume.addActionListener(new ResumeButton());
        SQuit.addActionListener(new SQuitButton());
        Quit.addActionListener(new QuitButton());

        // Add Panel to Frame
        this.add(P);

        // Center the Frame
        setLocationRelativeTo(Game);
    }
}
