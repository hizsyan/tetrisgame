package tetrisgame;

import javax.swing.*;
import java.awt.*;

/**
 * TetrisHUD is a graphical component that displays the current game status,
 * including the score and a preview of the next tetromino to the current Game window {@link GameFrame}
 */
public class TetrisHUD extends JPanel {
    private JLabel scoreLabel = new JLabel("Score: 0"); // Label to display the score
    private TetrisPanel Tetris; // Reference to the Tetris game panel

    /**
     * Panel to display the next tetromino. It overrides paintComponent to render the tetromino.
     * 
     */
    private JPanel nextTetrominoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Tetromino next = Tetris.getNextTetro();
            if (next != null) {
                for (Coord coord : next.getShape().getRelative()) {
                    g.setColor(next.getColor());
                    int x = (coord.getX() + 1) * 20; // Adjust scale
                    int y = (coord.getY() + 1) * 20;
                    g.fillRect(x, y, 20, 20);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, 20, 20);
                }
            }
        }
    };

    /**
     * Paints the TetrisHUD component, updating the score and next tetromino display.
     * Updating the HUD is controlled by a flag in the gamestate {@link TetrisPanel} that stores whether the information displayed needs updating
     * @param g The Graphics object used to render the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawNextTetromino();
        this.updateScore();
    }

    /**
     * Constructs the TetrisHUD with a reference to the Tetris game panel. {@link TetrisPanel}
     * Needs access to the Tetris game in order to display the correct information
     * @param T The TetrisPanel object representing the game state.
     */
    public TetrisHUD(TetrisPanel T) {
        this.Tetris = T;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(130, 600));
        this.setBackground(Color.LIGHT_GRAY);

        // Score Label
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        this.add(scoreLabel);

        // Next Tetromino Label
        JLabel nextLabel = new JLabel("Next:");
        nextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        this.add(nextLabel);

        // Next Tetromino Panel
        nextTetrominoPanel.setPreferredSize(new Dimension(100, 100));
        nextTetrominoPanel.setBackground(Color.WHITE);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(nextTetrominoPanel);

        this.add(Box.createVerticalGlue()); // Push content to the top
    }

    /**
     * Renders the next tetromino in the dedicated panel.
     */
    public void drawNextTetromino() {
        // Clear and repaint the next tetromino panel
        nextTetrominoPanel.removeAll();
        nextTetrominoPanel.revalidate();
        nextTetrominoPanel.repaint();
    }

    /**
     * Updates the score label with the current score from the TetrisPanel.
     */
    public void updateScore() {
        scoreLabel.setText("Score: " + Tetris.getScore());
    }
}
