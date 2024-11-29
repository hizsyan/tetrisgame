package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.*;

public class GameFrame extends JFrame implements Serializable{
    private TetrisPanel TetrisBoard = new TetrisPanel(this);
    private JPanel HUD = new JPanel();
    private Random random;
    private javax.swing.Timer gameTimer;

    class MyKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent k){
            switch (k.getKeyCode()){
                case KeyEvent.VK_RIGHT:
                    TetrisBoard.currTetro.moveRight();
                    break;
                case KeyEvent.VK_LEFT:
                    TetrisBoard.currTetro.moveLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    TetrisBoard.currTetro.pushDown();
                    break;
                case KeyEvent.VK_UP:
                    TetrisBoard.rotateTetro();
                    break;
                case KeyEvent.VK_ESCAPE:
                    Pause();
                    break;
                default:
                    break;
            }
        }
    }

    public Random getRandom(){
        return random;
    }

    public GameFrame(){
        random = new Random();
        HUD.setLayout(new BorderLayout());
        setTitle("Active Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450,800);
        setResizable(false);
        TetrisBoard.repaint();
        this.addKeyListener(new MyKeyListener());
        this.add(TetrisBoard, BorderLayout.CENTER);
    }

    public void resume(){
        this.gameTimer.start();
        this.TetrisBoard.resumeGame();
    }

    private void gameOver(){
     // Stop the game timer
     this.gameTimer.stop();

     // Create Game Over message
     JLabel overLabel = new JLabel("Game Over! Score: " + this.TetrisBoard.getScore(), SwingConstants.CENTER);
     overLabel.setFont(new Font("Arial", Font.BOLD, 20));
     overLabel.setOpaque(true);
     overLabel.setBackground(Color.BLACK);
     overLabel.setForeground(Color.GREEN);
 
     // Clear existing components and set layout
     this.getContentPane().removeAll(); // Remove all components from the JFrame
     this.setLayout(new BorderLayout()); // Set layout for new components
 
     // Add the Game Over label
     this.add(overLabel, BorderLayout.CENTER);
 
     // Revalidate and repaint to refresh the frame
     this.revalidate();
     this.repaint();
 }

    public void drawPauseMenu(){
        pauseMenu PM = new pauseMenu(this);
        PM.display();
    }

    public void Pause(){
        this.gameTimer.stop();
        this.TetrisBoard.pauseGame();
        drawPauseMenu();
    }

    public void runGame(){
        this.setVisible(true);
        TetrisBoard.doFrame();
        gameTimer = new javax.swing.Timer(10, e -> {
            if(!this.TetrisBoard.isGameOn()){
                this.gameOver();
                return;
            }
            TetrisBoard.doFrame(); // Update game logic
            TetrisBoard.repaint(); // Redraw the game board
        });
        gameTimer.start();
    }

    public TetrisPanel getBoard(){
        return this.TetrisBoard;
    }

}
