package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class pauseMenu extends JFrame {
    private JButton Resume;
    private JButton SQuit;
    private JButton Quit;
    private JLabel Status;
    private GameFrame Game;

    // ActionListener for Quit Button
    private class QuitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Game.dispatchEvent(new WindowEvent(Game, WindowEvent.WINDOW_CLOSING)); // Close the game
            kill();
        }
    }

    // ActionListener for Save & Quit Button
    private class SQuitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Save & Quit Clicked (Save logic not implemented)");
            kill();
        }
    }

    // ActionListener for Resume Button
    private class ResumeButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Game.resume(); // Resume the game
            kill();        // Close the pause menu
        }
    }

    // Kill the Pause Menu
    public void kill() {
        this.dispose(); // Close this frame
    }

    // Show the Pause Menu
    public void display() {
        this.setVisible(true); // Make the frame visible
    }

    public void saveMenu(){
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        savePanel.setSize(200,200);
        setResizable(false);
    }

    // Constructor
    public pauseMenu(GameFrame Game) {
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