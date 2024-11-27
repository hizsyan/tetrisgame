package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class GameFrame extends JFrame{
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450,800);
        setResizable(false);
        TetrisBoard.repaint();
        this.addKeyListener(new MyKeyListener());
        this.add(TetrisBoard, BorderLayout.CENTER);
    }

    private void gameOver(){
        this.gameTimer.stop();
        JLabel overLabel = new JLabel();
        overLabel.setText("Game Over \n Score:" + this.TetrisBoard.getScore());
        overLabel.setBackground(Color.BLACK);
        overLabel.setForeground(Color.green);
        overLabel.setBounds(100, 300, 150, 200);
        this.add(overLabel, BorderLayout.CENTER); 
        System.out.println("OVER!");
        this.setVisible(true);
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
}
