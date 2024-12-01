package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class TetrisHUD extends JPanel{
    private JLabel scoreLabel = new JLabel("Score: 0");
    private TetrisPanel Tetris;

    JPanel nextTetrominoPanel = new JPanel() {
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

    @Override 
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        this.drawNextTetromino();
        this.updateScore();
    }

    public TetrisHUD(TetrisPanel T){
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

    public void drawNextTetromino() {
        // Clear the panel
        nextTetrominoPanel.removeAll();
        nextTetrominoPanel.revalidate();
        nextTetrominoPanel.repaint();
    }

    public void updateScore() {
        scoreLabel.setText("Score: " + Tetris.getScore());
    }

}